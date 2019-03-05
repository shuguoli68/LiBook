package com.li.libook.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.sharelink.kotlinweb.util.http.HttpUtil
import com.li.libook.R
import com.li.libook.adapter.ResourceAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.BookResource
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_resource.*
import kotlinx.android.synthetic.main.common_toolbar.*

class ResourceActivity() : BaseAcivity() {

    private lateinit var resourceAdapter:ResourceAdapter
    private var list:MutableList<BookResource> = mutableListOf()
    private lateinit var bookid:String
    private var map:MutableMap<String,String> = mutableMapOf()
    private var resourceId = ""
    private var name = ""
    private val httpUtil: HttpUtil by lazy {
        HttpUtil()
    }

    override fun getResourceId(): Int {
        return R.layout.activity_resource
    }

    override fun initData() {
        bookid = intent.getStringExtra(MyConfig.BOOK_ID)
        setSupportActionBar(common_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        common_toolbar_title.text = getString(R.string.book_res)
        common_toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        resource_recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun initView() {
        map["view"] = "summary"
        map["book"] = bookid
        Thread(){
            httpUtil.bookResource(map).subscribe ({
                list = it
                mHandler.sendEmptyMessage(1)
            })
            {
                Logger.i("请求失败："+it.message)
            }

        }.start()
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1 -> {
                    resource_count.text = String.format(getString(R.string.resource_count),list.size)
                    resourceAdapter = ResourceAdapter(list)
                    resource_recycler.adapter = resourceAdapter
                    resourceAdapter.click {
                        resourceId = list[it]._id
                        name = list[it].name
                        val intent = Intent()
                        intent.putExtra(MyConfig.RESOURCE_ID,resourceId)
                        intent.putExtra(MyConfig.RESOURCE_NAME,name)
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }
                }
            }
        }
    }
}