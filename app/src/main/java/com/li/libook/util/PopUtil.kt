package com.li.libook.util

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.li.libook.App
import com.li.libook.R


class PopUtil(private val mContext:Activity,private val view: View , private val rootResId:Int){

    fun bottomPopupMenu(): PopupWindow {
        val popupWindow =
            PopupWindow(view, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        popupWindow.setAnimationStyle(R.style.MenuAnimationFade)
        // 弹出窗口显示内容视图,默认以锚定视图的左下角为起点
        popupWindow.showAtLocation(
            mContext.getLayoutInflater().inflate(rootResId, null),
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )
        //菜单背景色
        val dw = ColorDrawable(-0x1)
        popupWindow.setBackgroundDrawable(dw)
        //内容背景透明度
        backgroundAlpha(0.5f)
        //关闭事件，弹出窗口消失将背景透明度改回来
        popupWindow.setOnDismissListener(popupDismissListener())
        return popupWindow
    }

    //内容背景透明度
    private fun backgroundAlpha(bgAlpha: Float) {
        val lp = mContext.getWindow().getAttributes()
        lp.alpha = bgAlpha //0.0-1.0
        mContext.getWindow().setAttributes(lp)
    }

    internal inner class popupDismissListener : PopupWindow.OnDismissListener {
        override fun onDismiss() {
            backgroundAlpha(1.0f)
        }
    }
}