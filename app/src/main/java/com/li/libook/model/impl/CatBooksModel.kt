package com.li.libook.model.impl

import cn.sharelink.kotlinweb.util.http.HttpUtil
import com.li.libook.model.ICatBooksModel
import com.li.libook.model.entity.BooksByCat
import com.orhanobut.logger.Logger

class CatBooksModel() : ICatBooksModel{

    private val httpUtil: HttpUtil by lazy {
        HttpUtil()
    }

    override fun initData(map: MutableMap<String,Any>,rCall: (statis: BooksByCat) -> Unit) {
        Thread(){
            httpUtil.bookByCat(map)
                .subscribe({ it->
                    initCall = rCall
                    initCall(it)
                    Logger.i("数据："+it.books.size)
                })
                { throwable -> Logger.i("请求失败："+throwable.message) }
        }.start()
    }

    override fun loadData(map: MutableMap<String,Any>,rCall: (statis: BooksByCat) -> Unit) {
        Thread(){
            httpUtil.bookByCat(map)
                .subscribe({ it->
                    initCall = rCall
                    initCall(it)
                    Logger.i("数据："+it.books.size)
                })
                { throwable -> Logger.i("请求失败："+throwable.message) }
        }.start()
    }

    lateinit var initCall:(statis: BooksByCat) -> Unit
}