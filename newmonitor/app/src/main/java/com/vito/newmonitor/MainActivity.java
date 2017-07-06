package com.vito.newmonitor;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private ActivityManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mManager == null) {
            mManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        }
        mManager.killBackgroundProcesses("com.smates.selfservice");

        startByNormallService();

    }

    public void startByNormallService() {
        Intent i = new Intent(getApplicationContext(), MonitorService.class);
        startService(i);
        Toast.makeText(getApplicationContext(), "打开成功", Toast.LENGTH_LONG).show();
    }
}
