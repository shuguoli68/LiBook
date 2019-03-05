package com.li.libook.util

import com.li.libook.App
import com.li.libook.model.bean.DownLoad
import com.li.libook.model.bean.ShelfBean
import com.li.libook.model.gen.DownLoadDao
import com.li.libook.model.gen.ShelfBeanDao

class DBUtil(){

    private object DB{
        val INSTANCE = DBUtil()
    }

    companion object {
        val instance:DBUtil by lazy { DB.INSTANCE }
    }

    private val shelfBeanDao: ShelfBeanDao = App.instance.session().shelfBeanDao
    private val downLoadDao:DownLoadDao = App.instance.session().downLoadDao

    fun addShelfBean(bean:ShelfBean){
        shelfBeanDao.insertOrReplace(bean)
    }
    fun addAllShelfBean(mutableList: MutableList<ShelfBean>){
        shelfBeanDao.insertOrReplaceInTx(mutableList)
    }
    fun loadAllShelfBean(): MutableList<ShelfBean>{
        return shelfBeanDao.loadAll()
    }

    fun addDown(bean:DownLoad){
        downLoadDao.insertOrReplace(bean)
    }
    fun loadAllDown() : MutableList<DownLoad>{
        return downLoadDao.loadAll()
    }
    fun delDown(bean:DownLoad){
        downLoadDao.delete(bean)
    }
}