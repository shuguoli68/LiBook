package com.li.libook.presenter

import android.content.Context
import com.bumptech.glide.Glide.init
import com.li.libook.model.ISortModel
import com.li.libook.model.impl.SortModel
import com.li.libook.view.ISortView

class SortPresenter(private val mContext:Context, private val iSortView:ISortView){
    private var sortModel:ISortModel = SortModel(mContext)
    private var sortView:ISortView = iSortView

    fun initLeft(){
        sortView.leftV(sortModel.left())
    }

    fun initRight(){
        sortModel.right(){
            sortView.rightV(it)
        }
    }
}