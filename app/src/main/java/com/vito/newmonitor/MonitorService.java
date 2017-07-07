package com.vito.newmonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.wenming.library.BackgroundUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MonitorService extends Service {

    private static String TAG = "MonitorService";
    private Timer timer;
    public final static String PACKAGE_NAME = "com.smates.selfservice";


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
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
        Log.i(TAG, "onStartCommand: -----------进来了");
        startCheck();
      //  return Service.START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
        stopSelf();
        Log.i(TAG, "销毁服务");
    }


    private void startCheck() {

        Boolean isForeground = BackgroundUtil.getLinuxCoreInfo(getApplicationContext(), PACKAGE_NAME);
        Log.i(TAG, "isForeground----: " + isForeground);
        if (!isForeground) {
            startApp(PACKAGE_NAME);
        }

    }

}
