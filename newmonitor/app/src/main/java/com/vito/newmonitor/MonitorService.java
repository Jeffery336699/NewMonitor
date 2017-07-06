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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorService extends Service {

    private ActivityManager mManager;
    private Timer timer;
    private List<String> mList;
    private boolean first = true;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        super.onCreate();
        //开启监控
        startWatching();
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

        if (!mList.contains("com.smates.selfservice")) {
            Log.d("processName", "不包含自助终端进程");

            startApp("com.smates.selfservice");
        } else {
            if (first) {
                Log.d("processName", "我进来了,");
                mManager.killBackgroundProcesses("com.smates.selfservice");
                startApp("com.smates.selfservice");
            }
            first = false;

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
        timer.schedule(task, 5000, 10000);
    }

    private void getTime() {
        long mainApptime = readTime();
        long currentTime = System.currentTimeMillis();
        Log.i("xiaohao", "mainApptime=" + mainApptime + "");
        Log.i("xiaohao", "currentTime=" + currentTime + "");
        Log.i("xiaohao", "rest=" + (currentTime - mainApptime) + "");
        if (currentTime - mainApptime > 10000) {
            startApp("com.smates.selfservice");
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
}
