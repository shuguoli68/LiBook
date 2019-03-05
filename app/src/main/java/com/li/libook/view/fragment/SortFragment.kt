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
import com.li.libook.adapter.SortLeftAdapter
import com.li.libook.adapter.SortRightAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.ZhuiStatis
import com.li.libook.presenter.SortPresenter
import com.li.libook.view.ISortView
import com.li.libook.view.activity.CatBooksActivity
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_sort.*

class SortFragment(): Fragment() , ISortView{

    private lateinit var sortPresenter:SortPresenter
    private var list: MutableList<String> = mutableListOf()
    private var rightData: ZhuiStatis? = null
    private lateinit var leftAdapter: SortLeftAdapter
    private lateinit var rightAdapter: SortRightAdapter
    private var lastPos:Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = layoutInflater.inflate(R.layout.fragment_sort,container,false)
        sortPresenter = SortPresenter(activity!!,this)
        sortPresenter.initLeft()
        sortPresenter.initRight()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sort_left.layoutManager = LinearLayoutManager(activity)
        sort_right.layoutManager = LinearLayoutManager(activity)
    }

    private fun iniLeft() {
        leftAdapter = SortLeftAdapter(activity!!,list)
        sort_left.adapter = leftAdapter
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
        rightAdapter = SortRightAdapter(activity!!,lastPos,rightData!!)
        sort_right.adapter = rightAdapter
        rightAdapter.click { leftIndex, position ->
            Logger.i("点击：$leftIndex,$position")
            val intent = Intent(activity,CatBooksActivity::class.java)
            var gender = "male"
            var major = rightData!!.male[position].name
            if (leftIndex == 1){
                gender = "female"
                major = rightData!!.female[position].name
            }else if (leftIndex == 2){
                gender = "picture"
                major = rightData!!.picture[position].name
            }else if (leftIndex == 3){
                gender = "press"
                major = rightData!!.press[position].name
            }
            intent.putExtra(MyConfig.GENDER,gender)
            intent.putExtra(MyConfig.MAJOR,major)
            startActivity(intent)
        }
    }

    override fun leftV(LList: MutableList<String>) {
        list.clear()
        list.addAll(LList)
        mHandler.sendEmptyMessage(1)
    }

    override fun rightV(statis: ZhuiStatis) {
        rightData = statis
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