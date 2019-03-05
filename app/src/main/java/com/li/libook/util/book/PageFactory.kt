package com.li.libook.util.book

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.Typeface
import android.os.AsyncTask
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.li.libook.R
import com.li.libook.model.MyConfig
import com.li.libook.model.bean.Book
import com.li.libook.model.bean.BookCatalogue
import com.li.libook.util.BitmapUtil


import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList

/**
 * Created by Administrator on 2016/7/20 0020.
 */
class PageFactory private constructor(context: Context) {

    private val mContext: Context
    //当前的书本
    //    private File book_file = null;
    // 默认背景颜色
    private val m_backColor = -0x617b
    //页面宽
    private val mWidth: Int
    //页面高
    private val mHeight: Int
    //文字字体大小
    //获取文字大小
    var fontSize: Float = 0.toFloat()
        private set
    //时间格式
    private val sdf: SimpleDateFormat
    //时间
    private var date: String? = null
    //进度格式
    private val df: DecimalFormat
    //电池边界宽度
    private val mBorderWidth: Float
    // 上下与边缘的距离
    private val marginHeight: Float
    // 左右与边缘的距离
    private var measureMarginWidth: Float = 0.toFloat()
    // 左右与边缘的距离
    private val marginWidth: Float
    //状态栏距离底部高度
    private val statusMarginBottom: Float
    //行间距
    private val lineSpace: Float
    //段间距
    private val paragraphSpace: Float
    //字高度
    private val fontHeight: Float = 0.toFloat()
    //字体
    private var typeface: Typeface? = null
    //文字画笔
    private val mPaint: Paint
    //加载画笔
    private val waitPaint: Paint
    //文字颜色
    //获取文字颜色
    var textColor = Color.rgb(50, 65, 78)
        private set
    // 绘制内容的宽
    private val mVisibleHeight: Float
    // 绘制内容的宽
    private val mVisibleWidth: Float
    // 每页可以显示的行数
    private var mLineCount: Int = 0
    //电池画笔
    private val mBatterryPaint: Paint
    //电池字体大小
    private val mBatterryFontSize: Float
    //背景图片
    //设置页面背景
    //设置页面背景
    var bgBitmap: Bitmap? = null
    //当前显示的文字
    //    private StringBuilder word = new StringBuilder();
    //当前总共的行
    //    private Vector<String> m_lines = new Vector<>();
    //    // 当前页起始位置
    //    private long m_mbBufBegin = 0;
    //    // 当前页终点位置
    //    private long m_mbBufEnd = 0;
    //    // 之前页起始位置
    //    private long m_preBegin = 0;
    //    // 之前页终点位置
    //    private long m_preEnd = 0;
    // 图书总长度
    //    private long m_mbBufLen = 0;
    private val batteryInfoIntent: Intent?
    //电池电量百分比
    private var mBatteryPercentage: Float = 0.toFloat()
    //电池外边框
    private val rect1 = RectF()
    //电池内边框
    private val rect2 = RectF()
    //文件编码
    //    private String m_strCharsetName = "GBK";
    //当前是否为第一页
    private var m_isfirstPage: Boolean = false
    //当前是否为最后一页
    private var m_islastPage: Boolean = false
    //书本widget
    private var mBookPageWidget: PageWidget? = null
    //    //书本所有段
    //    List<String> allParagraph;
    //    //书本所有行
    //    List<String> allLines = new ArrayList<>();
    //现在的进度
    private var currentProgress: Float = 0.toFloat()
    //目录
    //    private List<BookCatalogue> directoryList = new ArrayList<>();
    //书本路径
    var bookPath = ""
        private set
    //书本名字
    private var bookName = ""
    private var bookList: Book? = null
    //书本章节
    private var currentCharter = 0
    //当前电量
    private var level = 0
    private val mBookUtil: BookUtil
    private var mPageEvent: PageEvent? = null
    var currentPage: TRPage? = null
        private set
    private var prePage: TRPage? = null
    private var cancelPage: TRPage? = null
    private var bookTask: BookTask? = null
    internal var values = ContentValues()

