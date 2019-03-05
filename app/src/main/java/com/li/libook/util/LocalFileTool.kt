package com.li.libook.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.net.URI


class LocalFileTool(){

    companion object {
        val instance:LocalFileTool = LocalFileTool()
        val txtType= arrayOf("text/plain")//"application/msword","application/pdf","application/vnd.ms-powerpoint","text/plain","application/vnd.ms-works"

        fun getFileName(pathandname: String): String? {
            val start = pathandname.lastIndexOf("/")
            val end = pathandname.lastIndexOf(".")
            return if (start != -1 && end != -1) {
                pathandname.substring(start + 1, end)
            } else {
                null
            }
        }
    }

    fun readFile(type:Array<String>, mContext:Context,rCall:(MutableList<String>) -> Unit){
        readCall = rCall
        Observable.just(mContext).map(object : Function1<Context, MutableList<String>> {
            override fun invoke(p1: Context): MutableList<String> {
                val paths = mutableListOf<String>()
                var uri:Uri = MediaStore.Files.getContentUri("external")
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                    uri = FileProvider.getUriForFile(mContext,"com.li.libook.readfile", File(Environment.getExternalStorageDirectory().path))
                val fileUri = arrayOf(uri)
                val colums  = arrayOf(MediaStore.Files.FileColumns.DATA)
                var extension = type

                //构造筛选语句
                var selection = ""
                for (i in 0 until extension.size){
                    if (i != 0){
                        selection += " OR "
                    }
                    selection += MediaStore.Files.FileColumns.MIME_TYPE + " LIKE '%" + extension[i] + "'"
                }

                //获取内容解析器对象
                var resolver:ContentResolver = p1.contentResolver
                for (j in 0 until fileUri.size){
                    var cursor:Cursor = resolver.query(fileUri[j], colums, selection, null, null)
                    if (null == cursor) {
                        return paths
                    }
                    //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
                    var beginTime:Long = System.currentTimeMillis()
                    if (cursor.moveToLast()){
                        do {
                            paths.add(cursor.getString(0))
                        }while (cursor.moveToPrevious())
                    }
                    cursor.close()
                }
                return paths
            }
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                readCall(it)
            }

    }

    private lateinit var readCall:(MutableList<String>) -> Unit
}