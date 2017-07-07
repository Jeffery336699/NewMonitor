package com.vito.newmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
        getMessage(this);
    }

    private void initView() {
        mTv = (TextView) findViewById(R.id.tv);
        mTv.setText("自助终端监控 "+getVersionName());
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