    //        Log.e("begin",currentPage.getEnd() + 1 + "");
    //        Log.e("end",mBookUtil.getPosition() + "");
    val nextPage: TRPage
        get() {
            mBookUtil.setPostition(currentPage!!.getEnd())

            val trPage = TRPage()
            trPage.setBegin(currentPage!!.getEnd() + 1)
            trPage.setLines(nextLines)
            trPage.setEnd(mBookUtil.position)
            return trPage
        }

    //判断是否换行
    //                    height +=  paragraphSpace;
    //            Log.e(TAG,str + "   ");
    val nextLines: List<String>
        get() {
            val lines = ArrayList<String>()
            var width = 0f
            val height = 0f
            var line = ""
            while (mBookUtil.next(true) != -1) {
                val word = mBookUtil.next(false).toChar()
                if (word + "" == "\r" && mBookUtil.next(true).toChar() + "" == "\n") {
                    mBookUtil.next(false)
                    if (!line.isEmpty()) {
                        lines.add(line)
                        line = ""
                        width = 0f
                        if (lines.size == mLineCount) {
                            break
                        }
                    }
                } else {
                    val widthChar = mPaint.measureText(word + "")
                    width += widthChar
                    if (width > mVisibleWidth) {
                        width = widthChar
                        lines.add(line)
                        line = word + ""
                    } else {
                        line += word
                    }
                }

                if (lines.size == mLineCount) {
                    if (!line.isEmpty()) {
                        mBookUtil.setPostition(mBookUtil.position - 1)
                    }
                    break
                }
            }

            if (!line.isEmpty() && lines.size < mLineCount) {
                lines.add(line)
            }
            for (str in lines) {
            }
            return lines
        }

    //            Log.e(TAG,lines.get(i) + "   ");
    val preLines: List<String>
        get() {
            val lines = ArrayList<String>()
            var width = 0f
            var line = ""

            var par = mBookUtil.preLine()
            while (par != null) {
                val preLines = ArrayList<String>()
                for (i in par.indices) {
                    val word = par[i]
                    val widthChar = mPaint.measureText(word + "")
                    width += widthChar
                    if (width > mVisibleWidth) {
                        width = widthChar
                        preLines.add(line)
                        line = word + ""
                    } else {
                        line += word
                    }
                }
                if (!line.isEmpty()) {
                    preLines.add(line)
                }

                lines.addAll(0, preLines)

                if (lines.size >= mLineCount) {
                    break
                }
                width = 0f
                line = ""
                par = mBookUtil.preLine()
            }

            val reLines = ArrayList<String>()
            var num = 0
            for (i in lines.indices.reversed()) {
                if (reLines.size < mLineCount) {
                    reLines.add(0, lines[i])
                } else {
                    num = num + lines[i].length
                }
            }

            if (num > 0) {
                if (mBookUtil.position > 0) {
                    mBookUtil.setPostition(mBookUtil.position + num.toLong() + 2)
                } else {
                    mBookUtil.setPostition(mBookUtil.position + num)
                }
            }

            return reLines
        }

    val nowDirectory: String
        get() = directoryList[currentCharter].bookCatalogue

    val bookLen: Long
        get() = mBookUtil.bookLen

    //获取书本的章
    val directoryList: List<BookCatalogue>
        get() = mBookUtil.getDirectoryList()

    enum class Status {
        OPENING,
        FINISH,
        FAIL
    }

    init {
        mBookUtil = BookUtil()
        mContext = context.applicationContext
        //获取屏幕宽高
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metric = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metric)
        mWidth = metric.widthPixels
        mHeight = metric.heightPixels

        sdf = SimpleDateFormat("HH:mm")//HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(java.util.Date())
        df = DecimalFormat("#0.0")

