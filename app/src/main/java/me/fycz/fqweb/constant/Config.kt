package me.fycz.fqweb.constant

import de.robv.android.xposed.XposedHelpers
import me.fycz.fqweb.utils.GlobalApp
import me.fycz.fqweb.utils.findClass
import me.fycz.fqweb.utils.findClassOrNull

/**
 * @author fengyue
 * @date 2023/5/30 8:35
 * @description
 */
object Config {

    val isFrpcVersion: Boolean by lazy {
        "frpclib.Frpclib".findClassOrNull(javaClass.classLoader) != null
    }

    const val TRAVERSAL_CONFIG_URL =
        "https://gitee.com/fengyuecanzhu/FQWeb/raw/master/traversal/config.json"

    const val DEFAULT_USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4168.3 Safari/537.36"

    const val DISCLAIMER =
        "1、该Xposed模块仅供学习交流使用，使用者必须自行承担使用该模块所带来的风险和责任。\n\n2、使用该Xposed模块可能导致设备不稳定、崩溃和数据丢失等问题。作者不对任何因使用该模块而导致的问题承担责任。\n\n3、开发者保留对该Xposed模块的更新、修改、暂停、终止等权利，使用者应该自行确认其使用版本的安全性和稳定性。\n\n4、任何人因使用该Xposed模块而导致的任何问题，作者不承担任何责任，一切后果由使用者自行承担。\n\n5、对于使用该Xposed模块所产生的任何问题，作者不提供任何形式的技术支持和解决方案。\n\n请在使用该Xposed模块之前认真阅读以上免责声明并自行权衡风险和利益，如有异议请勿使用。如果您使用了该Xposed模块，即代表您已经完全接受本免责声明。"

    const val TRAVERSAL_DISCLAIMER =
        "<p>\n    内网穿透是一项技术，旨在帮助用户通过公共网络访问位于私有网络中的设备或服务。通过此服务，您可以远程访问您的设备，无论其在全球的位置如何，这在许多场景下都非常方便。然而，内网穿透涉及将您的私有网络设备暴露在公共网络之下，这意味着潜在的安全风险可能会增加。</p>\n\n<p><font color=\"red\">特别注意：此内网穿透服务旨在于建立共享式API接口，因此你的公网地址将会上传到地址记录池，任何使用相关API的人员都可获得此地址(随机获取)，但其无法获取到设备相关信息</font></p>\n\n<h3>一、内网穿透风险警告：</h3>\n\n<p>\n    1、未经授权访问：内网穿透允许外部用户通过互联网访问内部网络或设备。如果安全措施不当或配置不当，可能会导致未经授权的人员获得对您内部系统和数据的访问权限。</p>\n<p>\n    2、数据泄露：如果内网穿透服务存在安全漏洞或第三方服务提供商的数据处理不当，可能导致敏感数据或机密信息泄露到公共网络上。</p>\n<p>3、恶意攻击：内网穿透服务开放了内部网络的端口，这为潜在的黑客或恶意攻击者提供了潜在的入口。他们可能尝试利用这些开放的端口进行入侵、DDoS\n    攻击、恶意软件传播等活动。</p>\n<p>\n    4、服务不稳定：内网穿透服务可能受到网络状况、带宽限制、服务器故障等因素的影响，导致连接不稳定或服务不可用。</p>\n<p>\n    5、第三方服务风险：当前内网穿透服务由第三方提供，您将面临依赖于第三方供应商的问题，包括其服务的可靠性、安全性和合规性。</p>\n<p>6、端口暴露：\n    内网穿透开放了内部网络的端口，这也就意味着攻击者可以尝试扫描这些开放端口，寻找可能的漏洞进行攻击。</p>\n<p>7、不稳定网络：\n    如果用户的内部网络连接不稳定，可能会导致内网穿透服务的性能下降，或者甚至无法正常使用。</p>\n<p>8、合规和法律问题：\n    某些地区或组织可能对内网穿透服务有特定的法律要求或限制。用户需要确保使用内网穿透功能的合规性，以避免可能的法律问题。</p>\n<h3>二、免责声明：</h3>\n<p>\n    1、对于因使用内网穿透服务导致的任何安全问题或数据泄露，开发者不承担任何责任。用户应自行评估使用内网穿透功能所带来的风险，并采取适当的措施保护其网络和设备。</p>\n<p>\n    2、开发者无法对第三方内网穿透服务的安全性、稳定性或适用性作出任何保证。对于用户在使用该功能时遇到的任何问题，包括但不限于连接问题、数据丢失、服务不稳定等，开发者不承担任何责任。</p>\n<p>\n    3、用户理解并同意，由于互联网本身的不稳定性和不可控性，开发者不对因网络原因造成的任何损失或损害承担责任。</p>\n<p>\n    4、开发者保留随时修改、暂停或终止内网穿透功能的权利，而无需提前通知用户。对于因此造成的任何损失或不便，开发者不承担责任。</p>\n<p>\n    在使用内网穿透服务前，请您务必审慎考虑并遵守上述警告和免责声明。如果您对内网穿透服务的安全性有任何疑虑，建议您不要使用该功能。</p>"


