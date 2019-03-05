package com.li.libook.util.book

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import com.li.libook.model.MyConfig
import com.li.libook.util.animation.*


/**
 * Created by Administrator on 2016/8/29 0029.
 */
class PageWidget @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(mContext, attrs, defStyleAttr) {
    //    private final static String TAG = "BookPageWidget";
    private var mScreenWidth = 0 // 屏幕宽
    private var mScreenHeight = 0 // 屏幕高

    //是否移动了
    private var isMove: Boolean? = false
    //是否翻到下一页
    private var isNext: Boolean? = false
    //是否取消翻页
    private var cancelPage: Boolean? = false
    //是否没下一页或者上一页
    private var noNext: Boolean? = false
    private var downX = 0
    private var downY = 0

    private var moveX = 0
    private var moveY = 0
    //翻页动画是否在执行
    private var isRuning: Boolean? = false

    var curPage: Bitmap? = null
        internal set // 当前页
    var nextPage: Bitmap? = null
        internal set
    private var mAnimationProvider: AnimationProvider? = null

    internal var mScroller: Scroller
    private var mBgColor = -0x313d64
    private var mTouchListener: TouchListener? = null

    val isRunning: Boolean
        get() = isRuning!!

    init {
        initPage()
        mScroller = Scroller(context, LinearInterpolator())
        mAnimationProvider = SimulationAnimation(curPage!!, nextPage!!, mScreenWidth, mScreenHeight)
    }

