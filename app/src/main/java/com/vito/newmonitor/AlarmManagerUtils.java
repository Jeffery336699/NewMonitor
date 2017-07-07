package com.vito.newmonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
/**
 * Created by xk on 2016/11/22.
 */

public class AlarmManagerUtils {

    private static AlarmManager am;

    public static AlarmManager getAlarmManager(Context ctx) {
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }



    //开始广告服务下载
    public static void startMonitor(Context ctx) {
        am = getAlarmManager(ctx);
        Intent i = new Intent(ctx, MonitorService.class);
        PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, i, 0);
        //TODO 修改时间间隔
        //当前系统时间 + 30秒 是为了等待USB设备完全打开后再去检测设备状态。
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+30 * 1000,  10 * 1000, pendingIntent);
    }
    public static void cancelMonitor(Context ctx) {
        AlarmManager am = getAlarmManager(ctx);
        Intent i = new Intent(ctx, MonitorService.class);
        PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, i, 0);
        am.cancel(pendingIntent);
    }


}


