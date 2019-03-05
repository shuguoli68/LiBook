package cn.xinlian.kotlinweb.util

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import com.li.libook.R


class DialogUtil(){

    companion object {

        fun getDialog(mContext: Context, title: String, msg: String, dialogCall: MyDialogCall): Dialog? {
            val alert = AlertDialog.Builder(mContext).setTitle(title)
                    .setMessage(msg)
                    .setNegativeButton(mContext.getString(R.string.cancel)) { dialog, which -> dialogCall.cancel() }
                    .setPositiveButton(mContext.getString(R.string.ok)) { dialog, which -> dialogCall.ok() }
                    .create()             //创建AlertDialog对象
            if (alert != null && !alert.isShowing) {
                alert.show()
            }
            return alert
        }

        fun numProgress(mContext: Context, title: String, msg: String, max: Int): ProgressDialog {
            val mDialog: ProgressDialog = ProgressDialog(mContext, R.style.my_dialog)
            mDialog!!.setTitle(title)
            mDialog!!.setMessage(msg)
            mDialog!!.setMax(max)
            mDialog!!.setProgress(0)
            mDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            mDialog!!.setCanceledOnTouchOutside(false)
            mDialog!!.setCancelable(false)
//            mDialog!!.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getString(R.string.cancel), DialogInterface.OnClickListener { dialogInterface, i ->
//                if (mDialog!!.isShowing()) {
//                    mDialog!!.dismiss()
//                }
//            })
            if (!mDialog!!.isShowing()) {
                mDialog!!.show()
            }
            return mDialog
        }
    }

    interface MyDialogCall {
        fun cancel()
        fun ok()
    }

}