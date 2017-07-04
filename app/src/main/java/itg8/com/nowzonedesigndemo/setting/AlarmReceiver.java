package itg8.com.nowzonedesigndemo.setting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath.BreathHistoryActivity;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.SharePrefrancClass;
import itg8.com.nowzonedesigndemo.setting.notification.AlarmNotification;
import itg8.com.nowzonedesigndemo.setting.notification.AlarmNotificationAdapter;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int ALARM_ID = 1234;
    private int requestCode=123;
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
         if(intent.hasExtra(CommonMethod.ALARM_FROMTIMEPICKER))
         {
              long startTime = SharePrefrancClass.getInstance(context).getLPref(CommonMethod.START_ALARM_TIME);
              long endTime = SharePrefrancClass.getInstance(context).getLPref(CommonMethod.END_ALARM_TIME);

             String time = SharePrefrancClass.getInstance(context).getPref(CommonMethod.SAVEALARMTIME);
             Intent intents = new Intent(context, AlarmNotification.class);
             intents.putExtra(CommonMethod.SAVEALARMTIME, time);
             intents.putExtra(CommonMethod.START_ALARM_TIME, startTime);
             intents.putExtra(CommonMethod.END_ALARM_TIME, endTime);


             PendingIntent pendingIntent = PendingIntent.getActivity(context,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.item_rv_alarm_notification);
             remoteView.setTextViewText(R.id.txt_app_name,"NowZone");
             remoteView.setTextViewText(R.id.txt_alarmStarted,"Alarm Started");
             remoteView.setTextViewText(R.id.txt_time,time);
             remoteView.setTextViewText(R.id.txt_hours,calculateHours(startTime, endTime));
             remoteView.setOnClickPendingIntent(R.id.img_close,createSelfPendingIntent(context));

             Notification noti = new Notification.Builder(context)
                     .setAutoCancel(false)
                     .setOngoing(true)
                     .setSmallIcon(R.drawable.ic_alarm)
                     .setContentIntent(pendingIntent)
                     .setCustomContentView(remoteView).build();

             notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
             // hide the notification after its selected

             notificationManager.notify(ALARM_ID, noti);


         }else if (intent.hasExtra(CommonMethod.END_ALARM_TIME))
         {
             notificationManager.cancel(ALARM_ID);
         }

    }

    private PendingIntent createSelfPendingIntent(Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(CommonMethod.ACTION_ALARM_NOTIFICATION);
        intent.putExtra(CommonMethod.END_ALARM_TIME,true);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private CharSequence calculateHours(long startTime, long endTime) {
        String hourses;
        long secs = (endTime - startTime) / 1000;
        int hours = (int) (secs / 3600);
        secs = secs % 3600;
        int mins = (int) (secs / 60);

        hourses = hours+":"+mins;
        return hourses;
    }
}
