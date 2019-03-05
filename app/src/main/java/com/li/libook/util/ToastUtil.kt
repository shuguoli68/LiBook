package cn.xinlian.kotlinweb.util

import android.content.Context
import android.widget.Toast

class ToastUtil private constructor() {


    init {
        throw UnsupportedOperationException("不能被实例化") as Throwable
    }

    //取消Toast显示
    fun cancelToast() {
        if (isShow && mToast != null) {
            mToast!!.cancel()
        }
    }

    companion object {
        private var isShow = true
        private var mToast: Toast? = null

        //全局控制是否显示Toast
        fun controlShow(ishowToat: Boolean) {
            isShow = ishowToat
        }


        //短时间显示Toast
        fun showShort(context: Context, message: CharSequence) {
            if (isShow) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                } else {
                    mToast!!.setText(message)
                }
                mToast!!.show()
            }
        }

        //短时间显示Toast,传入资源ID
        fun showShort(context: Context, resId: Int) {
            if (isShow) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT)
                } else {
                    mToast!!.setText(resId)
                }
                mToast!!.show()
            }

        }

        //长时显示Toast
        fun showLong(context: Context, message: CharSequence) {
            if (isShow) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, message, Toast.LENGTH_LONG)
                } else {
                    mToast!!.setText(message)
                }
                mToast!!.show()
            }
        }

        //长时间显示Toast
        fun showLong(context: Context, resId: Int) {
            if (isShow) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG)
                } else {
                    mToast!!.setText(resId)
                }
                mToast!!.show()
            }

        }


        //自定义显示的Toast时间
        fun show(context: Context, message: CharSequence, duration: Int) {
            if (isShow) {

                if (mToast == null) {
                    mToast = Toast.makeText(context, message, duration)
                } else {
                    mToast!!.setText(message)
                }
                mToast!!.show()
            }

        }

        //自定义显示时间
        fun show(context: Context, resId: Int, duration: Int) {

            if (isShow) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, resId, duration)
                } else {
                    mToast!!.setText(resId)
                }
                mToast!!.show()
            }
        }
    }

}
