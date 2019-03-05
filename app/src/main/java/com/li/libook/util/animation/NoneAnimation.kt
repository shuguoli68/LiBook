package com.li.libook.util.animation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Scroller

/**
 * Created by Administrator on 2016/8/30 0030.
 */
class NoneAnimation(mCurrentBitmap: Bitmap, mNextBitmap: Bitmap, width: Int, height: Int) :
    AnimationProvider(mCurrentBitmap, mNextBitmap, width, height) {

    override fun drawMove(canvas: Canvas) {
        if (cancel) {
            canvas.drawBitmap(mCurPageBitmap, 0f, 0f, null)
        } else {
            canvas.drawBitmap(mNextPageBitmap, 0f, 0f, null)
        }
    }

    override fun drawStatic(canvas: Canvas) {
        if (cancel) {
            canvas.drawBitmap(mCurPageBitmap, 0f, 0f, null)
        } else {
            canvas.drawBitmap(mNextPageBitmap, 0f, 0f, null)
        }
    }

    override fun startAnimation(scroller: Scroller) {

    }

}
