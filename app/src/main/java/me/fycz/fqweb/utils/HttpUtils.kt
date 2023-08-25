package me.fycz.fqweb.utils


/**
 * @author fengyue
 * @date 2023/7/24 18:08
 * @description
 */
object HttpUtils {
    fun doGet(url: String): String {
        return "com.ss.android.common.util.NetworkUtils"
            .findClass(GlobalApp.getClassloader())
            .callStaticMethod("executeGet", -1, url) as String
    }
}