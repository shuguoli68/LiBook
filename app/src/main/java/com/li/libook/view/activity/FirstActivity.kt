package com.li.libook.view.activity

import android.Manifest
import android.os.CountDownTimer
import com.li.libook.MainActivity
import com.li.libook.R
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions

class FirstActivity(): BaseAcivity(){

    lateinit var rxPermissions: RxPermissions
    var pers  = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun getResourceId(): Int {
        return R.layout.activity_first
    }

    override fun initData() {
        rxPermissions = RxPermissions(this)
    }

    override fun initView() {

    }

    internal var countDownTimer: CountDownTimer? = object : CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            startTo(MainActivity::class.java)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        rxPermissions.request(*pers)
            .subscribe {
                if (it){
                    Logger.i("权限被允许")
                    countDownTimer?.start()
                }else{
                    Logger.i("权限被拒绝")
                }
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
        countDownTimer=null
    }

}