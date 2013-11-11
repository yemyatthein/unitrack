package study.ms.bonn.lab.qs;

import java.text.SimpleDateFormat;
import java.util.Date;

import study.ms.bonn.lab.qs.utility.DBAdapter;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RemindRouterActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createGUI();
    }
	
	private void createGUI(){
		//NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//notificationManager.cancel(0);
		MyBroadcastReceiver.counter = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String curentDateandTime = sdf.format(new Date());
        int hour = 2;//Integer.parseInt(curentDateandTime.split(":")[0]);
        int min = 0;//Integer.parseInt(curentDateandTime.split(":")[1]);
        int fixedMin = 0;
        if(min<35 && min>25){
        	fixedMin = 30;
        }
		
		LinearLayout layout = new LinearLayout(this);
    	layout.setOrientation(LinearLayout.VERTICAL);
    	layout.setBackgroundResource(R.drawable.back);
    	LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
    	
    	TextView t1 = new TextView(this);
    	t1.setText("The following apps reminded you NOW.");
    	t1.setTextColor(Color.BLACK);
    	LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
    	pm.setMargins(10, 10, 10, 10);
        t1.setLayoutParams(pm);
    	layout.addView(t1);
    	
		DBAdapter db = new DBAdapter(this);
    	db.open();
    	Cursor c = db.queryDatabase("SELECT tt.track_id, tt.name, tt.about FROM tbl_remind as tr, tbl_trackers as tt " +
    			"WHERE tt.track_id=tr.fk_track_id AND rm_hour='" + hour + "' AND rm_min='" + fixedMin + "'");
		if(c.moveToFirst()){
			do{
				Button bt = new Button(this);
				bt.setText(c.getString(1));
		        bt.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		        final int trackerId = Integer.parseInt(c.getString(0));
		        final String info = c.getString(2);
		        
		        bt.setOnClickListener(new View.OnClickListener() {
    				public void onClick(View v) {
    					Intent intent = new Intent("study.ms.bonn.lab.qs.TRACKER_START");
    					Bundle extras = new Bundle();
    					extras.putInt("trackerId", trackerId);
    					extras.putString("info", info);
    					intent.putExtras(extras);
    	        		startActivity(intent);
					}
				});
		        
		        layout.addView(bt, params);
			} while(c.moveToNext());
		}
		
		View line = new View(this);
		LinearLayout.LayoutParams pm2 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 2);
		pm2.setMargins(5, 10, 5, 10);
		line.setLayoutParams(pm2);
		line.setBackgroundColor(Color.RED);
		layout.addView(line);
		
		
		TextView tOld = new TextView(this);
		tOld.setText("The following apps reminded you before.");
		tOld.setTextColor(Color.BLACK);
    	pm = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
    	pm.setMargins(10, 10, 10, 10);
    	tOld.setLayoutParams(pm);
    	layout.addView(tOld);
		
		/////Old ones/////
		c = db.queryDatabase("SELECT tt.track_id, tt.name, tt.about " +
				"FROM tbl_remind as tr, tbl_trackers as tt, tbl_notify as tn " +
    			"WHERE tt.track_id=tr.fk_track_id AND tr.remind_id=tn.fk_remind_id");
		if(c.moveToFirst()){
			do{
				Button bt = new Button(this);
				bt.setText("Old-" + c.getString(1));
		        bt.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		        final int trackerId = Integer.parseInt(c.getString(0));
		        final String info = c.getString(2);
		        
		        bt.setOnClickListener(new View.OnClickListener() {
    				public void onClick(View v) {
    					Intent intent = new Intent("study.ms.bonn.lab.qs.TRACKER_START");
    					Bundle extras = new Bundle();
    					extras.putInt("trackerId", trackerId);
    					extras.putString("info", info);
    					intent.putExtras(extras);
    	        		startActivity(intent);
					}
				});
		        
		        layout.addView(bt, params);
			} while(c.moveToNext());
		}
		
    	db.close();
    	
    	addContentView(layout, params);
    	
	}
}
