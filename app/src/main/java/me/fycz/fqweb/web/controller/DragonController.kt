package me.fycz.fqweb.web.controller

import me.fycz.fqweb.constant.Config
import me.fycz.fqweb.utils.HttpUtils
import me.fycz.fqweb.utils.getObjectField
import me.fycz.fqweb.utils.log
import me.fycz.fqweb.web.ReturnData
import me.fycz.fqweb.web.service.DragonService


/**
 * @author fengyue
 * @date 2023/5/29 18:04
 * @description
 */
object DragonController {

    fun search(parameters: Map<String, List<String>>): ReturnData {
        val keyword = parameters["query"]?.firstOrNull()
        val page = parameters["page"]?.firstOrNull()?.toInt() ?: 1
        val tabType = parameters["tab_type"]?.firstOrNull()?.toInt() ?: 1
        val returnData = ReturnData()
        if (keyword.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数query不能为空")
        }
        returnData.setData(DragonService.search(keyword, page, tabType))
        return returnData
    }

    fun info(parameters: Map<String, MutableList<String>>): ReturnData {
        val bookId = parameters["book_id"]?.firstOrNull()
        val returnData = ReturnData()
        if (bookId.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数book_id不能为空")
        }
        returnData.setData(DragonService.getInfo(bookId))
        return returnData
    }

    fun mInfo(parameters: Map<String, MutableList<String>>): ReturnData {
        val bookId = parameters["book_id"]?.firstOrNull()
        val returnData = ReturnData()
        if (bookId.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数book_id不能为空")
        }
        returnData.setData(DragonService.getMInfo(bookId))
        return returnData
    }

    fun catalog(parameters: Map<String, MutableList<String>>): ReturnData {
        val bookId = parameters["book_id"]?.firstOrNull()
        val returnData = ReturnData()
        if (bookId.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数book_id不能为空")
        }
        returnData.setData(DragonService.getCatalog(bookId))
        return returnData
    }

    fun content(parameters: Map<String, MutableList<String>>): ReturnData {
        val itemId = parameters["item_id"]?.firstOrNull()
        val returnData = ReturnData()
        if (itemId.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数item_id不能为空")
        }
        val content = DragonService.getContent(itemId)
        try {
            DragonService.decodeContent(content.getObjectField("data") as Any)
            returnData.setData(content)
        } catch (e: Throwable) {
            if (itemId == "1") {
                returnData.setData(content)
            } else {
                log("Decode Content item_id=$itemId error：\n${e.stackTraceToString()}")
                returnData.setData(e.stackTraceToString())
                returnData.setErrorMsg("章节item_id=${itemId}内容解密失败，可能是当前番茄版本(${Config.versionCode})未适配或者该章节不存在")
            }
        }
        return returnData
    }

    fun bookMall(parameters: Map<String, MutableList<String>>): ReturnData {
        val returnData = ReturnData()
        returnData.setData(DragonService.bookMall(parameters))
        return returnData
    }

    fun newCategory(parameters: Map<String, MutableList<String>>): ReturnData {
        val returnData = ReturnData()
        returnData.setData(DragonService.newCategory(parameters))
        return returnData
    }

    fun anyUrl(uri: String, paramString: String): ReturnData {
        val returnData = ReturnData()
        try {
            returnData.setData(HttpUtils.doFQGet("${Config.FQ_HOST_URL}$uri?$paramString"))
        } catch (e: Throwable) {
            returnData.setErrorMsg(e.stackTraceToString())
        }
        return returnData
    }
}