        marginWidth = mContext.resources.getDimension(R.dimen.readingMarginWidth)
        marginHeight = mContext.resources.getDimension(R.dimen.readingMarginHeight)
        statusMarginBottom = mContext.resources.getDimension(R.dimen.reading_status_margin_bottom)
        lineSpace = context.resources.getDimension(R.dimen.reading_line_spacing)
        paragraphSpace = context.resources.getDimension(R.dimen.reading_paragraph_spacing)
        mVisibleWidth = mWidth - marginWidth * 2
        mVisibleHeight = mHeight - marginHeight * 2

        typeface = MyConfig.instance.getTypeface()
        fontSize = MyConfig.instance.getFontSize()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)// 画笔
        mPaint.textAlign = Paint.Align.LEFT// 左对齐
        mPaint.textSize = fontSize// 字体大小
        mPaint.color = textColor// 字体颜色
        mPaint.typeface = typeface
        mPaint.isSubpixelText = true// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        waitPaint = Paint(Paint.ANTI_ALIAS_FLAG)// 画笔
        waitPaint.textAlign = Paint.Align.LEFT// 左对齐
        waitPaint.textSize = mContext.resources.getDimension(R.dimen.reading_max_text_size)// 字体大小
        waitPaint.color = textColor// 字体颜色
        waitPaint.typeface = typeface
        waitPaint.isSubpixelText = true// 设置该项为true，将有助于文本在LCD屏幕上的显示效果
        calculateLineCount()

        mBorderWidth = mContext.resources.getDimension(R.dimen.reading_board_battery_border_width)
        mBatterryPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBatterryFontSize = CommonUtil.instance.sp2px(context, 12f).toFloat()
        mBatterryPaint.textSize = mBatterryFontSize
        mBatterryPaint.typeface = typeface
        mBatterryPaint.textAlign = Paint.Align.LEFT
        mBatterryPaint.color = textColor
        batteryInfoIntent = context.applicationContext.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )//注册广播,随时获取到电池电量信息

        initBg(MyConfig.instance.getDayOrNight())
        measureMarginWidth()
    }

    private fun measureMarginWidth() {
        val wordWidth = mPaint.measureText("\u3000")
        val width = mVisibleWidth % wordWidth
        measureMarginWidth = marginWidth + width / 2

        //        Rect rect = new Rect();
        //        mPaint.getTextBounds("好", 0, 1, rect);
        //        float wordHeight = rect.height();
        //        float wordW = rect.width();
        //        Paint.FontMetrics fm = mPaint.getFontMetrics();
        //        float wrodH = (float) (Math.ceil(fm.top + fm.bottom + fm.leading));
        //        String a = "";

    }

    //初始化背景
    private fun initBg(isNight: Boolean) {
        if (isNight) {
            //设置背景
            //            setBgBitmap(BitmapUtil.decodeSampledBitmapFromResource(
            //                    mContext.getResources(), R.drawable.main_bg, mWidth, mHeight));
            val bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.BLACK)
            bgBitmap = bitmap
            //设置字体颜色
            setM_textColor(Color.rgb(128, 128, 128))
            setBookPageBg(Color.BLACK)
        } else {
            //设置背景
            setBookBg(MyConfig.instance.getBookBgType())
        }
    }

    private fun calculateLineCount() {
        mLineCount = (mVisibleHeight / (fontSize + lineSpace)).toInt()// 可显示的行数
    }

    private fun drawStatus(bitmap: Bitmap?) {
        var statu = ""
        when (status) {
            PageFactory.Status.OPENING -> statu = "正在打开书本..."
            PageFactory.Status.FAIL -> statu = "打开书本失败！"
        }

        val c = Canvas(bitmap!!)
        c.drawBitmap(bgBitmap!!, 0f, 0f, null)
        waitPaint.color = textColor
        waitPaint.textAlign = Paint.Align.CENTER

        val targetRect = Rect(0, 0, mWidth, mHeight)
        //        c.drawRect(targetRect, waitPaint);
        val fontMetrics = waitPaint.fontMetricsInt
        // 转载请注明出处：http://blog.csdn.net/hursing
        val baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        waitPaint.textAlign = Paint.Align.CENTER
        c.drawText(statu, targetRect.centerX().toFloat(), baseline.toFloat(), waitPaint)
        //        c.drawText("正在打开书本...", mHeight / 2, 0, waitPaint);
        mBookPageWidget!!.postInvalidate()
    }

    fun onDraw(bitmap: Bitmap?, m_lines: List<String>?, updateCharter: Boolean?) {
        if (directoryList.size > 0 && updateCharter!!) {
            currentCharter = getCurrentCharter()
        }
        //更新数据库进度
        if (currentPage != null && bookList != null) {
            object : Thread() {
                override fun run() {
                    super.run()
                    values.put("begin", currentPage!!.getBegin())
                    bookList!!.setBegin(currentPage!!.getBegin())
                    mBookUtil.updateBook(bookList!!)
                }
            }.start()
        }

        val c = Canvas(bitmap!!)
        c.drawBitmap(bgBitmap!!, 0f, 0f, null)
        //        word.setLength(0);
        mPaint.textSize = fontSize
        mPaint.color = textColor
        mBatterryPaint.color = textColor
        if (m_lines!!.size == 0) {
            return
        }

        if (m_lines.size > 0) {
            var y = marginHeight
            for (strLine in m_lines) {
                y += fontSize + lineSpace
                c.drawText(strLine, measureMarginWidth, y, mPaint)
                //                word.append(strLine);
            }
        }

        //画进度及时间
        val dateWith = (mBatterryPaint.measureText(date) + mBorderWidth).toInt()//时间宽度
        val fPercent = (currentPage!!.getBegin() * 1.0 / mBookUtil.bookLen).toFloat()//进度
        currentProgress = fPercent
        if (mPageEvent != null) {
            mPageEvent!!.changeProgress(fPercent)
        }
        val strPercent = df.format((fPercent * 100).toDouble()) + "%"//进度文字
        val nPercentWidth = mBatterryPaint.measureText("999.9%").toInt() + 1  //Paint.measureText直接返回參數字串所佔用的寬度
        c.drawText(
            strPercent,
            (mWidth - nPercentWidth).toFloat(),
            mHeight - statusMarginBottom,
            mBatterryPaint
        )//x y为坐标值
        c.drawText(date!!, marginWidth, mHeight - statusMarginBottom, mBatterryPaint)
        // 画电池
        level = batteryInfoIntent!!.getIntExtra("level", 0)
        val scale = batteryInfoIntent.getIntExtra("scale", 100)
        mBatteryPercentage = level.toFloat() / scale
        val rect1Left = marginWidth + dateWith.toFloat() + statusMarginBottom//电池外框left位置
        //画电池外框
        val width = CommonUtil.instance.convertDpToPixel(mContext, 20f) - mBorderWidth
        val height = CommonUtil.instance.convertDpToPixel(mContext, 10f)
        rect1.set(
            rect1Left,
            mHeight.toFloat() - height - statusMarginBottom,
            rect1Left + width,
            mHeight - statusMarginBottom
        )
        rect2.set(
            rect1Left + mBorderWidth,
            mHeight - height + mBorderWidth - statusMarginBottom,
            rect1Left + width - mBorderWidth,
            mHeight.toFloat() - mBorderWidth - statusMarginBottom
        )
        c.save(Canvas.CLIP_SAVE_FLAG)
        c.clipRect(rect2, Region.Op.DIFFERENCE)
        c.drawRect(rect1, mBatterryPaint)
        c.restore()
        //画电量部分
        rect2.left += mBorderWidth
        rect2.right -= mBorderWidth
        rect2.right = rect2.left + rect2.width() * mBatteryPercentage
        rect2.top += mBorderWidth
        rect2.bottom -= mBorderWidth
        c.drawRect(rect2, mBatterryPaint)
        //画电池头
        val poleHeight = CommonUtil.instance.convertDpToPixel(mContext, 10f).toInt() / 2
        rect2.left = rect1.right
        rect2.top = rect2.top + poleHeight / 4
        rect2.right = rect1.right + mBorderWidth
        rect2.bottom = rect2.bottom - poleHeight / 4
        c.drawRect(rect2, mBatterryPaint)
        //画书名
        c.drawText(
            CommonUtil.instance.subString(bookName, 12),
            marginWidth,
            statusMarginBottom + mBatterryFontSize,
            mBatterryPaint
        )
        //画章
        if (directoryList.size > 0) {
            val charterName = CommonUtil.instance.subString(directoryList[currentCharter].bookCatalogue, 12)
            val nChaterWidth = mBatterryPaint.measureText(charterName).toInt() + 1
            c.drawText(
                charterName,
                mWidth.toFloat() - marginWidth - nChaterWidth.toFloat(),
                statusMarginBottom + mBatterryFontSize,
                mBatterryPaint
            )
        }

        mBookPageWidget!!.postInvalidate()
    }

    //向前翻页
    fun prePage() {
        if (currentPage!!.getBegin() <= 0) {
            Log.e(TAG, "当前是第一页")
            if (!m_isfirstPage) {
                Toast.makeText(mContext, "当前是第一页", Toast.LENGTH_SHORT).show()
            }
            m_isfirstPage = true
            return
        } else {
            m_isfirstPage = false
        }

        cancelPage = currentPage
        onDraw(mBookPageWidget!!.curPage, currentPage!!.getLines(), true)
        currentPage = getPrePage()
        onDraw(mBookPageWidget!!.nextPage, currentPage!!.getLines(), true)
    }

    //向后翻页
    fun nextPage() {
        if (currentPage!!.getEnd() >= mBookUtil.bookLen) {
            Log.e(TAG, "已经是最后一页了")
            if (!m_islastPage) {
                Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show()
            }
            m_islastPage = true
            return
        } else {
            m_islastPage = false
        }

        cancelPage = currentPage
        onDraw(mBookPageWidget!!.curPage, currentPage!!.getLines(), true)
        prePage = currentPage
        currentPage = nextPage
        onDraw(mBookPageWidget!!.nextPage, currentPage!!.getLines(), true)
        //        Log.e("nextPage","nextPagenext");
    }

    //取消翻页
    fun cancelPage() {
        currentPage = cancelPage
    }

    /**
     * 打开书本
     * @throws IOException
     */
    @Throws(IOException::class)
    fun openBook(bookList: Book) {
        //清空数据
        currentCharter = 0
        //        m_mbBufLen = 0;
        initBg(MyConfig.instance.getDayOrNight())

        this.bookList = bookList
        bookPath = bookList.getBookpath()
        bookName = CommonUtil.instance.getFileName(bookPath)

        status = Status.OPENING
        drawStatus(mBookPageWidget!!.curPage)
        drawStatus(mBookPageWidget!!.nextPage)
        if (bookTask != null && bookTask!!.status != AsyncTask.Status.FINISHED) {
            bookTask!!.cancel(true)
        }
        bookTask = BookTask()
        bookTask!!.execute(bookList.getBegin())
    }

    private inner class BookTask : AsyncTask<Long, Void, Boolean>() {

        private var begin: Long = 0
        override fun onPostExecute(result: Boolean?) {
            //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
            super.onPostExecute(result)
            //            Log.e("onPostExecute",isCancelled() + "");
            if (isCancelled) {
                return
            }
            if (result!!) {
                PageFactory.status = PageFactory.Status.FINISH
                //                m_mbBufLen = mBookUtil.getBookLen();
                //mBookUtil.getDirectoryList().get(num).getBookCatalogueStartPos()
                currentPage = getPageForBegin(begin)
                if (mBookPageWidget != null) {
                    currentPage(true)
                }
            } else {
                PageFactory.status = PageFactory.Status.FAIL
                drawStatus(mBookPageWidget!!.curPage)
                drawStatus(mBookPageWidget!!.nextPage)
                Toast.makeText(mContext, "打开书本失败！", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onPreExecute() {
            //onPreExecute方法用于在执行后台任务前做一些UI操作
            super.onPreExecute()
        }

        override fun onProgressUpdate(vararg values: Void) {
            //onProgressUpdate方法用于更新进度信息
            super.onProgressUpdate(*values)
        }

        override fun doInBackground(vararg p0: Long?): Boolean {
            //doInBackground方法内部执行后台任务,不可在此方法内修改UI
            begin = p0[0]!!
            try {
                mBookUtil.openBook(bookList!!)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

            return true
        }

    }

    fun getPrePage(): TRPage {
        mBookUtil.setPostition(currentPage!!.getBegin())

        val trPage = TRPage()
        trPage.setEnd(mBookUtil.position - 1)
        //        Log.e("end",mBookUtil.getPosition() - 1 + "");
        trPage.setLines(preLines)
        //        Log.e("begin",mBookUtil.getPosition() + "");
        trPage.setBegin(mBookUtil.position)
        return trPage
    }

    fun getPageForBegin(begin: Long): TRPage {
        val trPage = TRPage()
        trPage.setBegin(begin)

        mBookUtil.setPostition(begin - 1)
        trPage.setLines(nextLines)
        trPage.setEnd(mBookUtil.position)
        return trPage
    }

    //上一章
    fun preChapter() {
        if (mBookUtil.getDirectoryList().size > 0) {
            var num = currentCharter
            if (num == 0) {
                num = getCurrentCharter()
            }
            num--
            if (num >= 0) {
                val begin = mBookUtil.getDirectoryList()[num].bookCatalogueStartPos
                currentPage = getPageForBegin(begin)
                currentPage(true)
                currentCharter = num
            }
        }
    }

    //下一章
    fun nextChapter() {
        var num = currentCharter
        if (num == 0) {
            num = getCurrentCharter()
        }
        num++
        if (num < directoryList.size) {
            val begin = directoryList[num].bookCatalogueStartPos
            currentPage = getPageForBegin(begin)
            currentPage(true)
            currentCharter = num
        }
    }

    //获取现在的章
    fun getCurrentCharter(): Int {
        var num = 0
        var i = 0
        while (directoryList.size > i) {
            val bookCatalogue = directoryList[i]
            if (currentPage!!.getEnd() >= bookCatalogue.bookCatalogueStartPos) {
                num = i
            } else {
                break
            }
            i++
        }
        return num
    }

    //绘制当前页面
    fun currentPage(updateChapter: Boolean?) {
        onDraw(mBookPageWidget!!.curPage, currentPage!!.getLines(), updateChapter)
        onDraw(mBookPageWidget!!.nextPage, currentPage!!.getLines(), updateChapter)
    }

    //更新电量
    fun updateBattery(mLevel: Int) {
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget!!.isRunning) {
            if (level != mLevel) {
                level = mLevel
                currentPage(false)
            }
        }
    }

    fun updateTime() {
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget!!.isRunning) {
            val mDate = sdf.format(java.util.Date())
            if (date !== mDate) {
                date = mDate
                currentPage(false)
            }
        }
    }

    //改变进度
    fun changeProgress(progress: Float) {
        val begin = (mBookUtil.bookLen * progress).toLong()
        currentPage = getPageForBegin(begin)
        currentPage(true)
    }

    //改变进度
    fun changeChapter(begin: Long) {
        currentPage = getPageForBegin(begin)
        currentPage(true)
    }

    //改变字体大小
    fun changeFontSize(fontSize: Int) {
        this.fontSize = fontSize.toFloat()
        mPaint.textSize = this.fontSize
        calculateLineCount()
        measureMarginWidth()
        currentPage = getPageForBegin(currentPage!!.getBegin())
        currentPage(true)
    }

    //改变字体
    fun changeTypeface(typeface: Typeface) {
        this.typeface = typeface
        mPaint.typeface = typeface
        mBatterryPaint.typeface = typeface
        calculateLineCount()
        measureMarginWidth()
        currentPage = getPageForBegin(currentPage!!.getBegin())
        currentPage(true)
    }

    //改变背景
    fun changeBookBg(type: Int) {
        setBookBg(type)
        currentPage(false)
    }

    //设置页面的背景
    fun setBookBg(type: Int) {
        var bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565)
        var canvas: Canvas? = Canvas(bitmap)
        var color = 0
        when (type) {
            MyConfig.BOOK_BG_DEFAULT -> {
                canvas = null
                bitmap.recycle()
                if (bgBitmap != null) {
                    bgBitmap!!.recycle()
                }
                bitmap = BitmapUtil.decodeSampledBitmapFromResource(
                    mContext.resources, R.drawable.paper, mWidth, mHeight
                )
                color = mContext.resources.getColor(R.color.read_font_default)
                setBookPageBg(mContext.resources.getColor(R.color.read_bg_default))
            }
            MyConfig.BOOK_BG_1 -> {
                canvas!!.drawColor(mContext.resources.getColor(R.color.read_bg_1))
                color = mContext.resources.getColor(R.color.read_font_1)
                setBookPageBg(mContext.resources.getColor(R.color.read_bg_1))
            }
            MyConfig.BOOK_BG_2 -> {
                canvas!!.drawColor(mContext.resources.getColor(R.color.read_bg_2))
                color = mContext.resources.getColor(R.color.read_font_2)
                setBookPageBg(mContext.resources.getColor(R.color.read_bg_2))
            }
            MyConfig.BOOK_BG_3 -> {
                canvas!!.drawColor(mContext.resources.getColor(R.color.read_bg_3))
                color = mContext.resources.getColor(R.color.read_font_3)
                if (mBookPageWidget != null) {
                    mBookPageWidget!!.setBgColor(mContext.resources.getColor(R.color.read_bg_3))
                }
            }
            MyConfig.BOOK_BG_4 -> {
                canvas!!.drawColor(mContext.resources.getColor(R.color.read_bg_4))
                color = mContext.resources.getColor(R.color.read_font_4)
                setBookPageBg(mContext.resources.getColor(R.color.read_bg_4))
            }
        }

        bgBitmap = bitmap
        //设置字体颜色
        setM_textColor(color)
    }

    fun setBookPageBg(color: Int) {
        if (mBookPageWidget != null) {
            mBookPageWidget!!.setBgColor(color)
        }
    }

    //设置日间或者夜间模式
    fun setDayOrNight(isNgiht: Boolean?) {
        initBg(isNgiht!!)
        currentPage(false)
    }

    fun clear() {
        currentCharter = 0
        bookPath = ""
        bookName = ""
        bookList = null
        mBookPageWidget = null
        mPageEvent = null
        cancelPage = null
        prePage = null
        currentPage = null
    }

    //是否是第一页
    fun isfirstPage(): Boolean {
        return m_isfirstPage
    }

    //是否是最后一页
    fun islastPage(): Boolean {
        return m_islastPage
    }

    //设置文字颜色
    fun setM_textColor(m_textColor: Int) {
        this.textColor = m_textColor
    }

    fun setPageWidget(mBookPageWidget: PageWidget) {
        this.mBookPageWidget = mBookPageWidget
    }

    fun setPageEvent(pageEvent: PageEvent) {
        this.mPageEvent = pageEvent
    }

    interface PageEvent {
        fun changeProgress(progress: Float)
    }

    companion object {
        private val TAG = "PageFactory"
        @get:Synchronized
        var instance: PageFactory? = null
            private set

        var status = Status.OPENING
            private set

        @Synchronized
        fun createPageFactory(context: Context): PageFactory {
            if (instance == null) {
                instance = PageFactory(context)
            }
            return instance!!
        }
    }

}
