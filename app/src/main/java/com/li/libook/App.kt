package com.li.libook

import android.app.Activity
import android.app.Application
import android.content.Context
import com.li.libook.cockroach.CockroachUtil
import com.li.libook.model.gen.DaoMaster
import com.li.libook.model.gen.DaoSession
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


class App(): Application(){

    private var activitys = mutableListOf<Activity>()

    companion object {
        lateinit var instance:App
        private set
    }

    private lateinit var daoSession: DaoSession

    override fun onCreate() {
        super.onCreate()
        instance = this
        initApp()
    }

    private fun initApp() {
        Logger.addLogAdapter(AndroidLogAdapter())
        initDB()
        CockroachUtil.install(this)
    }

    private fun initDB(){
        //数据库
        val helper = DaoMaster.DevOpenHelper(this, "libook-db", null)
        val db = helper.getWritableDatabase()
        daoSession = DaoMaster(db).newSession()
    }

    fun session(): DaoSession{
        return daoSession
    }

    fun addActivity(mContext:Activity){
        if (!activitys.contains(mContext))
            activitys.add(mContext)
    }
    fun delActivity(mContext:Activity){
        if (activitys.contains(mContext)) {
            activitys.remove(mContext)
        }
    }
    fun delAllActivity(){
        for (ac in activitys){
            ac.finish()
        }
    }
}