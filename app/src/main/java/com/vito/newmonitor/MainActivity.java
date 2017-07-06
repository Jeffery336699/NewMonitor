package com.vito.newmonitor;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


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

        mTv.setText("自助终端监控 "+getVersionName());
    }

    public void startByNormallService() {
        Intent i = new Intent(getApplicationContext(), MonitorService.class);
        startService(i);
        Toast.makeText(getApplicationContext(), "打开成功", Toast.LENGTH_LONG).show();
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
}
