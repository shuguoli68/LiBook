package com.li.libook.view

import com.li.libook.model.entity.BooksByCat

interface ICatBooksView{
    fun initData(books1: BooksByCat)
    fun loadData(books2: BooksByCat)
}