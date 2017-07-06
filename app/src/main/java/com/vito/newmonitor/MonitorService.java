package com.vito.newmonitor;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.wenming.library.BackgroundUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MonitorService extends Service {

    private ActivityManager mManager;
    private Timer           timer;
    private List<String>    mList;
    private             boolean first        = true;
//    public final static String  PACKAGE_NAME = "com.example.ling.installtestdemo";
        public final static String PACKAGE_NAME="com.smates.selfservice";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //开启监控
//        startWatching();
        startCheck();
        setForeApp();
    }


    private void updateProcessInfo() {
        if (mManager == null) {
            mManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        }

        // 获取进程信息
        List<ActivityManager.RunningAppProcessInfo> infos = mManager
                .getRunningAppProcesses();
        mList = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            String name = info.processName;
            mList.add(name);
            Log.d("processName", name);
        }

        if (!mList.contains(PACKAGE_NAME)) {
            Log.d("processName", "不包含自助终端进程");

            startApp(PACKAGE_NAME);
        } else {
            //            if (first) {
            //                Log.d("processName", "我进来了,");
            //                //                mManager.killBackgroundProcesses(PACKAGE_NAME);
            //                startApp(PACKAGE_NAME);
            //            }
            //TODO:这里记得解开注释
            //            first = false;

            //判断自助终端是否在前台执行,没有在前台执行就启动
            //            Boolean isForeground = BackgroundUtil.getLinuxCoreInfo(getApplication().getApplicationContext(), PACKAGE_NAME);
            //            Log.i("jiance", "isForeground----: " + isForeground);
            //            if (!isForeground){
            //                startApp(PACKAGE_NAME);
            //            }
        }
    }


    private void startWatching() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                //getTime();
                updateProcessInfo();
            }

        };
        timer.schedule(task, 5000, 5 * 1000);

    }

    private void getTime() {
        long mainApptime = readTime();
        long currentTime = System.currentTimeMillis();
        Log.i("xiaohao", "mainApptime=" + mainApptime + "");
        Log.i("xiaohao", "currentTime=" + currentTime + "");
        Log.i("xiaohao", "rest=" + (currentTime - mainApptime) + "");
        if (currentTime - mainApptime > 10000) {
            startApp(PACKAGE_NAME);
        }
    }


    private long readTime() {
        try {

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + "run.txt");
            if (!file.exists()) {
                return 0;
            }
            StringBuffer sb = new StringBuffer();//ok

            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = "";
            if ((str = br.readLine()) != null) {
                sb.append(str);

                return Long.parseLong(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * 打开app
     *
     * @param packageName
     */
    protected void startApp(String packageName) {
        // intent
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
        //API level 11
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
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent localIntent = new Intent(getApplicationContext(), MonitorService.class);
        //销毁时重新启动Service
        this.startService(localIntent);
        Toast.makeText(getApplicationContext(), "打开成功", Toast.LENGTH_LONG).show();
        Log.i("xiaohao", "fuwu走到这了");
    }


    private void startCheck() {
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask task = new TimerTask() {
            public void run() {
                Boolean isForeground = BackgroundUtil.getLinuxCoreInfo(getApplicationContext(), PACKAGE_NAME);
                Log.i("jiance", "isForeground----: " + isForeground);
                if (!isForeground) {
                    startApp(PACKAGE_NAME);
                }
            }
        };
        timer.schedule(task, 5000, 20 * 1000);
        recomdTime();
    }

    private void recomdTime() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(mRunnable);
    }

    public Runnable mRunnable = new Runnable() {
        private int time;

        @Override
        public void run() {
            while (true) {
                Log.i("tangj", "time--jiance--" + time++);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
