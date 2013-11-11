package study.ms.bonn.lab.qs;

import java.text.SimpleDateFormat;
import java.util.Date;

import study.ms.bonn.lab.qs.utility.DBAdapter;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
	public static int counter = 0;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent i = new Intent(context, RemindRouterActivity.class);
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String curentDateandTime = sdf.format(new Date());
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		String day = sdf.format(new Date());
		day = day.split("/")[0];
		
        int hour = Integer.parseInt(curentDateandTime.split(":")[0]);
        int min = Integer.parseInt(curentDateandTime.split(":")[1]);
        int fixedMin = 0;
        if(min<35 && min>25){
        	fixedMin = 30;
        }
		boolean shouldRemind = false;
		DBAdapter db = new DBAdapter(context);
    	db.open();
    	Cursor c = db.queryDatabase("SELECT tt.track_id, tr.only_on FROM tbl_remind as tr, tbl_trackers as tt " +
    			"WHERE tt.track_id=tr.fk_track_id AND rm_hour='" + hour + "' AND rm_min='" + fixedMin + "'");
		if(c.moveToFirst()){
			do{
				String onlyOn = c.getString(1).trim();
				if(onlyOn.equals("all_days")){
					shouldRemind = true;
					//break;
				}
				if(onlyOn.contains(day)){
					shouldRemind = true;
					//break;
				}
				String reminderItemId = c.getString(0).trim();
				db.execute("INSERT INTO tbl_notify VALUES(NULL, " + reminderItemId + ")");
			} while(c.moveToNext());
		}
    	db.close();
		
    	if(shouldRemind){
		    //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    //context.startActivity(i);
    		
    		
    		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(
					R.drawable.ic_main, "A new notification",
					System.currentTimeMillis());
			// Hide the notification after its selected
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			Intent intr = new Intent(context, RemindRouterActivity.class);
			PendingIntent activity = PendingIntent.getActivity(context, 0,
					intr, 0);
			notification.setLatestEventInfo(context, "Reminder",
					"Universal Tracker Notification!", activity);
			notification.number = ++counter;
			notificationManager.notify(0, notification);

    	}
		//Toast.makeText(context, "Don't panik but your time is up!!!!.", Toast.LENGTH_LONG).show();
		// Vibrate the mobile phone
		//Vibrator vibrator = (Vibrator) context
		//		.getSystemService(Context.VIBRATOR_SERVICE);
		//vibrator.vibrate(2000);
	}

}