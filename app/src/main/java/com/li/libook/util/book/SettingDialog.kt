package com.li.libook.util.book

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import com.li.libook.R
import com.li.libook.model.MyConfig
import com.li.libook.util.DisplayUtils
import com.li.libook.view.CircleImageView
import kotlinx.android.synthetic.main.dialog_setting.*


/**
 * Created by Administrator on 2016/7/26 0026.
 */
class SettingDialog : Dialog , View.OnClickListener{


    private var isSystem: Boolean? = null
    private var mSettingListener: SettingListener? = null
    private var FONT_SIZE_MIN: Int = 0
    private var FONT_SIZE_MAX: Int = 0
    private var currentFontSize: Int = 0

    val isShow: Boolean?
        get() = isShowing

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
        setContentView(R.layout.dialog_setting)

        val m = window!!.windowManager
        val d = m.defaultDisplay
        val p = window!!.attributes
        p.width = d.width
        window!!.attributes = p

        FONT_SIZE_MIN = context.resources.getDimension(R.dimen.reading_min_text_size).toInt()
        FONT_SIZE_MAX = context.resources.getDimension(R.dimen.reading_max_text_size).toInt()

        //初始化亮度
        isSystem = MyConfig.instance.isSystemLight()
        setTextViewSelect(tv_xitong, isSystem!!)
        setBrightness(MyConfig.instance.getLight())

        //初始化字体大小
        currentFontSize = MyConfig.instance.getFontSize().toInt()
        tv_size!!.text = currentFontSize.toString() + ""

        //初始化字体
        tv_default!!.typeface = MyConfig.instance.getTypeface(MyConfig.FONTTYPE_DEFAULT)
        tv_qihei!!.typeface = MyConfig.instance.getTypeface(MyConfig.FONTTYPE_QIHEI)
        //        tv_fzxinghei.setTypeface(config.getTypeface(MyConfig.FONTTYPE_FZXINGHEI));
        tv_fzkatong!!.typeface = MyConfig.instance.getTypeface(MyConfig.FONTTYPE_FZKATONG)
        tv_bysong!!.typeface = MyConfig.instance.getTypeface(MyConfig.FONTTYPE_BYSONG)
        //        tv_xinshou.setTypeface(config.getTypeface(MyConfig.FONTTYPE_XINSHOU));
        //        tv_wawa.setTypeface(config.getTypeface(MyConfig.FONTTYPE_WAWA));
        selectTypeface(MyConfig.instance.getTypefacePath())

        selectBg(MyConfig.instance.getBookBgType())

