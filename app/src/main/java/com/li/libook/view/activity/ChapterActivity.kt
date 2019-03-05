package com.li.libook.view.activity

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.sharelink.kotlinweb.util.http.HttpUtil
import com.li.libook.R
import com.li.libook.adapter.ChapterAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.ResourceChapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_chapter.*
import kotlinx.android.synthetic.main.common_toolbar.*

class ChapterActivity() : BaseAcivity() {

    private lateinit var chapterAdapter: ChapterAdapter
    private lateinit var list: ResourceChapter
    private lateinit var resourceId:String
    private lateinit var resourceName:String

    private val httpUtil: HttpUtil by lazy {
        HttpUtil()
    }

    override fun getResourceId(): Int {
        return R.layout.activity_chapter
    }

    override fun initData() {
        resourceId = intent.getStringExtra(MyConfig.RESOURCE_ID)
        resourceName = intent.getStringExtra(MyConfig.RESOURCE_NAME)
        chapter_recycler.layoutManager = LinearLayoutManager(this)
        setSupportActionBar(common_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        common_toolbar_title.text = resourceName
        common_toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })
    }

    override fun initView() {
        Thread(){
            httpUtil.allChapter(resourceId)
                .subscribe({ it->
                    Logger.i("数据："+it.name)
                    list = it
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
                    chapterAdapter = ChapterAdapter(this@ChapterActivity,list)
                    chapter_count.text = "共"+list.chapters.size
                    chapter_recycler.adapter = chapterAdapter
                    chapterAdapter.click { position ->
                        Logger.i("点击：$position")
                        Thread{
                            httpUtil.chapContent(list.chapters[position].link).subscribe({
                                Logger.i(position.toString()+"成功："+it.chapter.cpContent)
                            }){
                                Logger.i("请求失败："+it.message)
                            }
                        }.start()
                    }
                }
            }
        }
    }
}