    private fun initPage() {
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metric = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metric)
        mScreenWidth = metric.widthPixels
        mScreenHeight = metric.heightPixels
        curPage = Bitmap.createBitmap(
            mScreenWidth,
            mScreenHeight,
            Bitmap.Config.RGB_565
        )      //android:LargeHeap=true  use in  manifest application
        nextPage = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565)
    }

    fun setPageMode(pageMode: Int) {
        when (pageMode) {
            MyConfig.PAGE_MODE_SIMULATION -> mAnimationProvider =
                    SimulationAnimation(curPage!!, nextPage!!, mScreenWidth, mScreenHeight)
            MyConfig.PAGE_MODE_COVER -> mAnimationProvider =
                    CoverAnimation(curPage!!, nextPage!!, mScreenWidth, mScreenHeight)
            MyConfig.PAGE_MODE_SLIDE -> mAnimationProvider =
                    SlideAnimation(curPage, nextPage, mScreenWidth, mScreenHeight)
            MyConfig.PAGE_MODE_NONE -> mAnimationProvider =
                    NoneAnimation(curPage!!, nextPage!!, mScreenWidth, mScreenHeight)
            else -> mAnimationProvider = SimulationAnimation(curPage!!, nextPage!!, mScreenWidth, mScreenHeight)
        }
    }

    fun setBgColor(color: Int) {
        mBgColor = color
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(mBgColor)
        //        Log.e("onDraw","isNext:" + isNext + "          isRuning:" + isRuning);
        if (isRuning!!) {
            mAnimationProvider!!.drawMove(canvas)
        } else {
            mAnimationProvider!!.drawStatic(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (PageFactory.status === PageFactory.Status.OPENING) {
            return true
        }

        val x = event.x.toInt()
        val y = event.y.toInt()

        mAnimationProvider!!.setTouchPoint(x.toFloat(), y.toFloat())
        if (event.action == MotionEvent.ACTION_DOWN) {
            downX = event.x.toInt()
            downY = event.y.toInt()
            moveX = 0
            moveY = 0
            isMove = false
            //            cancelPage = false;
            noNext = false
            isNext = false
            isRuning = false
            mAnimationProvider!!.setStartPoint(downX.toFloat(), downY.toFloat())
            abortAnimation()
            //            Log.e(TAG,"ACTION_DOWN");
        } else if (event.action == MotionEvent.ACTION_MOVE) {

            val slop = ViewConfiguration.get(context).scaledTouchSlop
            //判断是否移动了
            if (!(isMove!!)) {
                isMove = Math.abs(downX - x) > slop || Math.abs(downY - y) > slop
            }

            if (isMove!!) {
                isMove = true
                if (moveX == 0 && moveY == 0) {
                    //                    Log.e(TAG,"isMove");
                    //判断翻得是上一页还是下一页
                    if (x - downX > 0) {
                        isNext = false
                    } else {
                        isNext = true
                    }
                    cancelPage = false
                    if (isNext!!) {
                        val isNext = mTouchListener!!.nextPage()
                        mAnimationProvider!!.direction = AnimationProvider.Direction.next

                        if (!(isNext!!)) {
                            noNext = true
                            return true
                        }
                    } else {
                        val isPre = mTouchListener!!.prePage()
                        mAnimationProvider!!.direction = AnimationProvider.Direction.pre

                        if (!(isPre!!)) {
                            noNext = true
                            return true
                        }
                    }
                    //                    Log.e(TAG,"isNext:" + isNext);
                } else {
                    //判断是否取消翻页
                    if (isNext!!) {
                        if (x - moveX > 0) {
                            cancelPage = true
                            mAnimationProvider!!.cancel = true
                        } else {
                            cancelPage = false
                            mAnimationProvider!!.cancel = false
                        }
                    } else {
                        if (x - moveX < 0) {
                            mAnimationProvider!!.cancel = true
                            cancelPage = true
                        } else {
                            mAnimationProvider!!.cancel = false
                            cancelPage = false
                        }
                    }
                    //                    Log.e(TAG,"cancelPage:" + cancelPage);
                }

                moveX = x
                moveY = y
                isRuning = true
                this.postInvalidate()
            }
        } else if (event.action == MotionEvent.ACTION_UP) {
            //            Log.e(TAG,"ACTION_UP");
            if (!(isMove!!)) {
                cancelPage = false
                //是否点击了中间
                if (downX > mScreenWidth / 5 && downX < mScreenWidth * 4 / 5 && downY > mScreenHeight / 3 && downY < mScreenHeight * 2 / 3) {
                    if (mTouchListener != null) {
                        mTouchListener!!.center()
                    }
                    //                    Log.e(TAG,"center");
                    //                    mCornerX = 1; // 拖拽点对应的页脚
                    //                    mCornerY = 1;
                    //                    mTouch.x = 0.1f;
                    //                    mTouch.y = 0.1f;
                    return true
                } else if (x < mScreenWidth / 2) {
                    isNext = false
                } else {
                    isNext = true
                }

                if (isNext!!) {
                    val isNext = mTouchListener!!.nextPage()
                    mAnimationProvider!!.direction = AnimationProvider.Direction.next
                    if (!(isNext!!)) {
                        return true
                    }
                } else {
                    val isPre = mTouchListener!!.prePage()
                    mAnimationProvider!!.direction = AnimationProvider.Direction.pre
                    if (!(isPre!!)) {
                        return true
                    }
                }
            }

            if (cancelPage!! && mTouchListener != null) {
                mTouchListener!!.cancel()
            }

            //            Log.e(TAG,"isNext:" + isNext);
            if (!(noNext!!)) {
                isRuning = true
                mAnimationProvider!!.startAnimation(mScroller)
                this.postInvalidate()
            }
        }

        return true
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            val x = mScroller.currX.toFloat()
            val y = mScroller.currY.toFloat()
            mAnimationProvider!!.setTouchPoint(x, y)
            if (mScroller.finalX.toFloat() == x && mScroller.finalY.toFloat() == y) {
                isRuning = false
            }
            postInvalidate()
        }
        super.computeScroll()
    }

    fun abortAnimation() {
        if (!mScroller.isFinished) {
            mScroller.abortAnimation()
            mAnimationProvider!!.setTouchPoint(mScroller.finalX.toFloat(), mScroller.finalY.toFloat())
            postInvalidate()
        }
    }

    fun setTouchListener(mTouchListener: TouchListener) {
        this.mTouchListener = mTouchListener
    }

    interface TouchListener {
        fun center()
        fun prePage(): Boolean?
        fun nextPage(): Boolean?
        fun cancel()
    }

}
