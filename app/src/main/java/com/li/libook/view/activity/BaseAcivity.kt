package com.li.libook.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.li.libook.App

abstract class BaseAcivity() : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeView()
        setContentView(getResourceId())
        App.instance.addActivity(this)
        initData()
        initView()
    }

    abstract fun getResourceId():Int
    abstract fun initData()
    abstract fun initView()

    /**
     * setContentView之前执行
     */
    open fun beforeView() {

    }

    fun <T> startTo(clazz: Class<T>){
        val intent:Intent = Intent(this,clazz)
        startActivity(intent)
    }
    fun <T> startTo(intent:Intent,clazz: Class<T>){
        intent.setClass(this,clazz)
        startActivity(intent)
    }

    fun viewClick(listener: View.OnClickListener, vararg views: View) {
        for (it in views) {
            it.setOnClickListener(listener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        App.instance.delActivity(this)
    }

    fun exitApp(){
        App.instance.delAllActivity()
    }
}