package com.li.libook.cockroach;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.li.libook.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CockroachUtil {
    //文件夹目录
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/LiBook/";
    //文件名
    private static final String FILE_NAME = "admin_crash_log";
    //文件名后缀
    private static final String FILE_NAME_SUFFIX = ".txt";


    public static void install(final Context mContext) {
        Cockroach.install(new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                Log.i("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", throwable);
                dumpExceptionToSDCard(mContext,throwable);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(mContext, "safe_mode_excep_tips", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                dumpExceptionToSDCard(mContext,throwable);
                throwable.printStackTrace();//打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
//                Toast.makeText(mContext, "Cockroach Worked", Toast.LENGTH_LONG).show();
                Log.i("AndroidRuntime", "Cockroach Worked" + throwable + "<---");
            }

            @Override
            protected void onEnterSafeMode() {
//                Toast.makeText(mContext, "onEnterSafeMode", Toast.LENGTH_LONG).show();

            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                dumpExceptionToSDCard(mContext,e);
                Thread thread = Looper.getMainLooper().getThread();
                Log.i("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", e);
                //黑屏时建议直接杀死app
//                sysExcepHandler.uncaughtException(thread, new RuntimeException("black screen"));
            }

        });
    }


    /**
     * 导出异常信息到SD卡
     */
    public static void dumpExceptionToSDCard(Context mContext, Throwable ex) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        //创建文件夹
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //获取当前时间
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        //以当前时间创建log文件
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            //输出流操作
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //导出手机信息和异常信息
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            pw.println("发生异常时间：" + time);
            pw.println("app："+ mContext.getString(R.string.app_name));
            pw.println("应用版本：" + pi.versionName);
            pw.println("应用版本号：" + pi.versionCode);
            pw.println("android版本号：" + Build.VERSION.RELEASE);
            pw.println("android版本号API：" + Build.VERSION.SDK_INT);
            pw.println("手机制造商:" + Build.MANUFACTURER);
            pw.println("手机型号：" + Build.MODEL);
            ex.printStackTrace(pw);
            //关闭输出流
            pw.close();
        } catch (Exception e) {

        }
    }
}
