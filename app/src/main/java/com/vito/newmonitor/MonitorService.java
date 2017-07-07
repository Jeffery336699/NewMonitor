package com.vito.newmonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.wenming.library.BackgroundUtil;
import com.wenming.library.processutil.models.AndroidAppProcess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorService extends Service {

    private static String TAG = "MonitorService";
    private Timer timer;
    public final static String PACKAGE_NAME = "com.smates.selfservice";
    public static final String monitorFile   = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "read.txt";
    public static final String START_MONITOR = "startMonitor";
    public static final String STOP_MONITOR  = "stopMonitor";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //开启监控

        setForeApp();
    }

    /**
     * 打开app
     *
     * @param packageName
     */
    protected void startApp(String packageName) {
        PackageManager pm = getPackageManager();
        Intent launchIntentForPackage = pm.getLaunchIntentForPackage(packageName);
        if (launchIntentForPackage != null)
            startActivity(launchIntentForPackage);
    }

    /**
     * 设置成前台进程
     */
    private void setForeApp() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle("monitor");
        builder.setContentText("监控进程");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.getNotification();
        manager.notify(R.mipmap.ic_launcher, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCheck();
        Log.i(TAG, "onStartCommand: -----------进来了");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
        super.onDestroy();
        stopSelf();
        Log.i(TAG, "销毁服务");

    }


    private void startCheck() {
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask task = new TimerTask() {
            public void run() {
                String s = FileUtil.readFile(monitorFile);
                Log.i(TAG, "monitorFile: "+s);
                if (STOP_MONITOR.equals(s)){//表示是操作人员退出的,不做一体机前后台监控
                    return;
                }
                Boolean isForeground = BackgroundUtil.getLinuxCoreInfo(getApplicationContext(), PACKAGE_NAME);
                Log.i(TAG, "isForeground----: " + isForeground);
                if (!isForeground) {
                    startApp(PACKAGE_NAME);
                }
            }
        };
        timer.schedule(task, 10*1000, 40 * 1000);
    }

}
