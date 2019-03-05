package com.li.libook.view.activity

import com.li.libook.R
import com.li.libook.model.bean.Book
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_read.*
import android.content.IntentFilter
import android.widget.Toast
import android.graphics.Point
import kotlinx.android.synthetic.main.common_toolbar.*
import java.io.IOException
import android.app.Activity
import android.view.animation.AnimationUtils
import com.li.libook.model.bean.BookMarkBean
import com.li.libook.view.fragment.BookShelfFragment
import android.os.Build
import android.graphics.Typeface
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import cn.xinlian.kotlinweb.util.ToastUtil
import com.li.libook.model.MyConfig
import com.li.libook.util.book.*
import com.orhanobut.logger.Logger
import java.sql.SQLException
import java.text.DecimalFormat


class ReadActivity():BaseAcivity() , View.OnClickListener{

    private val TAG = "ReadActivity"
    private val MESSAGE_CHANGEPROGRESS = 1

    private val lp: WindowManager.LayoutParams? = null
    private var bookList: Book? = null
    private var pageFactory: PageFactory? = null
    private var screenWidth: Int = 0
    private var screenHeight:Int = 0
    // popwindow是否显示
    private var isShow: Boolean? = false
    private lateinit var mSettingDialog: SettingDialog
    private lateinit var mPageModeDialog: PageModeDialog
    private var mDayOrNight: Boolean? = null

