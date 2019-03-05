package com.li.libook.util.animation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Region
import android.graphics.drawable.GradientDrawable
import android.widget.Scroller

/**
 * Created by Administrator on 2016/8/26 0026.
 */
class SimulationAnimation(mCurrentBitmap: Bitmap, mNextBitmap: Bitmap, width: Int, height: Int) :
    AnimationProvider(mCurrentBitmap, mNextBitmap, width, height) {
    private var mCornerX = 1 // 拖拽点对应的页脚
    private var mCornerY = 1
    private val mPath0: Path
    private val mPath1: Path

    internal var mBezierStart1 = PointF() // 贝塞尔曲线起始点
    internal var mBezierControl1 = PointF() // 贝塞尔曲线控制点
    internal var mBeziervertex1 = PointF() // 贝塞尔曲线顶点
    internal var mBezierEnd1 = PointF() // 贝塞尔曲线结束点

    internal var mBezierStart2 = PointF() // 另一条贝塞尔曲线
    internal var mBezierControl2 = PointF()
    internal var mBeziervertex2 = PointF()
    internal var mBezierEnd2 = PointF()

    internal var mMiddleX: Float = 0.toFloat()
    internal var mMiddleY: Float = 0.toFloat()
    internal var mDegrees: Float = 0.toFloat()
    internal var mTouchToCornerDis: Float = 0.toFloat()
    internal var mColorMatrixFilter: ColorMatrixColorFilter
    internal var mMatrix: Matrix
    internal var mMatrixArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1.0f)

    internal var mIsRTandLB: Boolean = false // 是否属于右上左下
    private val mMaxLength: Float
    lateinit var mBackShadowColors: IntArray// 背面颜色组
    lateinit var mFrontShadowColors: IntArray// 前面颜色组
    lateinit var mBackShadowDrawableLR: GradientDrawable // 有阴影的GradientDrawable
    lateinit var mBackShadowDrawableRL: GradientDrawable
    lateinit var mFolderShadowDrawableLR: GradientDrawable
    lateinit var mFolderShadowDrawableRL: GradientDrawable

    lateinit var mFrontShadowDrawableHBT: GradientDrawable
    lateinit var mFrontShadowDrawableHTB: GradientDrawable
    lateinit var mFrontShadowDrawableVLR: GradientDrawable
    lateinit var mFrontShadowDrawableVRL: GradientDrawable

    internal var mPaint: Paint

    init {

        mPath0 = Path()
        mPath1 = Path()
        mMaxLength = Math.hypot(mScreenWidth.toDouble(), mScreenHeight.toDouble()).toFloat()
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL

        createDrawable()

        val cm = ColorMatrix()//设置颜色数组
        //        float array[] = { 0.55f, 0, 0, 0, 80.0f,
        //                           0, 0.55f, 0, 0, 80.0f,
        //                           0, 0,0.55f, 0, 80.0f,
        //                           0, 0, 0, 0.2f, 0 };
        val array = floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f)
        cm.set(array)
        mColorMatrixFilter = ColorMatrixColorFilter(cm)
        mMatrix = Matrix()

        mTouch.x = 0.01f // 不让x,y为0,否则在点计算时会有问题
        mTouch.y = 0.01f
    }

    override fun drawMove(canvas: Canvas) {
        if (direction == AnimationProvider.Direction.next) {
            calcPoints()
            drawCurrentPageArea(canvas, mCurPageBitmap, mPath0)
            drawNextPageAreaAndShadow(canvas, mNextPageBitmap)
            drawCurrentPageShadow(canvas)
            drawCurrentBackArea(canvas, mCurPageBitmap)
        } else {
            calcPoints()
            drawCurrentPageArea(canvas, mNextPageBitmap, mPath0)
            drawNextPageAreaAndShadow(canvas, mCurPageBitmap)
            drawCurrentPageShadow(canvas)
            drawCurrentBackArea(canvas, mNextPageBitmap)
        }
    }

    override fun drawStatic(canvas: Canvas) {
        if (cancel) {
            canvas.drawBitmap(mCurPageBitmap, 0f, 0f, null)
        } else {
            canvas.drawBitmap(mNextPageBitmap, 0f, 0f, null)
        }
    }

    fun cancel(isCancel: Boolean) {
        super.cancel = isCancel
    }

    override fun startAnimation(scroller: Scroller) {
        var dx: Int
        val dy: Int
        // dx 水平方向滑动的距离，负值会使滚动向左滚动
        // dy 垂直方向滑动的距离，负值会使滚动向上滚动
        if (cancel) {
            if (mCornerX > 0 && direction == AnimationProvider.Direction.next) {
                dx = (mScreenWidth - mTouch.x).toInt()
            } else {
                dx = -mTouch.x.toInt()
            }

            if (direction != AnimationProvider.Direction.next) {
                dx = (-(mScreenWidth + mTouch.x)).toInt()
            }

            if (mCornerY > 0) {
                dy = (mScreenHeight - mTouch.y).toInt()
            } else {
                dy = -mTouch.y.toInt() // 防止mTouch.y最终变为0
            }
        } else {
            if (mCornerX > 0 && direction == AnimationProvider.Direction.next) {
                dx = -(mScreenWidth + mTouch.x).toInt()
            } else {
                dx = (mScreenWidth - mTouch.x + mScreenWidth).toInt()
            }
            if (mCornerY > 0) {
                dy = (mScreenHeight - mTouch.y).toInt()
            } else {
                dy = (1 - mTouch.y).toInt() // 防止mTouch.y最终变为0
            }
        }
        scroller.startScroll(mTouch.x.toInt(), mTouch.y.toInt(), dx, dy, 400)
    }


    fun direction(direction: AnimationProvider.Direction) {
        super.direction = direction
        when (direction) {
            AnimationProvider.Direction.pre ->
                //上一页滑动不出现对角
                if (myStartX > mScreenWidth / 2) {
                    calcCornerXY(myStartX, mScreenHeight.toFloat())
                } else {
                    calcCornerXY(mScreenWidth - myStartX, mScreenHeight.toFloat())
                }
            AnimationProvider.Direction.next -> if (mScreenWidth / 2 > myStartX) {
                calcCornerXY(mScreenWidth - myStartX, myStartY)
            }
        }
    }

    override fun setStartPoint(x: Float, y: Float) {
        super.setStartPoint(x, y)
        calcCornerXY(x, y)
    }

    override fun setTouchPoint(x: Float, y: Float) {
        super.setTouchPoint(x, y)
        //触摸y中间位置吧y变成屏幕高度
        if (myStartY > mScreenHeight / 3 && myStartY < mScreenHeight * 2 / 3 || direction == AnimationProvider.Direction.pre) {
            mTouch.y = mScreenHeight.toFloat()
        }

        if (myStartY > mScreenHeight / 3 && myStartY < mScreenHeight / 2 && direction == AnimationProvider.Direction.next) {
            mTouch.y = 1f
        }
    }

    /**
     * 创建阴影的GradientDrawable
     */
    private fun createDrawable() {
        val color = intArrayOf(0x333333, -0x4fcccccd)
        mFolderShadowDrawableRL = GradientDrawable(
            GradientDrawable.Orientation.RIGHT_LEFT, color
        )
        mFolderShadowDrawableRL.gradientType = GradientDrawable.LINEAR_GRADIENT

        mFolderShadowDrawableLR = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, color
        )
        mFolderShadowDrawableLR.gradientType = GradientDrawable.LINEAR_GRADIENT

        mBackShadowColors = intArrayOf(-0xeeeeef, 0x111111)
        mBackShadowDrawableRL = GradientDrawable(
            GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors
        )
        mBackShadowDrawableRL.gradientType = GradientDrawable.LINEAR_GRADIENT

        mBackShadowDrawableLR = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors
        )
        mBackShadowDrawableLR.gradientType = GradientDrawable.LINEAR_GRADIENT

        mFrontShadowColors = intArrayOf(-0x7feeeeef, 0x111111)
        mFrontShadowDrawableVLR = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors
        )
        mFrontShadowDrawableVLR.gradientType = GradientDrawable.LINEAR_GRADIENT
        mFrontShadowDrawableVRL = GradientDrawable(
            GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors
        )
        mFrontShadowDrawableVRL.gradientType = GradientDrawable.LINEAR_GRADIENT

        mFrontShadowDrawableHTB = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors
        )
        mFrontShadowDrawableHTB.gradientType = GradientDrawable.LINEAR_GRADIENT

        mFrontShadowDrawableHBT = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors
        )
        mFrontShadowDrawableHBT.gradientType = GradientDrawable.LINEAR_GRADIENT
    }

    /**
     * 是否能够拖动过去
     *
     * @return
     */
    fun canDragOver(): Boolean {
        return if (mTouchToCornerDis > mScreenWidth / 10) true else false
    }

    fun right(): Boolean {
        return if (mCornerX > -4) false else true
    }

    /**
     * 绘制翻起页背面
     *
     * @param canvas
     * @param bitmap
     */
    private fun drawCurrentBackArea(canvas: Canvas, bitmap: Bitmap) {
        val i = (mBezierStart1.x + mBezierControl1.x).toInt() / 2
        val f1 = Math.abs(i - mBezierControl1.x)
        val i1 = (mBezierStart2.y + mBezierControl2.y).toInt() / 2
        val f2 = Math.abs(i1 - mBezierControl2.y)
        val f3 = Math.min(f1, f2)
        mPath1.reset()
        mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y)
        mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y)
        mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y)
        mPath1.lineTo(mTouch.x, mTouch.y)
        mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y)
        mPath1.close()
        val mFolderShadowDrawable: GradientDrawable
        val left: Int
        val right: Int
        if (mIsRTandLB) {
            left = (mBezierStart1.x - 1).toInt()
            right = (mBezierStart1.x + f3 + 1f).toInt()
            mFolderShadowDrawable = mFolderShadowDrawableLR
        } else {
            left = (mBezierStart1.x - f3 - 1f).toInt()
            right = (mBezierStart1.x + 1).toInt()
            mFolderShadowDrawable = mFolderShadowDrawableRL
        }
        canvas.save()
        try {
            canvas.clipPath(mPath0)
            canvas.clipPath(mPath1, Region.Op.INTERSECT)
        } catch (e: Exception) {
        }


        mPaint.colorFilter = mColorMatrixFilter

        val dis = Math.hypot(
            (mCornerX - mBezierControl1.x).toDouble(),
            (mBezierControl2.y - mCornerY).toDouble()
        ).toFloat()
        val f8 = (mCornerX - mBezierControl1.x) / dis
        val f9 = (mBezierControl2.y - mCornerY) / dis
        mMatrixArray[0] = 1 - 2f * f9 * f9
        mMatrixArray[1] = 2f * f8 * f9
        mMatrixArray[3] = mMatrixArray[1]
        mMatrixArray[4] = 1 - 2f * f8 * f8
        mMatrix.reset()
        mMatrix.setValues(mMatrixArray)
        mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y)
        mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y)
        canvas.drawBitmap(bitmap, mMatrix, mPaint)
        // canvas.drawBitmap(bitmap, mMatrix, null);
        mPaint.colorFilter = null

        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y)
        mFolderShadowDrawable.setBounds(
            left, mBezierStart1.y.toInt(), right,
            (mBezierStart1.y + mMaxLength).toInt()
        )
        mFolderShadowDrawable.draw(canvas)
        canvas.restore()
    }

    /**
     * 绘制翻起页的阴影
     *
     * @param canvas
     */
    fun drawCurrentPageShadow(canvas: Canvas) {
        val degree: Double
        if (mIsRTandLB) {
            degree = Math.PI / 4 -
                    Math.atan2((mBezierControl1.y - mTouch.y).toDouble(), (mTouch.x - mBezierControl1.x).toDouble())
        } else {
            degree = Math.PI / 4 -
                    Math.atan2((mTouch.y - mBezierControl1.y).toDouble(), (mTouch.x - mBezierControl1.x).toDouble())
        }
        // 翻起页阴影顶点与touch点的距离
        val d1 = 25.toFloat().toDouble() * 1.414 * Math.cos(degree)
        val d2 = 25.toFloat().toDouble() * 1.414 * Math.sin(degree)
        val x = (mTouch.x + d1).toFloat()
        val y: Float
        if (mIsRTandLB) {
            y = (mTouch.y + d2).toFloat()
        } else {
            y = (mTouch.y - d2).toFloat()
        }
        mPath1.reset()
        mPath1.moveTo(x, y)
        mPath1.lineTo(mTouch.x, mTouch.y)
        mPath1.lineTo(mBezierControl1.x, mBezierControl1.y)
        mPath1.lineTo(mBezierStart1.x, mBezierStart1.y)
        mPath1.close()
        var rotateDegrees: Float
        canvas.save()
        try {
            canvas.clipPath(mPath0, Region.Op.XOR)
            canvas.clipPath(mPath1, Region.Op.INTERSECT)
        } catch (e: Exception) {
            // TODO: handle exception
        }

        var leftx: Int
        var rightx: Int
        var mCurrentPageShadow: GradientDrawable
        if (mIsRTandLB) {
            leftx = mBezierControl1.x.toInt()
            rightx = mBezierControl1.x.toInt() + 25
            mCurrentPageShadow = mFrontShadowDrawableVLR
        } else {
            leftx = (mBezierControl1.x - 25).toInt()
            rightx = mBezierControl1.x.toInt() + 1
            mCurrentPageShadow = mFrontShadowDrawableVRL
        }

        rotateDegrees = Math.toDegrees(
            Math.atan2(
                (mTouch.x - mBezierControl1.x).toDouble(),
                (mBezierControl1.y - mTouch.y).toDouble()
            )
        ).toFloat()
        canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y)
        mCurrentPageShadow.setBounds(
            leftx,
            (mBezierControl1.y - mMaxLength).toInt(), rightx,
            mBezierControl1.y.toInt()
        )
        mCurrentPageShadow.draw(canvas)
        canvas.restore()

        mPath1.reset()
        mPath1.moveTo(x, y)
        mPath1.lineTo(mTouch.x, mTouch.y)
        mPath1.lineTo(mBezierControl2.x, mBezierControl2.y)
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y)
        mPath1.close()
        canvas.save()
        try {
            canvas.clipPath(mPath0, Region.Op.XOR)
            canvas.clipPath(mPath1, Region.Op.INTERSECT)
        } catch (e: Exception) {
        }

        if (mIsRTandLB) {
            leftx = mBezierControl2.y.toInt()
            rightx = (mBezierControl2.y + 25).toInt()
            mCurrentPageShadow = mFrontShadowDrawableHTB
        } else {
            leftx = (mBezierControl2.y - 25).toInt()
            rightx = (mBezierControl2.y + 1).toInt()
            mCurrentPageShadow = mFrontShadowDrawableHBT
        }
        rotateDegrees = Math.toDegrees(
            Math.atan2(
                (mBezierControl2.y - mTouch.y).toDouble(),
                (mBezierControl2.x - mTouch.x).toDouble()
            )
        ).toFloat()
        canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y)
        val temp: Float
        if (mBezierControl2.y < 0)
            temp = mBezierControl2.y - mScreenHeight
        else
            temp = mBezierControl2.y

        val hmg = Math.hypot(mBezierControl2.x.toDouble(), temp.toDouble()).toInt()
        if (hmg > mMaxLength)
            mCurrentPageShadow
                .setBounds(
                    (mBezierControl2.x - 25).toInt() - hmg, leftx,
                    (mBezierControl2.x + mMaxLength).toInt() - hmg,
                    rightx
                )
        else
            mCurrentPageShadow.setBounds(
                (mBezierControl2.x - mMaxLength).toInt(), leftx,
                mBezierControl2.x.toInt(), rightx
            )

        // Log.i("hmg", "mBezierControl2.x   " + mBezierControl2.x
        // + "  mBezierControl2.y  " + mBezierControl2.y);
        mCurrentPageShadow.draw(canvas)
        canvas.restore()
    }

    private fun drawNextPageAreaAndShadow(canvas: Canvas, bitmap: Bitmap) {
        mPath1.reset()
        mPath1.moveTo(mBezierStart1.x, mBezierStart1.y)
        mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y)
        mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y)
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y)
        mPath1.lineTo(mCornerX.toFloat(), mCornerY.toFloat())
        mPath1.close()

        mDegrees = Math.toDegrees(
            Math.atan2(
                (mBezierControl1.x - mCornerX).toDouble(),
                (mBezierControl2.y - mCornerY).toDouble()
            )
        ).toFloat()
        val leftx: Int
        val rightx: Int
        val mBackShadowDrawable: GradientDrawable
        if (mIsRTandLB) {  //左下及右上
            leftx = mBezierStart1.x.toInt()
            rightx = (mBezierStart1.x + mTouchToCornerDis / 4).toInt()
            mBackShadowDrawable = mBackShadowDrawableLR
        } else {
            leftx = (mBezierStart1.x - mTouchToCornerDis / 4).toInt()
            rightx = mBezierStart1.x.toInt()
            mBackShadowDrawable = mBackShadowDrawableRL
        }
        canvas.save()
        try {
            canvas.clipPath(mPath0)
            canvas.clipPath(mPath1, Region.Op.INTERSECT)
        } catch (e: Exception) {
        }


        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y)
        mBackShadowDrawable.setBounds(
            leftx, mBezierStart1.y.toInt(), rightx,
            (mMaxLength + mBezierStart1.y).toInt()
        )//左上及右下角的xy坐标值,构成一个矩形
        mBackShadowDrawable.draw(canvas)
        canvas.restore()
    }

    private fun drawCurrentPageArea(canvas: Canvas, bitmap: Bitmap, path: Path) {
        mPath0.reset()
        mPath0.moveTo(mBezierStart1.x, mBezierStart1.y)
        mPath0.quadTo(
            mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x,
            mBezierEnd1.y
        )
        mPath0.lineTo(mTouch.x, mTouch.y)
        mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y)
        mPath0.quadTo(
            mBezierControl2.x, mBezierControl2.y, mBezierStart2.x,
            mBezierStart2.y
        )
        mPath0.lineTo(mCornerX.toFloat(), mCornerY.toFloat())
        mPath0.close()

        canvas.save()
        canvas.clipPath(path, Region.Op.XOR)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        try {
            canvas.restore()
        } catch (e: Exception) {

        }

    }

    /**
     * 计算拖拽点对应的拖拽脚
     *
     * @param x
     * @param y
     */
    fun calcCornerXY(x: Float, y: Float) {
        //  Log.i("hck", "PageWidget x:" + x + "      y" + y);
        if (x <= mScreenWidth / 2) {
            mCornerX = 0
        } else {
            mCornerX = mScreenWidth
        }
        if (y <= mScreenHeight / 2) {
            mCornerY = 0
        } else {
            mCornerY = mScreenHeight
        }

        if (mCornerX == 0 && mCornerY == mScreenHeight || mCornerX == mScreenWidth && mCornerY == 0) {
            mIsRTandLB = true
        } else {
            mIsRTandLB = false
        }

    }

    private fun calcPoints() {
        mMiddleX = (mTouch.x + mCornerX) / 2
        mMiddleY = (mTouch.y + mCornerY) / 2
        mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX)
        mBezierControl1.y = mCornerY.toFloat()
        mBezierControl2.x = mCornerX.toFloat()
        //   mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
        //   * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

        val f4 = mCornerY - mMiddleY
        if (f4 == 0f) {
            mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / 0.1f
            //    Log.d("PageWidget",""+f4);
        } else {
            mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY)
            //    Log.d("PageWidget","没有进入if判断"+ mBezierControl2.y + "");
        }

        // Log.i("hmg", "mTouchX  " + mTouch.x + "  mTouchY  " + mTouch.y);
        // Log.i("hmg", "mBezierControl1.x  " + mBezierControl1.x
        // + "  mBezierControl1.y  " + mBezierControl1.y);
        // Log.i("hmg", "mBezierControl2.x  " + mBezierControl2.x
        // + "  mBezierControl2.y  " + mBezierControl2.y);

        mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2
        mBezierStart1.y = mCornerY.toFloat()

        // 当mBezierStart1.x < 0或者mBezierStart1.x > 480时
        // 如果继续翻页，会出现BUG故在此限制
        if (mTouch.x > 0 && mTouch.x < mScreenWidth) {
            if (mBezierStart1.x < 0 || mBezierStart1.x > mScreenWidth) {
                if (mBezierStart1.x < 0)
                    mBezierStart1.x = mScreenWidth - mBezierStart1.x

                val f1 = Math.abs(mCornerX - mTouch.x)
                val f2 = mScreenWidth * f1 / mBezierStart1.x
                mTouch.x = Math.abs(mCornerX - f2)

                val f3 = Math.abs(mCornerX - mTouch.x) * Math.abs(mCornerY - mTouch.y) / f1
                mTouch.y = Math.abs(mCornerY - f3)

                mMiddleX = (mTouch.x + mCornerX) / 2
                mMiddleY = (mTouch.y + mCornerY) / 2

                mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX)
                mBezierControl1.y = mCornerY.toFloat()

                mBezierControl2.x = mCornerX.toFloat()
                //    mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
                //  * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

                val f5 = mCornerY - mMiddleY
                if (f5 == 0f) {
                    mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / 0.1f
                } else {
                    mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY)
                    //    Log.d("PageWidget", mBezierControl2.y + "");
                }


                // Log.i("hmg", "mTouchX --> " + mTouch.x + "  mTouchY-->  "
                // + mTouch.y);
                // Log.i("hmg", "mBezierControl1.x--  " + mBezierControl1.x
                // + "  mBezierControl1.y -- " + mBezierControl1.y);
                // Log.i("hmg", "mBezierControl2.x -- " + mBezierControl2.x
                // + "  mBezierControl2.y -- " + mBezierControl2.y);
                mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2
            }
        }
        mBezierStart2.x = mCornerX.toFloat()
        mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y) / 2

        mTouchToCornerDis = Math.hypot(
            (mTouch.x - mCornerX).toDouble(),
            (mTouch.y - mCornerY).toDouble()
        ).toFloat()

        mBezierEnd1 = getCross(
            mTouch, mBezierControl1, mBezierStart1,
            mBezierStart2
        )
        mBezierEnd2 = getCross(
            mTouch, mBezierControl2, mBezierStart1,
            mBezierStart2
        )

        // Log.i("hmg", "mBezierEnd1.x  " + mBezierEnd1.x + "  mBezierEnd1.y  "
        // + mBezierEnd1.y);
        // Log.i("hmg", "mBezierEnd2.x  " + mBezierEnd2.x + "  mBezierEnd2.y  "
        // + mBezierEnd2.y);

        /*
		 * mBeziervertex1.x 推导
		 * ((mBezierStart1.x+mBezierEnd1.x)/2+mBezierControl1.x)/2 化简等价于
		 * (mBezierStart1.x+ 2*mBezierControl1.x+mBezierEnd1.x) / 4
		 */
        mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4
        mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4
        mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4
        mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4
    }

    /**
     * 求解直线P1P2和直线P3P4的交点坐标
     *
     * @param P1
     * @param P2
     * @param P3
     * @param P4
     * @return
     */
    fun getCross(P1: PointF, P2: PointF, P3: PointF, P4: PointF): PointF {
        val CrossP = PointF()
        // 二元函数通式： y=ax+b
        val a1 = (P2.y - P1.y) / (P2.x - P1.x)
        val b1 = (P1.x * P2.y - P2.x * P1.y) / (P1.x - P2.x)

        val a2 = (P4.y - P3.y) / (P4.x - P3.x)
        val b2 = (P3.x * P4.y - P4.x * P3.y) / (P3.x - P4.x)
        CrossP.x = (b2 - b1) / (a1 - a2)
        CrossP.y = a1 * CrossP.x + b1
        return CrossP
    }

}
