package com.li.libook.model

import android.content.Context
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.li.libook.R
import android.graphics.Typeface
import android.os.Environment
import java.io.File
import android.content.SharedPreferences
import com.li.libook.App
import java.text.SimpleDateFormat
import java.util.*


class MyConfig() {
    // 静态常量
    companion object {

        val instance: MyConfig = MyConfig()

        val FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        const val LOCAL_LIST = "local_list"//传递LocalBean
        const val CODE_ONE = 1
        const val CODE_TWO = 2
        const val CODE_THREE = 3
        const val CODE_FOUR = 4
        const val CODE_FIVE = 5

        //Activity 传数据
        val EXTRA_BOOK = "extra_book"
        val BOOK_CAT = "book_cat"
        val BOOK_ID = "book_id"
        val RESOURCE_ID = "resource_id"
        val RESOURCE_NAME = "resource_name"
        val GENDER = "gender"
        val MAJOR = "major"
        val RANKINGID = "ranking_id"

        val ROOT_LOCAL_PATH =
            Environment.getExternalStorageDirectory().getPath() + File.separator + "BookApp" + File.separator + "Local"
        val ROOT_ONLINE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "BookApp" + File.separator + "Online"
        val ROOT_CACHE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "BookApp" + File.separator + "Cache"

        val NOW_NAME = "now_book_path"

        val JSON_FORM_DATA = "application/json; charset=utf-8"
        //翻页模式
        val PAGE_MODE_SIMULATION = 0
        val PAGE_MODE_COVER = 1
        val PAGE_MODE_SLIDE = 2
        val PAGE_MODE_NONE = 3
        //背景
        val BOOK_BG_DEFAULT = 0
        val BOOK_BG_1 = 1
        val BOOK_BG_2 = 2
        val BOOK_BG_3 = 3
        val BOOK_BG_4 = 4

        private val SP_NAME = "read_book"
        private val sp: SharedPreferences? = App.instance.getSharedPreferences(SP_NAME, Context.MODE_MULTI_PROCESS)
        //字体
        var typeface: Typeface? = null
        //字体大小
        var mFontSize = 0.0f
        //亮度值
        var light = 0.0f
        val bookBG: Int = 0
        val dayOrNight: Boolean = false
        val type: Int = BOOK_BG_DEFAULT

        private val BOOK_BG_KEY = "bookbg"
        private val FONT_TYPE_KEY = "fonttype"
        private val FONT_SIZE_KEY = "fontsize"
        private val NIGHT_KEY = "night"
        private val LIGHT_KEY = "light"
        private val SYSTEM_LIGHT_KEY = "systemlight"
        private val PAGE_MODE_KEY = "pagemode"

        val FONTTYPE_DEFAULT = ""
        val FONTTYPE_QIHEI = "font/qihei.ttf"
        val FONTTYPE_WAWA = "font/font1.ttf"

        val FONTTYPE_FZXINGHEI = "font/fzxinghei.ttf"
        val FONTTYPE_FZKATONG = "font/fzkatong.ttf"
        val FONTTYPE_BYSONG = "font/bysong.ttf"


        val options = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)  //加载成功之前占位图
            // .error(R.mipmap.ic_launcher)    //加载错误之后的错误图
            // .override(400,400)  //指定图片的尺寸
            //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
            .fitCenter()
            //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
            .centerCrop()
//            .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
            .skipMemoryCache(true)  //跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存所有版本的图像
            .diskCacheStrategy(DiskCacheStrategy.NONE)  //跳过磁盘缓存
            .diskCacheStrategy(DiskCacheStrategy.DATA)  //只缓存原来分辨率的图片
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)  //只缓存最终的图片
        val cicleOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher_round)  //加载成功之前占位图
            // .error(R.mipmap.ic_launcher)    //加载错误之后的错误图
            // .override(400,400)  //指定图片的尺寸
            //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
            .fitCenter()
            //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
            .centerCrop()
            .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
            .skipMemoryCache(true)  //跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存所有版本的图像
            .diskCacheStrategy(DiskCacheStrategy.NONE)  //跳过磁盘缓存
            .diskCacheStrategy(DiskCacheStrategy.DATA)  //只缓存原来分辨率的图片
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)  //只缓存最终的图片
    }

    fun getPageMode(): Int {
        return sp!!.getInt(PAGE_MODE_KEY, PAGE_MODE_SIMULATION)
    }

    fun setPageMode(pageMode: Int) {
        sp!!.edit().putInt(PAGE_MODE_KEY, pageMode).commit()
    }

    fun getBookBgType(): Int {
        return sp!!.getInt(BOOK_BG_KEY, BOOK_BG_DEFAULT)
    }

    fun setBookBg(type: Int) {
        sp!!.edit().putInt(BOOK_BG_KEY, type).commit()
    }

    fun getTypeface(): Typeface {
        if (typeface == null) {
            val typePath = sp!!.getString(FONT_TYPE_KEY, FONTTYPE_QIHEI)
            typeface = getTypeface(typePath)
        }
        return typeface!!
    }

    fun getTypefacePath(): String {
        return sp!!.getString(FONT_TYPE_KEY, FONTTYPE_QIHEI)
    }

    fun getTypeface(typeFacePath: String): Typeface {
        val mTypeface: Typeface
        if (typeFacePath == FONTTYPE_DEFAULT) {
            mTypeface = Typeface.DEFAULT
        } else {
            mTypeface = Typeface.createFromAsset(App.instance.getAssets(), typeFacePath)
        }
        return mTypeface
    }

    fun setTypeface(typefacePath: String) {
        typeface = getTypeface(typefacePath)
        sp!!.edit().putString(FONT_TYPE_KEY, typefacePath).commit()
    }

    fun getFontSize(): Float {
        if (mFontSize > -0.000001f && mFontSize < 0.000001f) {
            mFontSize = sp!!.getFloat(
                FONT_SIZE_KEY,
                App.instance.getResources().getDimension(R.dimen.reading_default_text_size)
            )
        }
        return mFontSize
    }

    fun setFontSize(fontSize: Float) {
        mFontSize = fontSize
        sp!!.edit().putFloat(FONT_SIZE_KEY, fontSize).commit()
    }

    /**
     * 获取夜间还是白天阅读模式,true为夜晚，false为白天
     */
    fun getDayOrNight(): Boolean {
        return sp!!.getBoolean(NIGHT_KEY, false)
    }

    fun setDayOrNight(isNight: Boolean) {
        sp!!.edit().putBoolean(NIGHT_KEY, isNight).commit()
    }

    fun isSystemLight(): Boolean? {
        return sp!!.getBoolean(SYSTEM_LIGHT_KEY, true)
    }

    fun setSystemLight(isSystemLight: Boolean?) {
        sp!!.edit().putBoolean(SYSTEM_LIGHT_KEY, isSystemLight!!).commit()
    }

    fun getLight(): Float {
        if (light > -0.000001f && light < 0.000001f) {
            light = sp!!.getFloat(LIGHT_KEY, 0.1f)
        }
        return light
    }

    /**
     * 记录配置文件中亮度值
     */
    fun setLight(lig: Float) {
        light = lig
        sp!!.edit().putFloat(LIGHT_KEY, light).commit()
    }
}