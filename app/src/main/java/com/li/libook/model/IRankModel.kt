package com.li.libook.model

import com.li.libook.model.entity.Rank

interface IRankModel{
    fun left():MutableList<String>
    fun right(rCall:(rank:Rank) -> Unit)
}