package com.vito.newmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mTv;
    private Timer timer;
    private File mFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFile();
        initView();
        startByNormallService();
        getMessage(this);
    }

    private void initFile() {
        try {
            mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "read.txt");
            if (!mFile.exists())
                mFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mTv = (TextView) findViewById(R.id.tv);
        mTv.setText("自助终端监控 "+getVersionName());
    }

    public void startByNormallService() {
        final Intent i = new Intent(getApplicationContext(), MonitorService.class);
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask task = new TimerTask() {
            public void run() {
                String result = FileUtils.read(mFile.getAbsolutePath());
                if ("true".equals(result.trim())){
                    stopService(i);
                }else {
                    startService(i);
                }
            }
        };
        timer.schedule(task, 20*1000, 60 * 1000);
    }

    public String getVersionName() {
        String versionName = null;
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = null;
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getVersionName: "+versionName);
        return versionName;
    }

    /**
     * 查询手机内非系统应用
     * @param context
     * @return
     */
    public AppBean getMessage(Context context) {
        PackageManager pManager = context.getPackageManager();
        AppBean appBean = new AppBean();
        try {
            PackageInfo packageInfo = pManager.getPackageInfo(MonitorService.PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS);
            Log.i(TAG, "getMessage: ----versionCode"+packageInfo.versionCode+"----versionName:"+packageInfo.versionName+"-------packageName:"+packageInfo.packageName);
            appBean.setVersionCode(packageInfo.versionCode);
            appBean.setVersionName(packageInfo.versionName);
            appBean.setPackageName(packageInfo.packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appBean;
    }

}
