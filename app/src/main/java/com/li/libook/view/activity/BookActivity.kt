package com.li.libook.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import cn.sharelink.kotlinweb.util.http.HttpUtil
import cn.xinlian.kotlinweb.util.ToastUtil
import com.bumptech.glide.Glide
import com.li.libook.R
import com.li.libook.model.MyConfig
import com.li.libook.model.bean.Book
import com.li.libook.model.bean.DownLoad
import com.li.libook.model.entity.CatBook
import com.li.libook.util.DBUtil
import com.li.libook.util.book.BookUtil
import com.li.libook.util.book.CommonUtil
import com.li.libook.util.http.ApiServer
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.item_catbooks.*
import java.io.File
import java.util.*

class BookActivity():BaseAcivity() , View.OnClickListener{

    private lateinit var book:CatBook
    private var resourceId = "56f8da09176d03ac1983f6cd"
    private var resourceName = "优质书源"

    override fun getResourceId(): Int {
        return R.layout.activity_book
    }

    override fun initData() {
        book = intent.getParcelableExtra(MyConfig.BOOK_CAT)
        setSupportActionBar(common_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        common_toolbar_title.text = book.title
        common_toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        Glide.with(this).load(ApiServer.BASE_URL_IMG+book.res).apply(MyConfig.options).into(item_catbooks_img)
        item_catbooks_title.text = book.title
        item_catbooks_author.text = book.author
        item_catbooks_retentionRatio.text = "留存率："+book.retentionRatio+"%"
        item_catbooks_lastChapter.text = book.lastChapter
        book_shortIntro.text = book.shortIntro
        book_directory.text = getString(R.string.read_setting_directory)+"    "+book.lastChapter
        book_resource.text = String.format(getString(R.string.book_resource),resourceName)
    }

    override fun initView() {
        book_shortIntro.setOnClickListener(this)
        book_directory.setOnClickListener(this)
        book_resource.setOnClickListener(this)
        book_add.setOnClickListener(this)
        book_read.setOnClickListener(this)
        book_down.setOnClickListener(this)

        var map:MutableMap<String,String> = mutableMapOf()
        map["view"] = "summary"
        map["book"] = book.id
        Thread(){
            val httpUtil: HttpUtil = HttpUtil()
            httpUtil.bookResource(map).subscribe ({
                resourceId = it[0]._id
            })
            {
                Logger.i("请求失败："+it.message)
            }

        }.start()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
           R.id.book_shortIntro -> {
               if (book_shortIntro.maxLines == 3)
                   book_shortIntro.maxLines = 8
               else
                   book_shortIntro.maxLines = 3
           }
            R.id.book_directory -> {
                val intent:Intent = Intent()
                intent.putExtra(MyConfig.RESOURCE_ID,resourceId)
                intent.putExtra(MyConfig.RESOURCE_NAME,book.title)
                startTo(intent,ChapterActivity::class.java)
            }
            R.id.book_resource -> {
                val intent:Intent = Intent(this,ResourceActivity::class.java)
                intent.putExtra(MyConfig.BOOK_ID,book.id)
                startActivityForResult(intent,1)
            }
            R.id.book_add -> {
                var cacheBook = Book()
                val path = book.author+"_"+book.title+".txt"
                val name = book.title
                val file = File(MyConfig.ROOT_ONLINE_PATH,path)
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                if (!file.exists()){//创建文件
                    file.createNewFile()
                }
                cacheBook.bookpath = MyConfig.ROOT_ONLINE_PATH+File.separator+path
                cacheBook.bookname = name
                val now = Date(System.currentTimeMillis())
                cacheBook.update = "最近阅读：" + MyConfig.FORMAT.format(now)
                cacheBook.readsection = 0
                cacheBook.total = 100
                cacheBook.section = "还未解析"
                cacheBook.end = "本地书籍"
                BookUtil.instance.addBook(cacheBook)
                ToastUtil.showShort(this,getString(R.string.add_success))
                var down = DownLoad(book.id,MyConfig.ROOT_ONLINE_PATH+File.separator+path,path,"0","100",0)
                DBUtil.instance.addDown(down)
            }
            R.id.book_read -> {

            }
            R.id.book_down -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            resourceId = data!!.getStringExtra(MyConfig.RESOURCE_ID)
            resourceName = data!!.getStringExtra(MyConfig.RESOURCE_NAME)
            book_resource.text = String.format(getString(R.string.book_resource),resourceName)
        }
    }
}