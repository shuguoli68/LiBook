package com.li.libook.view

import com.li.libook.model.entity.ZhuiStatis

interface ISortView{
    fun leftV(LList:MutableList<String>)
    fun rightV(statis: ZhuiStatis)
}