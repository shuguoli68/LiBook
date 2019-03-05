package com.li.libook.util

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.li.libook.App
import com.li.libook.App.Companion
import com.li.libook.util.SPUtil.Companion.PREF_NAME


class SPUtil {

    val preferences: SharedPreferences
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        get() = App.instance.getSharedPreferences(
            PREF_NAME,
            Context.MODE_MULTI_PROCESS
        )

    operator fun set(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        apply(editor)
    }

    operator fun set(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        apply(editor)
    }

    operator fun set(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        apply(editor)
    }

    operator fun get(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    operator fun get(key: String, defValue: String): String? {
        return preferences.getString(key, defValue)
    }

    operator fun get(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    companion object {

        private val PREF_NAME = "libook"
        private var spUtil: SPUtil? = null
        private var sIsAtLeastGB: Boolean = false

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                sIsAtLeastGB = true
            }
        }

        val instance: SPUtil
            get() {
                if (spUtil == null) {
                    synchronized(SPUtil::class.java) {
                        if (spUtil == null) {
                            spUtil = SPUtil()
                        }
                    }
                }
                return spUtil!!
            }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        private fun apply(editor: SharedPreferences.Editor) {
            if (sIsAtLeastGB) {
                editor.apply()
            } else {
                editor.commit()
            }
        }
    }
}
