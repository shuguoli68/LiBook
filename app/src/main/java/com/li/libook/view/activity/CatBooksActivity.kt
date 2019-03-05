package com.li.libook.view.activity

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.li.libook.R
import com.li.libook.adapter.CatBookAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.BooksByCat
import com.li.libook.model.entity.CatBook
import com.li.libook.presenter.CatBooksPresenter
import com.li.libook.view.ICatBooksView
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import kotlinx.android.synthetic.main.activity_catbooks.*
import kotlinx.android.synthetic.main.common_toolbar.*

class CatBooksActivity() : BaseAcivity() , ICatBooksView{

    private lateinit var catBooksPresenter: CatBooksPresenter
    var map:MutableMap<String,Any> = mutableMapOf()
    var list:MutableList<CatBook> = mutableListOf()
    private lateinit var bookAdapter:CatBookAdapter
    private lateinit var gender:String
    private lateinit var major:String
    private var start = 0

    override fun getResourceId(): Int {
        return R.layout.activity_catbooks
    }

    override fun initData() {
        setSupportActionBar(common_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        common_toolbar_title.text = getString(R.string.all)
        common_toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        catbooks_recycler.layoutManager = LinearLayoutManager(this)


        catbooks_type.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.catbooks_hot -> map["type"] = "hot"
                R.id.catbooks_over -> map["type"] = "over"
                R.id.catbooks_new -> map["type"] = "new"
            }
            catBooksPresenter.initData(map)
        }

        gender = intent.getStringExtra(MyConfig.GENDER)
        major = intent.getStringExtra(MyConfig.MAJOR)

        catBooksPresenter = CatBooksPresenter(this,this)
        map["gender"] = gender
        map["type"] = "hot"
        map["major"] = major
        map["minor"] = ""
        map["start"] = 0
        map["limit"] = 20
        catBooksPresenter.initData(map)
        start += 20
    }

    override fun initView() {
        catbooks_refresh.setOnRefreshListener {
            mHandler.sendEmptyMessage(2)
        }
        catbooks_refresh.setOnLoadMoreListener {
            mHandler.sendEmptyMessage(3)
        }
    }

    override fun initData(books1: BooksByCat) {
        Logger.i(books1.books.size.toString()+","+books1.ok)
        catbooks_refresh.finishRefresh()
        list.clear()
        for (i in books1.books){
            val tag = i.tags.toString()
            list.add(CatBook(i._id,i.cover,i.title,i.author,i.retentionRatio,i.lastChapter,i.shortIntro,i.latelyFollower,tag))
        }
        mHandler.sendEmptyMessage(1)
    }

    override fun loadData(books2: BooksByCat) {
        catbooks_refresh.finishLoadMore()
        for (i in books2.books){
            val tag = i.tags.toString()
            list.add(CatBook(i._id,i.cover,i.title,i.author,i.retentionRatio,i.lastChapter,i.shortIntro,i.latelyFollower,tag))
        }
        bookAdapter.notifyDataSetChanged()
        start += 20
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1 -> {
                    bookAdapter = CatBookAdapter(this@CatBooksActivity,list)
                    catbooks_recycler.adapter = bookAdapter
                    bookAdapter.click {
                        Logger.i("点击")
                        val book = list[it]
                        val intent = Intent(this@CatBooksActivity,BookActivity::class.java)
                        intent.putExtra(MyConfig.BOOK_CAT,book)
                        startActivity(intent)
                    }
                }
                2 -> {//刷新
                    catBooksPresenter.initData(map)
                }
                3 -> {//加载
                    map["start"] = start
                    catBooksPresenter.loadData(map)
                }
            }
        }
    }
}