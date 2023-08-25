package me.fycz.fqweb.web

import android.graphics.Bitmap
import fi.iki.elonen.NanoHTTPD
import me.fycz.fqweb.constant.Config
import me.fycz.fqweb.utils.HttpUtils
import me.fycz.fqweb.utils.JsonUtils
import me.fycz.fqweb.web.controller.DragonController
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * @author fengyue
 * @date 2023/5/29 17:58
 * @description
 */
class HttpServer(port: Int) : NanoHTTPD(port) {

    private val defaultPage =
        "<!DOCTYPE html>\n<html>\n<head>\n    <title>Not Found</title>\n    <style>\n    body {\n        width: 35em;\n        margin: 0 auto;\n        font-family: Tahoma, Verdana, Arial, sans-serif;\n    }\n\n    </style>\n</head>\n<body>\n<h1><a href=\"https://github.com/fengyuecanzhu/FQWeb\">FQWeb</a>: 404 Not Found.</h1>\n<p>Sorry, the page you are looking for does not exist.</p>\n<p>The server is powered by <a href=\"https://github.com/fengyuecanzhu/FQWeb\">FQWeb</a>.</p>\n<p><em>Faithfully yours, FQWeb.</em></p>\n</body>\n</html>"

    override fun serve(session: IHTTPSession): Response {
        var returnData: ReturnData? = null
        val ct = ContentType(session.headers["content-type"]).tryUTF8()
        session.headers["content-type"] = ct.contentTypeHeader
        val uri = session.uri
        try {
            if (session.method == Method.GET) {
                val parameters = session.parameters
                returnData = when (uri) {
                    "/search" -> DragonController.search(parameters)
                    "/info" -> DragonController.info(parameters)
                    "/minfo" -> DragonController.mInfo(parameters)
                    "/catalog" -> DragonController.catalog(parameters)
                    "/content" -> DragonController.content(parameters)
                    "/reading/bookapi/bookmall/cell/change/v1/" -> DragonController.bookMall(parameters)
                    "/reading/bookapi/new_category/landing/v/" -> DragonController.newCategory(parameters)
                    else -> null
                }
            }/* else if (session.method == Method.POST) {
                val parameters = session.parameters
                val files = HashMap<String, String>()
                session.parseBody(files)
                val postBody = files["postData"]
            }*/
            if (uri == "/") {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, defaultPage)
            }
            if (returnData == null) {
                return try {
                    newFixedLengthResponse(
                        Response.Status.OK,
                        "application/json",
                        HttpUtils.doGet("${Config.FQ_HOST_URL}$uri?${session.queryParameterString}")
                    )
                } catch (e: Throwable) {
                    newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, defaultPage)
                }
            }
            val response = when (returnData.data) {
                is Bitmap -> {
                    val outputStream = ByteArrayOutputStream()
                    (returnData.data as Bitmap).compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        outputStream
                    )
                    val byteArray = outputStream.toByteArray()
                    outputStream.close()
                    val inputStream = ByteArrayInputStream(byteArray)
                    newFixedLengthResponse(
                        Response.Status.OK,
                        "image/png",
                        inputStream,
                        byteArray.size.toLong()
                    )
                }

                else -> newFixedLengthResponse(
                    Response.Status.OK,
                    "application/json",
                    JsonUtils.toJson(returnData)
                )
            }
            response.addHeader("Access-Control-Allow-Methods", "GET, POST")
            response.addHeader("Access-Control-Allow-Origin", session.headers["origin"])
            return response
        } catch (e: Throwable) {
            return newFixedLengthResponse(e.stackTraceToString())
        }
    }
}
