package com.li.libook.util.book

import android.graphics.Bitmap
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.util.TypedValue
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import org.mozilla.universalchardet.UniversalDetector
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern


class CommonUtil(){

    companion object {
        val instance:CommonUtil = CommonUtil()
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     * @param context
     * @return
     */
    fun getDpi(context: Context): Int {
        var dpi = 0
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, displayMetrics)
            dpi = displayMetrics.heightPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dpi
    }

    /**
     * 获取 虚拟按键的高度
     * @param context
     * @return
     */
    fun getBottomStatusHeight(context: Context): Int {
        val totalHeight = getDpi(context)

        val contentHeight = getScreenHeight(context)

        return totalHeight - contentHeight
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * 标题栏高度
     * @return
     */
    fun getTitleHeight(activity: Activity): Int {
        return activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT).getTop()
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    fun getStatusHeight(context: Context): Int {

        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(
                clazz.getField("status_bar_height")
                    .get(`object`).toString()
            )
            statusHeight = context.getResources().getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    fun getAPIVersion(): Int {
        var APIVersion: Int
        try {
            APIVersion = Integer.valueOf(android.os.Build.VERSION.SDK)
        } catch (e: NumberFormatException) {
            APIVersion = 0
        }

        return APIVersion
    }

    /**
     *
     * @param context
     * @param px
     * @return
     */
    fun convertPixelsToDp(context: Context, px: Float): Float {
        return px / context.getResources().getDisplayMetrics().density
    }

    /**
     *
     * @param context
     * @param dp
     * @return
     */
    fun convertDpToPixel(context: Context, dp: Float): Float {
        return dp * context.getResources().getDisplayMetrics().density
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.getResources().getDisplayMetrics()
        ).toInt()
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    fun px2sp(context: Context, pxVal: Float): Float {
        return pxVal / context.getResources().getDisplayMetrics().scaledDensity
    }

    fun subString(text: String, num: Int): String {
        var content = ""
        if (text.length > num) {
            content = text.substring(0, num - 1) + "..."
        } else {
            content = text
        }

        return content
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */

    fun getVersion(context: Context): String {
        try {
            val manager = context.getPackageManager()
            val info = manager.getPackageInfo(context.getPackageName(), 0)
            return info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            return "找不到版本号"
        }

    }

    /**
     * @return 版本号
     */
    fun getVersionCode(context: Context): Int {
        try {
            val manager = context.getPackageManager()
            val info = manager.getPackageInfo(context.getPackageName(), 0)
            return info.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }

    }

    /*
    返回单个字符串，若匹配到多个的话就返回第一个
    如：String str = "abc3443abcfgjhgabcgfjabc";
        String rgex = "abc(.*?)abc";
        返回：3443
     */
    fun getSubUtilSimple(soap: String): String {
        val rgex = "第(.*?)章"
        val pattern = Pattern.compile(rgex)// 匹配的模式
        val m = pattern.matcher(soap)
        while (m.find()) {
            return m.group(1)
        }
        return "第0章"
    }

    @Throws(IOException::class)
    fun getCharset(fileName: String): String {
        val charset: String
        val fis = FileInputStream(fileName)
        val buf = ByteArray(4096)
        // (1)
        val detector = UniversalDetector(null)
        // (2)
        var nread: Int = 0
        while (nread !=-1 && !detector.isDone()) {
            nread = fis.read(buf)
            detector.handleData(buf, 0, nread)
        }
        // (3)
        detector.dataEnd()
        // (4)
        charset = detector.getDetectedCharset()
        // (5)
        detector.reset()
        return charset
    }

    //（需要显示的文字，上下文对象，背景图片）
    fun createTextBitmap(contents: String, context: Context, bitmapResource: Int): Bitmap {
        val scale = context.getResources().getDisplayMetrics().scaledDensity
        val tv = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tv.setPadding(scale.toInt() * 10, scale.toInt() * 15, scale.toInt() * 10, scale.toInt() * 15)//l,t,r,b
        tv.layoutParams = layoutParams
        tv.text = contents
        tv.textSize = scale * 6
        tv.gravity = Gravity.CENTER
        tv.isDrawingCacheEnabled = true
        tv.setTextColor(Color.WHITE)
        tv.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        tv.layout(0, 0, tv.measuredWidth, tv.measuredHeight)


        tv.setBackgroundResource(bitmapResource)

        tv.buildDrawingCache()
        return tv.drawingCache
    }

    fun writeTo(bookPath: String, content: String) {
        var content = content
        content = formatContent(content)
        try {
            /*File targetFile = new File(bookPath);
            //使用RandomAccessFile是在原有的文件基础之上追加内容，
            //而使用outputstream则是要先清空内容再写入
            RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
            //光标移到原始文件最后，再执行写入
            raf.seek(targetFile.length());
            raf.write(content.getBytes());
            raf.close();*/
            val fout = FileOutputStream(bookPath, true)//是否追加
            val bytes = content.toByteArray()
            fout.write(bytes)
            fout.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 格式化小说内容。
     *
     *
     *  * 小说的开头，缩进2格。在开始位置，加入2格空格。
     *  * 所有的段落，缩进2格。所有的\n,替换为2格空格。
     *
     */
    fun formatContent(str: String): String {
        var str = str
        str = str.replace("[ ]*".toRegex(), "")//替换来自服务器上的，特殊空格
        str = str.replace("[ ]*".toRegex(), "")//
        str = str.replace("\n\n", "\n")
        str = str.replace("\n", "\n" + "\u3000\u3000")
        str = "\u3000\u3000" + str
        //        str = convertToSBC(str);
        return str
    }

    /**
     * 路径中获取文件名称
     * @param path
     * @return
     */
    fun getFileName(path: String): String {
        val index = path.lastIndexOf("/")
        return path.substring(index + 1)
    }
}