    override fun beforeView() {
        super.beforeView()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun getResourceId(): Int {
        return R.layout.activity_read
    }

    override fun initData() {
        //获取intent中的携带的信息
        val key:String = intent.getStringExtra(MyConfig.EXTRA_BOOK)
        val book:Book = BookUtil.instance.loadBook(key!!)
        bookList = book

        findViewById<TextView>(R.id.read_pre).setOnClickListener(this)
        findViewById<TextView>(R.id.read_next).setOnClickListener(this)
        findViewById<TextView>(R.id.read_directory).setOnClickListener(this)
        findViewById<TextView>(R.id.read_dayornight).setOnClickListener(this)
        findViewById<TextView>(R.id.read_pagemode).setOnClickListener(this)
        findViewById<TextView>(R.id.read_setting).setOnClickListener(this)

    }

    override fun initView() {
        initDa()
        initListener()
    }


    protected fun initListener() {
        read_progress!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            internal var pro: Float = 0.toFloat()

            // 触发操作，拖动
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                pro = (progress / 10000.0).toFloat()
                showProgress(pro)
            }

            // 表示进度条刚开始拖动，开始拖动时候触发的操作
            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            // 停止拖动时候
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                pageFactory!!.changeProgress(pro)
            }
        })

        mPageModeDialog.setOnCancelListener { hideSystemUI() }

        mPageModeDialog.setPageModeListener(object : PageModeDialog.PageModeListener {
            override fun changePageMode(pageMode: Int) {
                read_page!!.setPageMode(pageMode)
            }
        })

        mSettingDialog.setOnCancelListener { hideSystemUI() }

        mSettingDialog.setSettingListener(object : SettingDialog.SettingListener {
            override fun changeSystemBright(isSystem: Boolean?, brightness: Float) {
                if (!(isSystem!!)) {
                    BrightnessUtil.setBrightness(this@ReadActivity, brightness)
                } else {
                    val bh = BrightnessUtil.getScreenBrightness(this@ReadActivity)
                    BrightnessUtil.setBrightness(this@ReadActivity, bh)
                }
            }

            override fun changeFontSize(fontSize: Int) {
                pageFactory!!.changeFontSize(fontSize)
            }

            override fun changeTypeFace(typeface: Typeface) {
                pageFactory!!.changeTypeface(typeface)
            }

            override fun changeBookBg(type: Int) {
                pageFactory!!.changeBookBg(type)
            }
        })

        pageFactory!!.setPageEvent(object : PageFactory.PageEvent {
            override fun changeProgress(progress: Float) {
                val message = Message()
                message.what = MESSAGE_CHANGEPROGRESS
                message.obj = progress
                mHandler.sendMessage(message)
            }
        })

        read_page!!.setTouchListener(object : PageWidget.TouchListener {
            override fun center() {
                if (isShow!!) {
                    hideReadSetting()
                } else {
                    showReadSetting()
                }
            }

            override fun prePage(): Boolean? {
                if (isShow!!) {
                    return false
                }

                pageFactory!!.prePage()
                return !pageFactory!!.isfirstPage()

            }

            override fun nextPage(): Boolean? {
                //                Log.e("setTouchListener", "nextPage");
                if (isShow!!) {
                    return false
                }

                pageFactory!!.nextPage()
                return !pageFactory!!.islastPage()
            }

            override fun cancel() {
                pageFactory!!.cancelPage()
            }
        })
    }

    private fun initDa() {
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 19) {
            read_page!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        setSupportActionBar(common_toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(true)
        common_toolbar_title.text = bookList?.bookname
        common_toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })

        pageFactory = PageFactory.createPageFactory(this)

        val mfilter = IntentFilter()
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        mfilter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(myReceiver, mfilter)

        mSettingDialog = SettingDialog(this)
        mPageModeDialog = PageModeDialog(this)

        //获取屏幕宽高
        val manage = windowManager
        val display = manage.defaultDisplay
        val displaysize = Point()
        display.getSize(displaysize)
        screenWidth = displaysize.x
        screenHeight = displaysize.y
        //保持屏幕常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //隐藏
        hideSystemUI()
        //改变屏幕亮度
        if (!(MyConfig.instance.isSystemLight()!!)) {
            BrightnessUtil.setBrightness(this, MyConfig.instance.getLight())
        }

        read_page!!.setPageMode(MyConfig.instance.getPageMode())
        if (read_page == null) {
            Log.i("page", "null")
        }
        pageFactory!!.setPageWidget(read_page!!)

        try {
            pageFactory!!.openBook(bookList!!)
            //            read_progress.setProgress((int) bookList.getBegin());
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show()
        }

        initDayOrNight()
    }

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MESSAGE_CHANGEPROGRESS -> {
                    val progress = msg.obj as Float
                    setSeekBarProgress(progress)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (!(isShow!!)) {
            hideSystemUI()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateBack()
        pageFactory!!.clear()
        unregisterReceiver(myReceiver)
    }

    private fun updateBack() {
        bookList!!.total = pageFactory!!.directoryList.size.toLong()
        var section: String? = null
        if (pageFactory!!.directoryList.isEmpty()) {
            section = "最新章节：章节解析出错"
        } else {
            section = "最新章节：" +
                    pageFactory!!.directoryList[pageFactory!!.directoryList.size - 1].bookCatalogue.replace(" ", "")
            bookList!!.readsection = (CommonUtil.instance.getSubUtilSimple(pageFactory!!.nowDirectory).toLong())
        }
        bookList!!.setSection(section)
        val now = Date(System.currentTimeMillis())
        bookList!!.setUpdate("最近阅读：" + MyConfig.FORMAT.format(now))
        val message = Message()
        val shelf:BookShelfFragment = BookShelfFragment()
        message.what = shelf.HANDLER_MSG_READ
        message.obj = bookList
        shelf.mHandler.sendMessage(message)
        BookUtil.instance.updateBook(bookList!!)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow!!) {
                hideReadSetting()
                return true
            }
            if (mSettingDialog.isShowing) {
                mSettingDialog.hide()
                return true
            }
            if (mPageModeDialog.isShowing) {
                mPageModeDialog.hide()
                return true
            }
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.read_book_mark, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.add_book_mark -> if (pageFactory!!.currentPage != null) {
                val bookMark =
                    BookUtil.instance.loadBookMark(pageFactory!!.bookPath, pageFactory!!.currentPage!!.getBegin())
                if (bookMark != null) {
                    Toast.makeText(this@ReadActivity, "该书签已存在", Toast.LENGTH_SHORT).show()
                } else {
                    val bookMarks = BookMarkBean()
                    var word = ""
                    for (line in pageFactory!!.currentPage!!.getLines()!!) {
                        word += line
                    }
                    try {
                        val sf = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm ss"
                        )
                        val time = sf.format(Date())
                        bookMarks.time = time
                        val begin = pageFactory!!.currentPage!!.getBegin()
                        val fPercent = (begin * 1.0 / pageFactory!!.bookLen).toFloat()
                        val df = DecimalFormat("#0.0")
                        val strPercent = df.format(fPercent * 100) + "%"
                        bookMarks.progress = strPercent
                        bookMarks.text = word
                        bookMarks.bookpath = pageFactory!!.bookPath
                        bookMarks.begin = begin
                        bookMarks.id = BookUtil.instance.loadAllBookMark(pageFactory!!.bookPath).size.toLong()
                        BookUtil.instance.addBookMark(bookMarks)

                        Toast.makeText(this@ReadActivity, "书签添加成功", Toast.LENGTH_SHORT).show()
                    } catch (e: SQLException) {
                        Toast.makeText(this@ReadActivity, "该书签已存在", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@ReadActivity, "添加书签失败", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
        return true
    }

    fun openBook(key: String?, context: Activity): Boolean {
        if (TextUtils.isEmpty(key)) {
            Logger.i("bookList == null")
            throw NullPointerException("BookList can not be null")
        }
        val intent = Intent(context, ReadActivity::class.java)
        intent.putExtra(MyConfig.EXTRA_BOOK, key)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left)
        context.startActivity(intent)
        return true
    }

    /**
     * 隐藏菜单。沉浸式阅读
     */
    private fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                //                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    //显示书本进度
    fun showProgress(progress: Float) {
        if (read_progress_line!!.visibility != View.VISIBLE) {
            read_progress_line!!.visibility = View.VISIBLE
        }
        setProgress(progress)
    }

    //隐藏书本进度
    fun hideProgress() {
        read_progress_line!!.visibility = View.GONE
    }

    fun initDayOrNight() {
        mDayOrNight = MyConfig.instance.getDayOrNight()
        if (mDayOrNight!!) {
            read_dayornight!!.text = resources.getString(R.string.read_setting_day)
        } else {
            read_dayornight!!.text = resources.getString(R.string.read_setting_night)
        }
    }

    //改变显示模式
    fun changeDayOrNight() {
        if (mDayOrNight!!) {
            mDayOrNight = false
            read_dayornight!!.text = resources.getString(R.string.read_setting_night)
        } else {
            mDayOrNight = true
            read_dayornight!!.text = resources.getString(R.string.read_setting_day)
        }
        MyConfig.instance.setDayOrNight(mDayOrNight!!)
        pageFactory!!.setDayOrNight(mDayOrNight)
    }

    private fun setProgress(progress: Float) {
        val decimalFormat = DecimalFormat("00.00")//构造方法的字符格式这里如果小数不足2位,会以0补足.
        val p = decimalFormat.format(progress * 100.0)//format 返回的是字符串
        var now: String? = null
        var size = 0
        if (pageFactory!!.directoryList.size <= 0) {
            now = "第0章"
        } else {
            now = pageFactory!!.nowDirectory
            size = pageFactory!!.directoryList.size
        }
        read_progress_txt!!.text = (now + "\n"
                + CommonUtil.instance.getSubUtilSimple(now) + "/" + size + "章"
                + "(进度：" + p + "%)")
    }

    fun setSeekBarProgress(progress: Float) {
        read_progress!!.progress = (progress * 10000).toInt()
    }

    private fun showReadSetting() {
        isShow = true
        read_progress_line!!.visibility = View.GONE
        if (read_progress_line!!.visibility == View.INVISIBLE || read_progress_line!!.visibility == View.GONE) {
            read_progress_line!!.visibility = View.VISIBLE
        }
        //        if (isSpeaking) {
        //            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
        //            rl_read_bottom.startAnimation(topAnim);
        //            rl_read_bottom.setVisibility(View.VISIBLE);
        //        } else {
        showSystemUI()

        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter)
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter)
        read_bottom!!.startAnimation(topAnim)
        read_bar!!.startAnimation(topAnim)
        //        ll_top.startAnimation(topAnim);
        read_bottom!!.visibility = View.VISIBLE
        //        ll_top.setVisibility(View.VISIBLE);
        read_bar!!.visibility = View.VISIBLE
        //        }
    }

    private fun hideReadSetting() {
        isShow = false
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_exit)
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit)
        if (read_bottom!!.visibility == View.VISIBLE) {
            read_bottom!!.startAnimation(topAnim)
        }
        if (read_bar!!.visibility == View.VISIBLE) {
            read_bar!!.startAnimation(topAnim)
        }
        if (read_progress_line!!.visibility == View.VISIBLE) {
            read_progress_line!!.visibility = View.GONE
        }
        //        if (rl_read_bottom.getVisibility() == View.VISIBLE) {
        //            rl_read_bottom.startAnimation(topAnim);
        //        }
        //        ll_top.startAnimation(topAnim);
        read_bottom!!.visibility = View.GONE
        //        rl_read_bottom.setVisibility(View.GONE);
        //        ll_top.setVisibility(View.GONE);
        read_bar!!.visibility = View.GONE
        hideSystemUI()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.read_pre -> pageFactory!!.preChapter()
            R.id.read_next -> pageFactory!!.nextChapter()
            R.id.read_directory -> {
                ToastUtil.showShort(this,"read_directory")
//                val _intent = Intent(this@ReadActivity, MarkActivity::class.java)
//                _intent.putExtra(MyConfig.NOW_NAME, bookList!!.getBookpath())
//                startActivity(_intent)
            }
            R.id.read_dayornight -> changeDayOrNight()
            R.id.read_pagemode -> {
                hideReadSetting()
                if (mPageModeDialog!= null && !mPageModeDialog.isShowing)
                    mPageModeDialog.show()
            }
            R.id.read_setting -> {
                hideReadSetting()
                if (mSettingDialog != null && !mSettingDialog.isShowing)
                    mSettingDialog.show()
            }
        }
    }

    // 接收电池信息更新的广播
    private val myReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
                Log.e(TAG, Intent.ACTION_BATTERY_CHANGED)
                val level = intent.getIntExtra("level", 0)
                pageFactory!!.updateBattery(level)
            } else if (intent.action == Intent.ACTION_TIME_TICK) {
                Log.e(TAG, Intent.ACTION_TIME_TICK)
                pageFactory!!.updateTime()
            }
        }
    }
}