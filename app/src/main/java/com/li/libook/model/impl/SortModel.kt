package com.li.libook.model.impl

import android.content.Context
import cn.sharelink.kotlinweb.util.http.HttpUtil
import com.li.libook.R
import com.li.libook.model.ISortModel
import com.li.libook.model.entity.ZhuiStatis
import com.orhanobut.logger.Logger

class SortModel(private val mContext: Context):ISortModel{
    private val httpUtil: HttpUtil by lazy {
        HttpUtil()
    }

    override fun left(): MutableList<String> {
        var list1:MutableList<String> = mutableListOf()
        list1.add(mContext.getString(R.string.male)+"_"+true)
        list1.add(mContext.getString(R.string.female)+"_"+false)
        list1.add(mContext.getString(R.string.picture)+"_"+false)
        list1.add(mContext.getString(R.string.press)+"_"+false)
        return list1
    }

    override fun right(rCall:(statis: ZhuiStatis) -> Unit){
        Thread(){
            httpUtil.category()
                .subscribe({ it->
                    rightCall = rCall
                    rightCall(it)
                    Logger.i("数据："+it.female.size)
                })
                { throwable -> Logger.i("请求失败："+throwable.message) }
        }.start()
    }

    lateinit var rightCall:(statis: ZhuiStatis) -> Unit
}