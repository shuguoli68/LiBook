package com.li.libook.util.animation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.Scroller

/**
 * Created by Administrator on 2016/8/30 0030.
 */
class CoverAnimation(mCurrentBitmap: Bitmap, mNextBitmap: Bitmap, width: Int, height: Int) :
    AnimationProvider(mCurrentBitmap, mNextBitmap, width, height) {

    private val mSrcRect: Rect
    private val mDestRect: Rect
    private val mBackShadowDrawableLR: GradientDrawable

    init {
        mSrcRect = Rect(0, 0, mScreenWidth, mScreenHeight)
        mDestRect = Rect(0, 0, mScreenWidth, mScreenHeight)
        val mBackShadowColors = intArrayOf(0x66000000, 0x00000000)
        mBackShadowDrawableLR = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors
        )
        mBackShadowDrawableLR.gradientType = GradientDrawable.LINEAR_GRADIENT
    }

    override fun drawMove(canvas: Canvas) {
        if (direction == AnimationProvider.Direction.next) {
            //            mSrcRect.left = (int) ( - (mScreenWidth - mTouch.x));
            //            mSrcRect.right =  mSrcRect.left + mScreenWidth;
            var dis = (mScreenWidth - myStartX + mTouch.x).toInt()
            if (dis > mScreenWidth) {
                dis = mScreenWidth
            }
            //计算bitmap截取的区域
            mSrcRect.left = mScreenWidth - dis
            //计算bitmap在canvas显示的区域
            mDestRect.right = dis
            canvas.drawBitmap(mNextPageBitmap, 0f, 0f, null)
            canvas.drawBitmap(mCurPageBitmap, mSrcRect, mDestRect, null)
            addShadow(dis, canvas)
        } else {
            mSrcRect.left = (mScreenWidth - mTouch.x).toInt()
            mDestRect.right = mTouch.x.toInt()
            canvas.drawBitmap(mCurPageBitmap, 0f, 0f, null)
            canvas.drawBitmap(mNextPageBitmap, mSrcRect, mDestRect, null)
            addShadow(mTouch.x.toInt(), canvas)
        }
    }

    override fun drawStatic(canvas: Canvas) {
        if (cancel) {
            canvas.drawBitmap(mCurPageBitmap, 0f, 0f, null)
        } else {
            canvas.drawBitmap(mNextPageBitmap, 0f, 0f, null)
        }
    }

    //添加阴影
    fun addShadow(left: Int, canvas: Canvas) {
        mBackShadowDrawableLR.setBounds(left, 0, left + 30, mScreenHeight)
        mBackShadowDrawableLR.draw(canvas)
    }

    override fun startAnimation(scroller: Scroller) {

        var dx = 0
        if (direction == AnimationProvider.Direction.next) {
            if (cancel) {
                var dis = (mScreenWidth - myStartX + mTouch.x).toInt()
                if (dis > mScreenWidth) {
                    dis = mScreenWidth
                }
                dx = mScreenWidth - dis
            } else {
                //                dx = (int) - (mTouch.x + myStartX);
                dx = (-(mTouch.x + (mScreenWidth - myStartX))).toInt()
            }
        } else {
            if (cancel) {
                dx = (-mTouch.x).toInt()
            } else {
                dx = (mScreenWidth - mTouch.x).toInt()
            }
        }
        //滑动速度保持一致
        val duration = 400 * Math.abs(dx) / mScreenWidth
        //        Log.e("duration",duration + "");
        scroller.startScroll(mTouch.x.toInt(), 0, dx, 0, duration)
    }

}
