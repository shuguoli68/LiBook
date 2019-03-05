package com.li.libook.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.li.libook.R
import com.li.libook.adapter.RankRightAdapter
import com.li.libook.adapter.SortLeftAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.Rank
import com.li.libook.presenter.RankPresenter
import com.li.libook.view.IRankView
import com.li.libook.view.activity.CatBooksActivity
import com.li.libook.view.activity.RankActivity
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_rank.*

class RankFragment(): Fragment() , IRankView{

    private lateinit var rankPresenter: RankPresenter
    private var list: MutableList<String> = mutableListOf()
    private var rightData: Rank? = null
    private lateinit var leftAdapter: SortLeftAdapter
    private lateinit var rightAdapter: RankRightAdapter
    private var lastPos:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = layoutInflater.inflate(R.layout.fragment_rank,container,false)
        rankPresenter = RankPresenter(activity!!,this)
        rankPresenter.initLeft()
        rankPresenter.initRight()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rank_left.layoutManager = LinearLayoutManager(activity)
        rank_right.layoutManager = LinearLayoutManager(activity)
    }

    private fun iniLeft() {
        leftAdapter = SortLeftAdapter(activity!!,list)
        rank_left.adapter = leftAdapter
        leftAdapter.click {
            if (lastPos == it)
                return@click
            list[lastPos] = list[lastPos].split("_")[0]+"_"+false
            list[it] = list[it].split("_")[0]+"_"+true
            leftAdapter.notifyItemChanged(lastPos)
            leftAdapter.notifyItemChanged(it)
            lastPos = it
            initRight()
        }
    }

    private fun initRight() {
        Logger.i(activity.toString()+","+lastPos+","+rightData)
        rightAdapter = RankRightAdapter(activity!!,lastPos,rightData!!)
        rank_right.adapter = rightAdapter
        rightAdapter.click { leftIndex, position ->
            Logger.i("点击：$leftIndex,$position")
            val intent = Intent(activity, RankActivity::class.java)
            var gender = rightData!!.male[position].shortTitle
            var rankingId = rightData!!.male[position]._id
            if (leftIndex == 1){
                gender = rightData!!.female[position].shortTitle
                rankingId = rightData!!.female[position]._id
            }else if (leftIndex == 2){
                gender = rightData!!.picture[position].shortTitle
                rankingId = rightData!!.picture[position]._id
            }else if (leftIndex == 3){
                gender = rightData!!.epub[position].shortTitle
                rankingId = rightData!!.epub[position]._id
            }
            intent.putExtra(MyConfig.GENDER,gender)
            intent.putExtra(MyConfig.RANKINGID,rankingId)
            startActivity(intent)
        }
    }

    override fun leftV(LList: MutableList<String>) {
        list.clear()
        list.addAll(LList)
        mHandler.sendEmptyMessage(1)
    }

    override fun rightV(rank: Rank) {
        rightData = rank
        mHandler.sendEmptyMessage(2)
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1 -> {
                    iniLeft()
                }
                2 -> {
                    initRight()
                }
            }
        }
    }
}