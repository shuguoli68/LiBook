package com.li.libook.model

import com.li.libook.model.entity.BooksByCat
import com.li.libook.model.entity.ZhuiStatis

interface ICatBooksModel{
    fun initData(map: MutableMap<String,Any>,rCall:(statis: BooksByCat) -> Unit)
    fun loadData(map: MutableMap<String,Any>,rCall:(statis: BooksByCat) -> Unit)
}