        sb_brightness!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress > 10) {
                    changeBright(false, progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        val views: Array<Int> = arrayOf(
            R.id.tv_dark,
            R.id.tv_bright,
            R.id.tv_xitong,
            R.id.tv_subtract,
            R.id.tv_add,
            R.id.tv_size_default,
            R.id.tv_qihei,
            R.id.tv_fzxinghei,
            R.id.tv_fzkatong,
            R.id.tv_bysong,
            R.id.tv_default
        )
        val views2:Array<Int> = arrayOf(
            R.id.iv_bg_default,
            R.id.iv_bg_1,
            R.id.iv_bg_2,
            R.id.iv_bg_3,
            R.id.iv_bg_4
        )
        viewClick(this,views)
        viewClick2(this,views2)
    }

    private fun viewClick(listener: View.OnClickListener, ids: Array<Int>) {
        for (it in ids) {
            val view  = findViewById<TextView>(it)
            view.setOnClickListener(listener)
        }
    }
    private fun viewClick2(listener: View.OnClickListener, ids: Array<Int>) {
        for (it in ids) {
            val view  = findViewById<CircleImageView>(it)
            view.setOnClickListener(listener)
        }
    }

    //选择背景
    private fun selectBg(type: Int) {
        when (type) {
            MyConfig.BOOK_BG_DEFAULT -> {
                iv_bg_default!!.borderWidth=(DisplayUtils.dp2px(context, 2.0f))
                iv_bg_1!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_2!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_3!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_4!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
            }
            MyConfig.BOOK_BG_1 -> {
                iv_bg_default!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_1!!.borderWidth=(DisplayUtils.dp2px(context, 2.0f))
                iv_bg_2!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_3!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_4!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
            }
            MyConfig.BOOK_BG_2 -> {
                iv_bg_default!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_1!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_2!!.borderWidth=(DisplayUtils.dp2px(context, 2.0f))
                iv_bg_3!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_4!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
            }
            MyConfig.BOOK_BG_3 -> {
                iv_bg_default!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_1!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_2!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_3!!.borderWidth=(DisplayUtils.dp2px(context, 2.0f))
                iv_bg_4!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
            }
            MyConfig.BOOK_BG_4 -> {
                iv_bg_default!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_1!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_2!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_3!!.borderWidth=(DisplayUtils.dp2px(context, 0.0f))
                iv_bg_4!!.borderWidth=(DisplayUtils.dp2px(context, 2.0f))
            }
        }
    }

    //设置字体
    fun setBookBg(type: Int) {
        MyConfig.instance.setBookBg(type)
        if (mSettingListener != null) {
            mSettingListener!!.changeBookBg(type)
        }
    }

    //选择字体
    private fun selectTypeface(typeface: String) {
        if (typeface == MyConfig.FONTTYPE_DEFAULT) {
            setTextViewSelect(tv_default, true)
            setTextViewSelect(tv_qihei, false)
            setTextViewSelect(tv_fzxinghei, false)
            setTextViewSelect(tv_fzkatong, false)
            setTextViewSelect(tv_bysong, false)
            //            setTextViewSelect(tv_xinshou, false);
            //            setTextViewSelect(tv_wawa, false);
        } else if (typeface == MyConfig.FONTTYPE_QIHEI) {
            setTextViewSelect(tv_default, false)
            setTextViewSelect(tv_qihei, true)
            setTextViewSelect(tv_fzxinghei, false)
            setTextViewSelect(tv_fzkatong, false)
            setTextViewSelect(tv_bysong, false)
            //            setTextViewSelect(tv_xinshou, false);
            //            setTextViewSelect(tv_wawa, false);
        } else if (typeface == MyConfig.FONTTYPE_FZXINGHEI) {
            setTextViewSelect(tv_default, false)
            setTextViewSelect(tv_qihei, false)
            setTextViewSelect(tv_fzxinghei, true)
            setTextViewSelect(tv_fzkatong, false)
            setTextViewSelect(tv_bysong, false)
            //            setTextViewSelect(tv_xinshou, true);
            //            setTextViewSelect(tv_wawa, false);
        } else if (typeface == MyConfig.FONTTYPE_FZKATONG) {
            setTextViewSelect(tv_default, false)
            setTextViewSelect(tv_qihei, false)
            setTextViewSelect(tv_fzxinghei, false)
            setTextViewSelect(tv_fzkatong, true)
            setTextViewSelect(tv_bysong, false)
            //            setTextViewSelect(tv_xinshou, false);
            //            setTextViewSelect(tv_wawa, true);
        } else if (typeface == MyConfig.FONTTYPE_BYSONG) {
            setTextViewSelect(tv_default, false)
            setTextViewSelect(tv_qihei, false)
            setTextViewSelect(tv_fzxinghei, false)
            setTextViewSelect(tv_fzkatong, false)
            setTextViewSelect(tv_bysong, true)
            //            setTextViewSelect(tv_xinshou, false);
            //            setTextViewSelect(tv_wawa, true);
        }
    }

    //设置字体
    fun setTypeface(typeface: String) {
        MyConfig.instance.setTypeface(typeface)
        val tface = MyConfig.instance.getTypeface(typeface)
        if (mSettingListener != null) {
            mSettingListener!!.changeTypeFace(tface)
        }
    }

    //设置亮度
    fun setBrightness(brightness: Float) {
        sb_brightness!!.progress = (brightness * 100).toInt()
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

    private fun applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return
        }
        this.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )//去掉信息栏
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_dark -> {
            }
            R.id.tv_bright -> {
            }
            R.id.tv_xitong -> {
                isSystem = !(isSystem!!)
                changeBright(isSystem, sb_brightness!!.progress)
            }
            R.id.tv_subtract -> subtractFontSize()
            R.id.tv_add -> addFontSize()
            R.id.tv_size_default -> defaultFontSize()
            R.id.tv_qihei -> {
                selectTypeface(MyConfig.FONTTYPE_QIHEI)
                setTypeface(MyConfig.FONTTYPE_QIHEI)
            }
            R.id.tv_fzxinghei -> {
                selectTypeface(MyConfig.FONTTYPE_FZXINGHEI)
                setTypeface(MyConfig.FONTTYPE_FZXINGHEI)
            }
            R.id.tv_fzkatong -> {
                selectTypeface(MyConfig.FONTTYPE_FZKATONG)
                setTypeface(MyConfig.FONTTYPE_FZKATONG)
            }
            R.id.tv_bysong -> {
                selectTypeface(MyConfig.FONTTYPE_BYSONG)
                setTypeface(MyConfig.FONTTYPE_BYSONG)
            }
            //            case R.id.tv_xinshou:
            //                selectTypeface(MyConfig.FONTTYPE_XINSHOU);
            //                setTypeface(MyConfig.FONTTYPE_XINSHOU);
            //                break;
            //            case R.id.tv_wawa:
            //                selectTypeface(MyConfig.FONTTYPE_WAWA);
            //                setTypeface(MyConfig.FONTTYPE_WAWA);
            //                break;
            R.id.tv_default -> {
                selectTypeface(MyConfig.FONTTYPE_DEFAULT)
                setTypeface(MyConfig.FONTTYPE_DEFAULT)
            }
            R.id.iv_bg_default -> {
                setBookBg(MyConfig.BOOK_BG_DEFAULT)
                selectBg(MyConfig.BOOK_BG_DEFAULT)
            }
            R.id.iv_bg_1 -> {
                setBookBg(MyConfig.BOOK_BG_1)
                selectBg(MyConfig.BOOK_BG_1)
            }
            R.id.iv_bg_2 -> {
                setBookBg(MyConfig.BOOK_BG_2)
                selectBg(MyConfig.BOOK_BG_2)
            }
            R.id.iv_bg_3 -> {
                setBookBg(MyConfig.BOOK_BG_3)
                selectBg(MyConfig.BOOK_BG_3)
            }
            R.id.iv_bg_4 -> {
                setBookBg(MyConfig.BOOK_BG_4)
                selectBg(MyConfig.BOOK_BG_4)
            }
        }
    }

    //变大书本字体
    private fun addFontSize() {
        if (currentFontSize < FONT_SIZE_MAX) {
            currentFontSize += 1
            tv_size!!.text = currentFontSize.toString() + ""
            MyConfig.instance.setFontSize(currentFontSize.toFloat())
            if (mSettingListener != null) {
                mSettingListener!!.changeFontSize(currentFontSize)
            }
        }
        fontSizeBg()
    }

    private fun fontSizeBg(){
        val defFontSize = context.resources.getDimension(R.dimen.reading_default_text_size).toInt()
        var size:Boolean = false
        if (defFontSize == currentFontSize)
            size = true
        setTextViewSelect(tv_size_default, size)
    }

    private fun defaultFontSize() {
        currentFontSize = context.resources.getDimension(R.dimen.reading_default_text_size).toInt()
        tv_size!!.text = currentFontSize.toString() + ""
        MyConfig.instance.setFontSize(currentFontSize.toFloat())
        if (mSettingListener != null) {
            mSettingListener!!.changeFontSize(currentFontSize)
        }
        fontSizeBg()
    }

    //变小书本字体
    private fun subtractFontSize() {
        if (currentFontSize > FONT_SIZE_MIN) {
            currentFontSize -= 1
            tv_size!!.text = currentFontSize.toString() + ""
            MyConfig.instance.setFontSize(currentFontSize.toFloat())
            if (mSettingListener != null) {
                mSettingListener!!.changeFontSize(currentFontSize)
            }
        }
        fontSizeBg()
    }

    //改变亮度
    fun changeBright(isSystem: Boolean?, brightness: Int) {
        val light = (brightness / 100.0).toFloat()
        setTextViewSelect(tv_xitong, isSystem!!)
        MyConfig.instance.setSystemLight(isSystem)
        MyConfig.instance.setLight(light)
        if (mSettingListener != null) {
            mSettingListener!!.changeSystemBright(isSystem, light)
        }
    }

    fun setSettingListener(settingListener: SettingListener) {
        this.mSettingListener = settingListener
    }

    interface SettingListener {
        fun changeSystemBright(isSystem: Boolean?, brightness: Float)

        fun changeFontSize(fontSize: Int)

        fun changeTypeFace(typeface: Typeface)

        fun changeBookBg(type: Int)
    }

}