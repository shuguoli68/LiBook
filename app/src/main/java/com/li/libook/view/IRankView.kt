package com.li.libook.view

import com.li.libook.model.entity.Rank

interface IRankView{
    fun leftV(LList:MutableList<String>)
    fun rightV(rank: Rank)
}