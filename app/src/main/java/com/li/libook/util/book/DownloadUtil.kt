package com.li.libook.util.book

import android.text.TextUtils
import cn.sharelink.kotlinweb.util.http.HttpUtil
import com.li.libook.model.MyConfig
import com.li.libook.model.bean.DownLoad
import com.li.libook.util.DBUtil
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileWriter
import java.io.IOException

class DownloadUtil(){

    private var isDwon:Boolean = true
    private var map:MutableMap<String,String> = mutableMapOf()
    private var resourceId:String = "56f8da09176d03ac1983f6cd"

    private val httpUtil: HttpUtil by lazy {
        HttpUtil()
    }

    fun isDown(b:Boolean){
        isDwon = b
    }

    fun start(down:DownLoad,downCall: DownCall){
        map["view"] = "summary"
        map["book"] = down.id
        Thread(){
            httpUtil.bookResource(map).subscribe ({
                if (!TextUtils.isEmpty(it[0]._id)){
                    resourceId = it[0]._id
                    httpUtil.allChapter(resourceId)
                        .subscribe({ resourceChapter->
                            Logger.i("数据："+resourceChapter.name)
                            val list = resourceChapter.chapters
                            down.total = list.size.toString()
                            DBUtil.instance.addDown(down)
                            downCall.init(down.total)
                            var i = down.progress.toInt()
                            while (i < list.size && isDwon){
                                if (!list[i].isVip){
                                    httpUtil.chapContent(list[i].link).subscribe({chapterContent->
                                        val content:String = chapterContent.chapter.cpContent
                                        Logger.i("成功：$content")
                                        writeToTxt(content, MyConfig.ROOT_ONLINE_PATH,down.name)
                                        downCall.now(i)
                                    }){throwable3 ->
                                        Logger.i("请求失败："+throwable3.message)
                                    }
                                }else{
                                    Logger.i("剩下的全是VIP章节")
                                    break
                                }
                                i++
                            }
                            down.progress = i.toString()
                            DBUtil.instance.addDown(down)
                        })
                        { throwable -> Logger.i("allChapter请求失败："+throwable.message) }
                }
            })
            {
                Logger.i("bookResource请求失败："+it.message)
            }

        }.start()
    }

    interface DownCall{
        fun init(total:String)
        fun now(progress:Int)
    }

    /**
     * @param content 文件内容
     * @param filePath 文件路径(不要以/结尾)
     * @param fileName 文件名称（包含后缀,如：ReadMe.txt）
     */
    private fun writeToTxt(content: String, filePath: String, fileName: String) {
        val thisFile = File(filePath+File.separator+fileName)
        try {
            if (!thisFile.parentFile.exists()) {
                thisFile.parentFile.mkdirs()
            }
            if (!thisFile.exists()){
                thisFile.createNewFile()
            }
            val fw = FileWriter(filePath+File.separator+fileName, true)
            fw.write(content)
            fw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}