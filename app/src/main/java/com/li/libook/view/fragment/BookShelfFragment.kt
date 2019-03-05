package com.li.libook.view.fragment

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.li.libook.MainActivity
import com.li.libook.R
import com.li.libook.adapter.ShelfAdapter
import com.li.libook.model.MyConfig
import com.li.libook.model.bean.Book
import com.li.libook.model.entity.LocalBean
import com.li.libook.util.PopUtil
import com.li.libook.util.book.BookUtil
import com.li.libook.util.book.CommonUtil
import com.li.libook.view.activity.ReadActivity
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import java.util.*

class BookShelfFragment():Fragment() , View.OnClickListener{

    val HANDLER_MSG_READ = 3
    var list : MutableList<Book> = mutableListOf()
    var sheldAdapter:ShelfAdapter? = null
    var shelfPop:PopupWindow? = null
    var longItem = 0
    var pop:PopUtil? = null

    companion object {
        lateinit var mainAct:Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_bookshelf,container,false)
        list.clear()
        val pauseList = BookUtil.instance.loadAllBook()
        if (pauseList !=null && pauseList.size>0) {
            list.addAll(pauseList)
        }else {

//            DBUtil.instance.addAllShelfBean(list)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        Logger.i("BookShelfFragment初始化完毕")
    }

    override fun onResume() {
        super.onResume()
        Logger.i("可见")
        val pauseList = BookUtil.instance.loadAllBook()
        if (pauseList !=null && pauseList.isNotEmpty()) {
            list.clear()
            list.addAll(pauseList)
        }
        mHandler.sendEmptyMessage(2)
    }

    private fun initData() {
        book_shelf_swipe.setOnRefreshListener {
//            list.add(
//                ShelfBean("title"+list.size
//                    ,"author"+list.size
//                    ,"img"+list.size
//                    ,"url"+list.size
//                    ,"update"+list.size
//                    ,"lastTime"+list.size
//                )
//            )
            mHandler.postDelayed(swipRun,2000)
        }
        Logger.i("初始化")
        book_shelf_recycler.layoutManager = LinearLayoutManager(activity)
        sheldAdapter = ShelfAdapter(activity!!.applicationContext,list)
        book_shelf_recycler.adapter = sheldAdapter
    }

    private fun initView() {
        sheldAdapter!!.click {
            Logger.i("点击："+it+",path"+list[it].bookpath)
            val read:ReadActivity = ReadActivity()
            read.openBook(list[it].bookpath,activity!!)
        }
        sheldAdapter!!.longClick {
            Logger.i("长按："+it)
            longItem = it
            shelfPop = pop?.bottomPopupMenu()
        }

        val view = LayoutInflater.from(activity).inflate(R.layout.menu_pop_shelf,null,false)
        view.findViewById<TextView>(R.id.shelf_add).setOnClickListener(this)
        view.findViewById<TextView>(R.id.shelf_del).setOnClickListener(this)
        view.findViewById<TextView>(R.id.shelf_cancel).setOnClickListener(this)
        pop = PopUtil(mainAct,view,R.layout.activity_main)
    }

    val mHandler:Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1 -> {
                    if (book_shelf_swipe.isRefreshing){
                        book_shelf_swipe.isRefreshing = false
                    }
                    if (sheldAdapter != null)
                        sheldAdapter!!.notifyDataSetChanged()
                }
                2 -> {
                    if (sheldAdapter != null)
                        sheldAdapter!!.notifyDataSetChanged()
                }
                HANDLER_MSG_READ -> {

                }
            }
        }
    }

    private val swipRun:Runnable = Runnable {
        mHandler.sendEmptyMessage(1)
    }

    fun addLocal(localList: ArrayList<LocalBean>) {
        Logger.i("添加")
        Thread(){
            for (bean in localList) {
                var book = Book()
                book.bookpath = bean.path
                book.bookname = CommonUtil.instance.getFileName(bean.path)
                val now = Date(System.currentTimeMillis())
                book.update = "最近阅读：" + MyConfig.FORMAT.format(now)
                book.readsection = 0
                book.total = 100
                book.section = "还未解析"
                book.end = "本地书籍"
                list.add(book)
                BookUtil.instance.addBook(book)
            }
            mHandler.sendEmptyMessage(2)
        }.start()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.shelf_add -> {

            }
            R.id.shelf_del -> {
                BookUtil.instance.delBook(list[longItem])
                mHandler.sendEmptyMessage(2)
            }
            R.id.shelf_cancel -> {

            }
        }
        shelfPop?.dismiss()
    }
}