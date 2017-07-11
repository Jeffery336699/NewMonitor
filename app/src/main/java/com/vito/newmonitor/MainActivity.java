package com.vito.newmonitor;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        startByNormallService();

    }

    private void initView() {
        mTv = (TextView) findViewById(R.id.tv);
        mTv.setText("自助终端监控 " + getVersionName());
    }

    public void startByNormallService() {
        Intent i = new Intent(getApplicationContext(), MonitorService.class);
        startService(i);
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
        Log.i(TAG, "getVersionName: " + versionName);
        return versionName;
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.d(TAG, "可见");
                SystemClock.sleep(5000);
                startApp("com.smates.selfservice");
                AlarmManagerUtils.startMonitor(getApplicationContext());
            }
        }.start();

    }

    protected void startApp(String packageName) {
        PackageManager pm = getPackageManager();
        Intent launchIntentForPackage = pm.getLaunchIntentForPackage(packageName);
        if (launchIntentForPackage != null)
            startActivity(launchIntentForPackage);
    }
}
