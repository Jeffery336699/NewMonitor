package com.vito.newmonitor;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xk on 2016/11/14.
 */

public class BootReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private ActivityManager mManager;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION)){
            Log.i("vvv","我进来了吗 ");
            Intent mBootIntent = new Intent(context, MainActivity.class);
            //设置开机自动运行app
            mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mBootIntent);
        }

//        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
//            if (mManager == null) {
//                mManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
//            }
//            mManager.killBackgroundProcesses(MonitorService.PACKAGE_NAME);
//
//            Toast.makeText(context, "有应用被添加", Toast.LENGTH_LONG).show();
//        }

        if(intent.getAction().equals("com.vito.newmonitor")){
            AlarmManagerUtils.cancelMonitor(context);
            Toast.makeText(context, "接收暂停监控广播", Toast.LENGTH_LONG).show();
        }



    }

}
