package com.vito.newmonitor;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.wenming.library.BackgroundUtil;
import com.wenming.library.processutil.ProcessManager;
import com.wenming.library.processutil.models.AndroidAppProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MonitorService extends Service {

    private static String TAG="MonitorService";
    private ActivityManager mManager;
    private Timer           timer;
    private List<String>    mList;
    private             boolean first        = true;

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
        startCheck();
        setForeApp();
    }

    public  List<AndroidAppProcess> getRunningForegroundApps(Context ctx) {
        List<AndroidAppProcess> processes = new ArrayList<>();
        File[] files = new File("/proc").listFiles();
        PackageManager pm = ctx.getPackageManager();
        for (File file : files) {
            if (file.isDirectory()) {
                int pid;
                try {
                    pid = Integer.parseInt(file.getName());
                } catch (NumberFormatException e) {
                    continue;
                }
                try {
                    AndroidAppProcess process = new AndroidAppProcess(pid);
                    if (process.foreground
                            && (process.uid < 1000 || process.uid > 9999)
                            && !process.name.contains(":")
                            && pm.getLaunchIntentForPackage(process.getPackageName()) != null) {
                        processes.add(process);
                    }
                } catch (AndroidAppProcess.NotAndroidAppProcessException ignored) {
                } catch (IOException e) {
                    Log.e(TAG, pid+"");

                }
            }
        }
        return processes;
    }

    /**
     * 把process进程信息保存在/proc目录下，使用Shell命令去获取的他，再根据进程的属性判断是否为前台
     *
     * @param packageName 需要检查是否位于栈顶的App的包名
     */
    public  boolean getLinuxCoreInfo(Context context, String packageName) {

        List<AndroidAppProcess> processes = getRunningForegroundApps(context);
        for (AndroidAppProcess appProcess : processes) {
            if (appProcess.getPackageName().equals(packageName) && appProcess.foreground) {
                return true;
            }
        }
        return false;

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
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent localIntent = new Intent(getApplicationContext(), MonitorService.class);
        //销毁时重新启动Service
        this.startService(localIntent);
        Toast.makeText(getApplicationContext(), "打开成功", Toast.LENGTH_LONG).show();
        Log.d(TAG, "销毁重启服务");
    }


    private void startCheck() {
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask task = new TimerTask() {
            public void run() {
                Boolean isForeground = getLinuxCoreInfo(getApplicationContext(), PACKAGE_NAME);
                Log.i(TAG, "isForeground----: " + isForeground);
                if (!isForeground) {
                    startApp(PACKAGE_NAME);
                }
            }
        };
        timer.schedule(task, 50000, 20 * 1000);
    }

}
