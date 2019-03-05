package com.li.libook.view.activity

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import cn.sharelink.kotlinweb.util.http.HttpUtil
import com.li.libook.R
import com.li.libook.adapter.RankAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.CatBook
import com.li.libook.model.entity.OneRank
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.android.synthetic.main.common_toolbar.*

class RankActivity() : BaseAcivity(){

    private lateinit var rankingId:String
    private lateinit var gender:String
    private lateinit var oneRank: OneRank
    private lateinit var rankAdapter: RankAdapter

    private val httpUtil: HttpUtil by lazy {
        HttpUtil()
    }

    override fun getResourceId(): Int {
        return R.layout.activity_rank
    }

    override fun initData() {
        setSupportActionBar(common_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        common_toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        gender = intent.getStringExtra(MyConfig.GENDER)
        common_toolbar_title.text = gender
        rankingId = intent.getStringExtra(MyConfig.RANKINGID)
        rank_recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun initView() {
        data()
        rank_refresh.setOnRefreshListener {
            data()
        }
    }

    private fun data(){
        Thread(){
            httpUtil.oneRanking(rankingId)
                .subscribe({
                    oneRank = it
                    Logger.i("数据：")
                    mHandler.sendEmptyMessage(1)
                })
                { throwable -> Logger.i("请求失败："+throwable.message) }
        }.start()
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1 -> {
                    rankAdapter = RankAdapter(this@RankActivity,oneRank)
                    rank_recycler.adapter = rankAdapter
                    rankAdapter.click {
                        val bo = oneRank.ranking.books[it]
                        var last = if (TextUtils.isEmpty(bo.lastChapter)) bo.shortIntro else bo.lastChapter
                        val book: CatBook = CatBook(bo._id,bo.cover,bo.title,bo.author,bo.retentionRatio,last,bo.shortIntro,bo.latelyFollower,bo.minorCate)
                        val intent = Intent(this@RankActivity,BookActivity::class.java)
                        intent.putExtra(MyConfig.BOOK_CAT,book)
                        startActivity(intent)
                    }
                    rank_refresh.finishRefresh()
                }

            }
        }
    }
}