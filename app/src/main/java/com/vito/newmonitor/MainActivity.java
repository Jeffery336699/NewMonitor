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
    private ActivityManager mManager;
    private TextView mTv;
    private AppCompatButton mBt_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Log.i(TAG, "onCreate: xiaomei");
        Log.i(TAG, "onCreate: tangj");
        if (mManager == null) {
            mManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        }
        mManager.killBackgroundProcesses(MonitorService.PACKAGE_NAME);

        startByNormallService();

    }

    private void initView() {
        mTv = (TextView) findViewById(R.id.tv);
        mBt_other = (AppCompatButton) findViewById(R.id.bt_other);
        mBt_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();

            }
        });
        mTv.setText("自助终端监控"+getVersionName());
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
