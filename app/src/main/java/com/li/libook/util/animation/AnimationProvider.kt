package com.li.libook.util.animation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.widget.Scroller

/**
 * Created by Administrator on 2016/8/1 0001.
 */
abstract class AnimationProvider(
    //    private Mode myMode = Mode.NoScrolling;

    protected var mCurPageBitmap: Bitmap,
    protected var mNextPageBitmap: Bitmap,
    protected var mScreenWidth: Int,
    protected var mScreenHeight: Int
) {
    protected var myStartX: Float = 0.toFloat()
    protected var myStartY: Float = 0.toFloat()
    protected var myEndX: Int = 0
    protected var myEndY: Int = 0
    protected var myDirection: Direction? = null
    protected var mySpeed: Float = 0.toFloat()

    protected var mTouch = PointF() // 拖拽点
    //设置方向
    var direction = Direction.none
    var cancel = false
    //    static enum Mode {
    //        NoScrolling(false),
    //        ManualScrolling(false),
    //        AnimatedScrollingForward(true),
    //        AnimatedScrollingBackward(true);
    //
    //        final boolean Auto;
    //
    //        Mode(boolean auto) {
    //            Auto = auto;
    //        }
    //    }

    enum class Direction private constructor(val IsHorizontal: Boolean) {
        none(true), next(true), pre(true), up(false), down(false)
    }

    enum class Animation {
        none, curl, slide, shift
    }

    //绘制滑动页面
    abstract fun drawMove(canvas: Canvas)

    //绘制不滑动页面
    abstract fun drawStatic(canvas: Canvas)

    //设置开始拖拽点
    open fun setStartPoint(x: Float, y: Float) {
        myStartX = x
        myStartY = y
    }

    //设置拖拽点
    open fun setTouchPoint(x: Float, y: Float) {
        mTouch.x = x
        mTouch.y = y
    }

    abstract fun startAnimation(scroller: Scroller)

}
