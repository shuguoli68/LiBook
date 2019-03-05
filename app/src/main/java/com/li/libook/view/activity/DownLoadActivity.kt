package com.li.libook.view.activity

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.li.libook.R
import com.li.libook.adapter.DownAdapter
import com.li.libook.model.bean.DownLoad
import com.li.libook.util.DBUtil
import com.li.libook.util.book.DownloadUtil
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.common_toolbar.*

class DownLoadActivity() : BaseAcivity() {

    private var list: MutableList<DownLoad> = mutableListOf()
    private lateinit var downAdapter:DownAdapter
    private var gress:Int = 0
    private var index:Int = 0
    private val downloadUtil = DownloadUtil()

    override fun getResourceId(): Int {
        return R.layout.activity_download
    }

    override fun initData() {
        setSupportActionBar(common_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        common_toolbar_title.text = getString(R.string.down_load)
        common_toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        download_recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun initView() {
        val pause: MutableList<DownLoad> = DBUtil.instance.loadAllDown()
        list.clear()
        list.addAll(pause)
        for (i in 0..5){
            list.add(DownLoad("id$i","path$i","名称$i",i.toString(),(100+i).toString(),1))
        }
        for (i in list){
            Logger.i(i.id+","+i.name+","+i.total+","+i.progress+","+i.type)
        }
        mHandler.sendEmptyMessage(1)
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1 -> {
                    downAdapter = DownAdapter(this@DownLoadActivity,list)
                    download_recycler.adapter = downAdapter
                    downAdapter.click { position, type ->
                        Logger.i("click:$position,$type")
                        index = position
                        if (1 == type){//开始下载
                            list[position].type = 2
                            downloadUtil.isDown(true)
                            downloadUtil.start(list[position], Down())
                        }else{//暂停
                            list[position].type = 1
                            downloadUtil.isDown(false)
                        }
                        downAdapter.notifyItemChanged(position)
                    }
                    downAdapter.longClick {
                        Logger.i("longClick:$it")
                    }
                }
                2 -> {
                    list[index].progress = gress.toString()
                    downAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    inner class Down () : DownloadUtil.DownCall{
        private val down = list[index]
        override fun init(total: String) {
            down.total = total
            mHandler.sendEmptyMessage(2)
        }

        override fun now(progress: Int) {
            gress = progress
            mHandler.sendEmptyMessage(2)
        }

    }
}