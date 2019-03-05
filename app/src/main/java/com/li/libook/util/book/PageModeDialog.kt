package com.li.libook.util.book

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView

import com.li.libook.R
import com.li.libook.model.MyConfig
import kotlinx.android.synthetic.main.dialog_pagemode.*


/**
 * Created by Administrator on 2016/8/30 0030.
 */
class PageModeDialog : Dialog , View.OnClickListener{

    private var pageModeListener: PageModeListener? = null

    private constructor(context: Context, flag: Boolean, listener: DialogInterface.OnCancelListener) : super(
        context,
        flag,
        listener
    ) {
    }

    @JvmOverloads constructor(context: Context, themeResId: Int = R.style.setting_dialog) : super(context, themeResId) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setGravity(Gravity.BOTTOM)
        setContentView(R.layout.dialog_pagemode)

        val m = window!!.windowManager
        val d = m.defaultDisplay
        val p = window!!.attributes
        p.width = d.width
        window!!.attributes = p

        selectPageMode(MyConfig.instance.getPageMode())
        val views: Array<Int> = arrayOf(
            R.id.tv_simulation,
            R.id.tv_cover,
            R.id.tv_slide,
            R.id.tv_none
        )
        viewClick(this,views)
    }

    private fun viewClick(listener: View.OnClickListener, ids: Array<Int>) {
        for (it in ids) {
            val view : TextView = findViewById(it) as TextView
            view.setOnClickListener(listener)
        }
    }

    override
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_simulation -> {
                selectPageMode(MyConfig.PAGE_MODE_SIMULATION)
                setPageMode(MyConfig.PAGE_MODE_SIMULATION)
            }
            R.id.tv_cover -> {
                selectPageMode(MyConfig.PAGE_MODE_COVER)
                setPageMode(MyConfig.PAGE_MODE_COVER)
            }
            R.id.tv_slide -> {
                selectPageMode(MyConfig.PAGE_MODE_SLIDE)
                setPageMode(MyConfig.PAGE_MODE_SLIDE)
            }
            R.id.tv_none -> {
                selectPageMode(MyConfig.PAGE_MODE_NONE)
                setPageMode(MyConfig.PAGE_MODE_NONE)
            }
        }
    }

    //设置翻页
    fun setPageMode(pageMode: Int) {
        MyConfig.instance.setPageMode(pageMode)
        if (pageModeListener != null) {
            pageModeListener!!.changePageMode(pageMode)
        }
    }

    //选择怕翻页
    private fun selectPageMode(pageMode: Int) {
        if (pageMode == MyConfig.PAGE_MODE_SIMULATION) {
            setTextViewSelect(tv_simulation, true)
            setTextViewSelect(tv_cover, false)
            setTextViewSelect(tv_slide, false)
            setTextViewSelect(tv_none, false)
        } else if (pageMode == MyConfig.PAGE_MODE_COVER) {
            setTextViewSelect(tv_simulation, false)
            setTextViewSelect(tv_cover, true)
            setTextViewSelect(tv_slide, false)
            setTextViewSelect(tv_none, false)
        } else if (pageMode == MyConfig.PAGE_MODE_SLIDE) {
            setTextViewSelect(tv_simulation, false)
            setTextViewSelect(tv_cover, false)
            setTextViewSelect(tv_slide, true)
            setTextViewSelect(tv_none, false)
        } else if (pageMode == MyConfig.PAGE_MODE_NONE) {
            setTextViewSelect(tv_simulation, false)
            setTextViewSelect(tv_cover, false)
            setTextViewSelect(tv_slide, false)
            setTextViewSelect(tv_none, true)
        }
    }

    //设置按钮选择的背景
    private fun setTextViewSelect(textView: TextView?, isSelect: Boolean) {
        if (isSelect) {
            textView!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.button_select_bg))
            textView.setTextColor(context.resources.getColor(R.color.read_dialog_button_select))
        } else {
            textView!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.button_bg))
            textView.setTextColor(context.resources.getColor(R.color.white))
        }
    }

    fun setPageModeListener(pageModeListener: PageModeListener) {
        this.pageModeListener = pageModeListener
    }

    interface PageModeListener {
        fun changePageMode(pageMode: Int)
    }
}
