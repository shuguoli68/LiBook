package com.li.libook.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.li.libook.R
import com.li.libook.adapter.LocalAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.entity.LocalBean
import com.li.libook.util.LocalFileTool
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_local.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.ArrayList

class LocalActivity() : BaseAcivity(),View.OnClickListener{

    var localList:ArrayList<LocalBean> = arrayListOf()
    lateinit var localAdapter:LocalAdapter
    var count:Int = 0
    var isAll:Boolean = false
    var pauseList:ArrayList<LocalBean> = arrayListOf()

    override fun getResourceId(): Int {
        return R.layout.activity_local
    }

    override fun initData() {
        local_recycler.layoutManager = LinearLayoutManager(this)
        Thread(){
            LocalFileTool.instance.readFile(LocalFileTool.txtType,this,rCall = {
                for (str in it){
                    val name:String? = LocalFileTool.getFileName(str)
                    localList.add(LocalBean(name,str,false,false))
                }
                mHandler.sendEmptyMessage(1)
            })
        }.start()
        okNo()
    }

    override fun initView() {
        setSupportActionBar(common_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        common_toolbar_title.text = getString(R.string.local)
        common_toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1 -> {
                    localAdapter = LocalAdapter(localList)
                    local_recycler.adapter = localAdapter
                    localAdapter.click {
                        Logger.i("点击："+it)
                        localList[it].isSelect = !localList[it].isSelect
                        localAdapter.notifyItemChanged(it)
                        if (localList[it].isSelect) {
                            count++
                            pauseList.add(localList[it])
                        }
                        else {
                            count--
                            pauseList.remove(localList[it])
                        }
                        sendEmptyMessage(2)
                    }
                    local_count.text = String.format(getString(R.string.local_count),count,localList.size)
                }
                2 -> {
                    local_count.text = String.format(getString(R.string.local_count),count,localList.size)
                    okNo()
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.local_all -> {
                if (isAll){
                    isAll = false
                    count = 0
                    local_all.text = getString(R.string.local_all)
                    all(false)
                    pauseList.addAll(localList)
                }else{
                    isAll = true
                    count = localList.size
                    local_all.text = getString(R.string.local_all_cancel)
                    all(true)
                    pauseList.clear()
                }
                mHandler.sendEmptyMessage(2)
                localAdapter.notifyDataSetChanged()
            }
            R.id.local_ok -> {
                val intent = Intent()
                intent.putParcelableArrayListExtra(MyConfig.LOCAL_LIST,pauseList)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
    }

    private fun okNo(){
        if (count<=0){
            local_ok.isClickable = false
            local_ok.alpha = 0.6f
        }else{
            local_ok.isClickable = true
            local_ok.alpha = 1.0f
        }
    }

    private fun all(b:Boolean){
        for (bean in localList){
            bean.isSelect = b
        }
    }
}