package me.fycz.fqweb.web

import me.fycz.fqweb.BuildConfig
import me.fycz.fqweb.constant.Config.TRAVERSAL_CONFIG_URL
import me.fycz.fqweb.entity.NATTraversalConfig
import me.fycz.fqweb.entity.ServerConfig
import me.fycz.fqweb.utils.GlobalApp
import me.fycz.fqweb.utils.HttpUtils
import me.fycz.fqweb.utils.JsonUtils
import me.fycz.fqweb.utils.SPUtils
import me.fycz.fqweb.utils.ToastUtils
import me.fycz.fqweb.utils.callStaticMethod
import me.fycz.fqweb.utils.findClass
import me.fycz.fqweb.utils.log
import java.io.File
import java.util.Random

/**
 * @author fengyue
 * @date 2023/7/24 13:53
 * @description
 */
class FrpcServer {
    private var myThread: Thread? = null

    private var heartThread: Thread? = null

    private var heartDuration: Long = 30 * 1000L
    val isAlive: Boolean
        get() = myThread?.isAlive == true

    var isFailed = false

    var status: String = "未启动"

    var domain: String = "未获取"

    var token: String = ""

    private val retry: Int = 1

    var traversalConfig: NATTraversalConfig? = null

    val servers get() = traversalConfig?.servers?.filter { it.check() }

    var currentServer: ServerConfig? = null

    private val configFile: File by lazy {
        File(GlobalApp.application?.filesDir?.absolutePath + "/frpc.ini")
    }

    init {
        initConfig(false) { }
    }

    fun start(manual: Boolean = false) {
        if (myThread?.isAlive == true) return
        if (manual) ToastUtils.toast("正在启动内网穿透服务...")
        initConfig() {
            myThread = Thread {
                try {
                    "frpclib.Frpclib".findClass(javaClass.classLoader)
                        .callStaticMethod("run", configFile.absolutePath)
                } catch (e: Throwable) {
                    log(e)
                    ToastUtils.toastLong("内网穿透服务启动失败\n${e.localizedMessage}")
                    isFailed = true
                    status = "启动失败"
                }
            }.apply {
                isDaemon = true
                name = "Frp Client"
            }.also {
                it.start()
                status = "正在启动"
            }
        }
    }

    fun initConfig(isStart: Boolean = true, callback: () -> Unit) {
        Thread {
            var throwable: Throwable? = null
            for (i in 1..retry) {
                try {
                    val json = HttpUtils.doGet(TRAVERSAL_CONFIG_URL)
                    if (json.isEmpty()) throw RuntimeException("json数据为空")
                    traversalConfig = JsonUtils.fromJson(json, NATTraversalConfig::class.java)
                    if (traversalConfig?.enable != true) {
                        if (isStart) ToastUtils.toastLong("内网穿透服务已关闭")
                        return@Thread
                    }
                    if (BuildConfig.VERSION_CODE < traversalConfig!!.minVersion!!) {
                        if (isStart) ToastUtils.toastLong("当前番茄Web版本过低，已不支持内网穿透服务")
                        return@Thread
                    }
                    if (traversalConfig!!.servers.isNullOrEmpty()) {
                        if (isStart) ToastUtils.toastLong("当前没有可用的内网穿透服务")
                        return@Thread
                    }
                    throwable = null
                    break
                } catch (e: Throwable) {
                    throwable = e
                    log(e)
                }
            }
            if (throwable != null) {
                if (isStart) ToastUtils.toastLong("无法获取内网穿透服务配置，请更新番茄Web到最新版后重试\n${throwable.localizedMessage}")
                return@Thread
            }
            val selectServer = SPUtils.getString("selectServer")
            servers?.forEach {
                if (it.name == selectServer) {
                    currentServer = it
                    return@forEach
                }
            }
            if (currentServer == null) {
                currentServer = servers?.firstOrNull { it.check() }
            }
            if (isStart) writeConfig(callback)
        }.start()
    }

    private fun writeConfig(callback: () -> Unit) {
        if (currentServer == null) {
            ToastUtils.toastLong("当前没有可用的内网穿透服务")
            return
        }
        val timestamp = System.currentTimeMillis().toString()
        domain = currentServer!!.customDomain!!.replace("{timestamp}", timestamp)
        val config =
            currentServer!!.frpcConfig!!.replace("{port}", SPUtils.getInt("port", 9999).toString())
                .replace("{timestamp}", timestamp).replace("{domain}", domain)
        configFile.writeText(config)
        token = SPUtils.getString("token", "")!!
        if (token.isEmpty()) {
            reGenerateToken()
        }
        uploadDomain()
        callback()
    }

    fun reGenerateToken() {
        token = getRandomStr(12)
        SPUtils.putString("token", token)
    }

    private fun uploadDomain() {
        heartThread = Thread {
            Thread.sleep(1000)
            while (!isFailed && isAlive) {
                status = try {
                    val res = HttpUtils.doGet("http://$domain/content?item_id=1")
                    if (res.contains("该书不存在")) {
                        if (currentServer?.uploadOnlySelf == true) {
                            uploadDomain(currentServer!!)
                        } else {
                            servers?.forEach {
                                uploadDomain(it)
                            }
                        }
                        "在线"
                    } else {
                        "无效"
                    }
                } catch (e: Throwable) {
                    log(e)
                    "离线"
                }
                kotlin.runCatching { Thread.sleep(heartDuration) }
            }
        }.apply {
            isDaemon = true
            name = "Heart thread"
        }.also {
            it.start()
        }
    }

    private fun uploadDomain(serverConfig: ServerConfig) {
        try {
            if (!serverConfig.uploadDomainUrl.isNullOrEmpty()) {
                HttpUtils.doGet(
                    serverConfig.uploadDomainUrl!!.replace("{domain}", domain)
                        .replace("{token}", token)
                )
            }
        } catch (e: Throwable) {
            log(e)
        }
    }

    //生成随机数字和字母
    private fun getRandomStr(length: Int): String {
        val sb = StringBuilder()
        val random = Random()

        //参数length，表示生成几位随机数
        for (i in 0 until length) {
            //输出字母还是数字
            when (if (random.nextInt(2) % 2 == 0) "char" else "num") {
                "char" -> {
                    //输出是大写字母还是小写字母
                    val temp = if (random.nextInt(2) % 2 == 0) 65 else 97
                    sb.append((random.nextInt(26) + temp).toChar())
                }

                "num" -> sb.append(random.nextInt(10).toString())
            }
        }
        return sb.toString()
    }
}