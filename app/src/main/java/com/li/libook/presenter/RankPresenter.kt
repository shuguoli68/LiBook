package com.li.libook.presenter

import android.content.Context
import com.li.libook.model.IRankModel
import com.li.libook.model.impl.RankModel
import com.li.libook.view.IRankView

class RankPresenter(private val mContext: Context, private val iRankView: IRankView){
    private var rankModel: IRankModel = RankModel(mContext)
    private var rankView: IRankView = iRankView

    fun initLeft(){
        rankView.leftV(rankModel.left())
    }

    fun initRight(){
        rankModel.right(){
            rankView.rightV(it)
        }
    }
}