    private val dragonClassloader by lazy { GlobalApp.getClassloader() }

    val settingRecyclerAdapterClz: String by lazy {
        when {
            versionCode == 523 -> "com.dragon.read.base.recyler.c"
            versionCode == 57932 -> "com.dragon.read.recyler.c"
            versionCode < 58732 -> {
                kotlin.runCatching {
                    "com.dragon.read.recyler.c".findClass(dragonClassloader)
                    return@lazy "com.dragon.read.recyler.c"
                }
                "com.dragon.read.base.recyler.c"
            }
            else -> "com.dragon.read.recyler.d"
        }
    }

    val settingItemQSNClz: String by lazy {
        val prefix = "com.dragon.read.component.biz.impl.mine.settings.a"
        when (versionCode) {
            523 -> "$prefix.g"
            57932 -> "$prefix.k"
            else -> {
                kotlin.runCatching {
                    "$prefix.k".findClass(dragonClassloader)
                    return@lazy "$prefix.k"
                }
                "$prefix.g"
            }
        }
    }

    val settingAdapterFiledName: String by lazy {
        when {
            versionCode < 58732 -> "b"
            else -> "a"
        }
    }

    val settingItemStrFieldName: String by lazy {
        when {
            versionCode < 58732 -> "e"
            else -> "d"
        }
    }

    val settingItemSubStrFieldName: String by lazy {
        when (versionCode) {
            523 -> "i"
            57932 -> "i"
            58332 -> "j"
            else -> {
                val settingItemClz =
                    "com.dragon.read.pages.mine.settings.e".findClass(dragonClassloader)
                val iField = XposedHelpers.findField(settingItemClz, "i")
                if (iField.type == CharSequence::class.java) {
                    "i"
                } else {
                    "j"
                }
            }
        }
    }

    val readerFullRequestClz: String by lazy {
        when (versionCode) {
            523 -> "$rpcApiPackage.e"
            57932 -> "$rpcApiPackage.e"
            58332 -> "$rpcApiPackage.f"
            else -> {
                val FullRequest =
                    "$rpcModelPackage.FullRequest".findClass(dragonClassloader)
                kotlin.runCatching {
                    XposedHelpers.findMethodExact(
                        "$rpcApiPackage.e",
                        dragonClassloader,
                        "a",
                        FullRequest
                    )
                    return@lazy "$rpcApiPackage.e"
                }
                "$rpcApiPackage.f"
            }
        }
    }

    val rpcApiPackage: String by lazy {
        val prefix = "com.dragon.read.rpc"
        when (versionCode) {
            523 -> "$prefix.a"
            57932 -> "$prefix.rpc"
            else -> {
                kotlin.runCatching {
                    "$prefix.rpc.a".findClass(dragonClassloader)
                    return@lazy "$prefix.rpc"
                }
                "$prefix.a.a".findClass(dragonClassloader)
                "$prefix.a"
            }
        }
    }

    const val rpcModelPackage = "com.dragon.read.rpc.model"

    val versionCode: Int by lazy {
        try {
            val manager =
                GlobalApp.application!!.packageManager
            val info = manager.getPackageInfo(GlobalApp.application!!.packageName, 0)
            info.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}
