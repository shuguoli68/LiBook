package com.li.libook.model

import com.li.libook.model.entity.ZhuiStatis

interface ISortModel{
    fun left():MutableList<String>
    fun right(rCall:(statis: ZhuiStatis) -> Unit)
}