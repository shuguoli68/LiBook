package com.li.libook.util.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class SlideAnimation extends AnimationProvider {
    private Rect mSrcRect, mDestRect,mNextSrcRect,mNextDestRect;

    public SlideAnimation(Bitmap mCurrentBitmap, Bitmap mNextBitmap, int width, int height) {
        super(mCurrentBitmap, mNextBitmap, width, height);
        mSrcRect = new Rect(0, 0, getMScreenWidth(), getMScreenHeight());
        mDestRect = new Rect(0, 0, getMScreenWidth(), getMScreenHeight());
        mNextSrcRect = new Rect(0, 0, getMScreenWidth(), getMScreenHeight());
        mNextDestRect = new Rect(0, 0, getMScreenWidth(), getMScreenHeight());
    }

    @Override
    public void drawMove(Canvas canvas) {
        if (getDirection().equals(AnimationProvider.Direction.next)){
//            mSrcRect.left = (int) ( - (mScreenWidth - mTouch.x));
//            mSrcRect.right =  mSrcRect.left + mScreenWidth;
            int dis = (int) (getMScreenWidth() - getMyStartX() + getMTouch().x);
            if (dis > getMScreenWidth()){
                dis = getMScreenWidth();
            }
            //计算bitmap截取的区域
            mSrcRect.left = getMScreenWidth() - dis;
            //计算bitmap在canvas显示的区域
            mDestRect.right = dis;

            //计算下一页截取的区域
            mNextSrcRect.right = getMScreenWidth() - dis;
            //计算下一页在canvas显示的区域
            mNextDestRect.left = dis;

            canvas.drawBitmap(getMNextPageBitmap(),mNextSrcRect,mNextDestRect,null);
            canvas.drawBitmap(getMCurPageBitmap(),mSrcRect,mDestRect,null);
        }else{
            int dis = (int) (getMTouch().x - getMyStartX());
            if (dis < 0){
                dis = 0;
                setMyStartX(getMTouch().x);
            }
            mSrcRect.left =  getMScreenWidth() - dis;
            mDestRect.right = dis;

            //计算下一页截取的区域
            mNextSrcRect.right = getMScreenWidth() - dis;
            //计算下一页在canvas显示的区域
            mNextDestRect.left = dis;

            canvas.drawBitmap(getMCurPageBitmap(),mNextSrcRect,mNextDestRect,null);
            canvas.drawBitmap(getMNextPageBitmap(),mSrcRect,mDestRect,null);
        }
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (getCancel()){
            canvas.drawBitmap(getMCurPageBitmap(), 0, 0, null);
        }else {
            canvas.drawBitmap(getMNextPageBitmap(), 0, 0, null);
        }
    }

    @Override
    public void startAnimation(Scroller scroller) {

        int dx = 0;
        if (getDirection().equals(Direction.next)){
            if (getCancel()){
                int dis = (int) ((getMScreenWidth() - getMyStartX()) + getMTouch().x);
                if (dis > getMScreenWidth()){
                    dis = getMScreenWidth();
                }
                dx = getMScreenWidth() - dis;
            }else{
                dx = (int) - (getMTouch().x + (getMScreenWidth() - getMyStartX()));
            }
        }else{
            if (getCancel()){
                dx = (int) - Math.abs(getMTouch().x - getMyStartX());
            }else{
//                dx = (int) (mScreenWidth - mTouch.x);
                dx = (int) (getMScreenWidth() - (getMTouch().x - getMyStartX()));
            }
        }
        //滑动速度保持一致
         int duration =  (400 * Math.abs(dx)) / getMScreenWidth();
//        Log.e("duration",duration + "");
        scroller.startScroll((int) getMTouch().x, 0, dx, 0, duration);
    }

}
