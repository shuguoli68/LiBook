package com.li.libook.presenter

import android.content.Context
import com.li.libook.model.ICatBooksModel
import com.li.libook.model.impl.CatBooksModel
import com.li.libook.view.ICatBooksView

class CatBooksPresenter(private val mContext: Context, private val iCatBooksView: ICatBooksView){

    private var catBooksView:ICatBooksView = iCatBooksView
    private var catBooksModel:ICatBooksModel = CatBooksModel()

    fun initData(map:MutableMap<String,Any>){
        catBooksModel.initData(map) {
            catBooksView.initData(it)
        }
    }
    fun loadData(map:MutableMap<String,Any>){
        catBooksModel.loadData(map){
            catBooksView.loadData(it)
        }
    }
}