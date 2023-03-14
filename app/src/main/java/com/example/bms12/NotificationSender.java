package com.example.bms12;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class NotificationSender {
        Context context;
        public NotificationSender(Context c)
        {
            context=c;
        }
        public String CHANNEL_ID;
        public void createChannel()
        {
            CharSequence channelName=CHANNEL_ID;
            String channelDesc="channelDesc";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                int importance= NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel=new NotificationChannel(CHANNEL_ID,channelName,importance);
                channel.setDescription(channelDesc);
                NotificationManager notificationManager=context.getSystemService(NotificationManager.class);
                assert  notificationManager!=null;
                NotificationChannel currChannel=notificationManager.getNotificationChannel(CHANNEL_ID);
                if(currChannel==null)
                    notificationManager.createNotificationChannel(channel);
            }
        }
        public void sendNotification(String bms_id,String event,String geofence_id)
        {
            String message = null;
            if(event.equals("ENTERED"))
            {
                message=bms_id+" "+event+" in "+geofence_id+".";
            }
            else if(event.equals("EXITED"))
            {
                message=bms_id+" "+event+" from "+geofence_id+".";
            }

            CHANNEL_ID="YOSH1002";
//assert getActivity()!=null;
            createChannel();
            Log.d("yoho", "sendNotification: Insdie thhe method boiii");
            NotificationCompat.Builder builder=new NotificationCompat.Builder(context,CHANNEL_ID).
                    setSmallIcon(R.drawable.ic_message).
                    setContentTitle("GEOFENCE UPDATE").
                    setContentText(message).
                    setPriority(NotificationCompat.PRIORITY_DEFAULT).
                    setAutoCancel(true);

//        Intent intent = new Intent(this,Main2Activity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("message","yohohohhohohhhohoh");
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentIntent(pendingIntent);
            NotificationManagerCompat notificationManager=NotificationManagerCompat.from(context);
            int notificationId=(int)(System.currentTimeMillis()/4);
            notificationManager.notify(notificationId,builder.build());

//        NotificationManager notificationManager=(NotificationManager) getSystemService(
//                Context.NOTIFICATION_SERVICE);
//
//        //notificationManager.notify();
//        notificationManager.notify(1,builder.build());
        }


}
