package study.ms.bonn.lab.qs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.content.DialogInterface;
import android.content.Intent;
import study.ms.bonn.lab.qs.utility.DBAdapter;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class RemindTimeSetterActivity extends Activity {
	CharSequence[] options = new CharSequence[48];
	boolean[] selections = new boolean[options.length];
	ArrayList<String> created = new ArrayList<String>();

	@Override
	protected void onResume() {
		super.onResume();
		createGUI();
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.remind_time_setter);

		createGUI();

	}

	public void createGUI() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.back);

		Bundle extras = getIntent().getExtras();
		final int trackId = extras.getInt("trackId");
		String type = extras.getString("type");
		final String info = extras.getString("info");

		LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		if (type.equals("init")) {

			LinearLayout.LayoutParams pm2 = new LinearLayout.LayoutParams(
					100,
					100);
	    	pm2.setMargins(30, 10, 0, 10);
	    	ImageButton btnMain = new ImageButton(this);
	    	btnMain.setImageResource(R.drawable.e);
	    	btnMain.setLayoutParams(pm2);
	    	btnMain.setScaleType(ScaleType.FIT_CENTER);
	        btnMain.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		Intent intent = new Intent("study.ms.bonn.lab.qs.TRACKER_START");
					Bundle extras = new Bundle();
					extras.putInt("trackerId", trackId);
					extras.putString("info", info);
					intent.putExtras(extras);
	        		startActivity(intent);
				}
			});
	        //layout.addView(btnMain);
	        
	        LinearLayout.LayoutParams pm4 = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
	    	pm4.setMargins(5, 10, 5, 20);
	        LinearLayout layoutIn = new LinearLayout(this);
	    	layoutIn.setOrientation(LinearLayout.VERTICAL);
	    	layoutIn.setBackgroundColor(Color.parseColor("#B06749"));
	    	layoutIn.setLayoutParams(pm4);
	    	layoutIn.addView(btnMain);
	    	layout.addView(layoutIn);
			
			
			LinearLayout.LayoutParams pm3 = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
	    	pm3.setMargins(5, 10, 5, 20);
			TextView t = new TextView(this);
			t.setBackgroundColor(Color.WHITE);
			t.setPadding(8, 8, 8, 8);
			t.setText("No reminder is currently set.");
			t.setLayoutParams(pm3);
			t.setTextColor(Color.BLACK);

			boolean delNeeded = false;
			DBAdapter db = new DBAdapter(this);
			db.open();
			Cursor c = db
					.queryDatabase("SELECT * FROM tbl_remind WHERE fk_track_id='"
							+ trackId + "'");
			if (c.moveToFirst()) {
				if (c.getString(6).trim().equals("all_days")) {
					t.setText("Everyday at:\n");
				} else {
					t.setText("Only on (" + c.getString(6) + "):\n");
				}
				delNeeded = true;
				do {
					String newInfo = doubleDigitString(c.getInt(2)) + ":"
							+ doubleDigitString(c.getInt(3));
					t.setText(t.getText() + newInfo + "\n");
				} while (c.moveToNext());
			}
			db.close();

			layout.addView(t);

			Button btn1 = (Button) new Button(this);
			btn1.setText("Periodic Timer");
			btn1.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layout.addView(btn1, params);
			btn1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(
							"study.ms.bonn.lab.qs.REMIND_TIME");
					Bundle extras = new Bundle();
					extras.putInt("trackId", trackId);
					extras.putString("type", "periodic_start");
					extras.putString("info", info);
					intent.putExtras(extras);
					startActivity(intent);
				}
			});

			Button btn2 = (Button) new Button(this);
			btn2.setText("Custom Hours (Daily)");
			btn2.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layout.addView(btn2, params);
			btn2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(
							"study.ms.bonn.lab.qs.REMIND_TIME");
					Bundle extras = new Bundle();
					extras.putInt("trackId", trackId);
					extras.putString("type", "custom_daily");
					extras.putString("info", info);
					intent.putExtras(extras);
					startActivity(intent);
				}
			});

			Button btn3 = (Button) new Button(this);
			btn3.setText("Constrain On Time");
			btn3.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layout.addView(btn3, params);
			btn3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(
							"study.ms.bonn.lab.qs.REMIND_CONSTRAINT");
					Bundle extras = new Bundle();
					extras.putInt("trackId", trackId);
					extras.putString("type", "periodic_start");
					intent.putExtras(extras);
					startActivity(intent);
				}
			});

			Button btn4 = (Button) new Button(this);
			btn4.setText("Remove all reminders");
			btn4.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			if (delNeeded) {
				layout.addView(btn4, params);
			}
			btn4.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					removeAndCancelAlarms(trackId);
					
					Intent intent = new Intent(
							"study.ms.bonn.lab.qs.REMIND_TIME");
					Bundle extras = new Bundle();
					extras.putInt("trackId", trackId);
					extras.putString("type", "init");
					extras.putString("info", info);
					intent.putExtras(extras);
					startActivity(intent);

				}
			});

			Button btnSim1 = (Button) new Button(this);
			btnSim1.setText("Simulate Notification");
			btnSim1.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layout.addView(btnSim1, params);
			btnSim1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// startActivity(new
					// Intent("study.ms.bonn.lab.qs.ROUTE_REMIND"));
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					Notification notification = new Notification(
							R.drawable.ic_main, "A new notification",
							System.currentTimeMillis());
					// Hide the notification after its selected
					notification.flags |= Notification.FLAG_AUTO_CANCEL;

					Intent intent = new Intent(getBaseContext(), RemindRouterActivity.class);
					PendingIntent activity = PendingIntent.getActivity(getBaseContext(), 0,
							intent, 0);
					notification.setLatestEventInfo(getBaseContext(), "Reminder",
							"Reminder App Notification!", activity);
					notification.number = ++MyBroadcastReceiver.counter;
					notificationManager.notify(0, notification);

				}
			});
			
			
			//sssss
			
		} else if (type.equals("periodic_start")) {
//			TextView tvLabel1 = new TextView(this);
//			tvLabel1.setText("Length of Period:");
//			tvLabel1.setTextColor(Color.BLACK);
//			layout.addView(tvLabel1);

			LinearLayout.LayoutParams pm2 = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
	    	pm2.setMargins(0, 10, 0, 0);
			final Spinner spinner1 = new Spinner(this);
			spinner1.setLayoutParams(pm2);
			spinner1.setId(1);
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
					this, android.R.layout.simple_spinner_item, new String[] {
							"Daily", "Every 6 Hours" });
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner1.setAdapter(adapter);
			layout.addView(spinner1);

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	        String curentTime = sdf.format(new Date());
	        int hour = Integer.parseInt(curentTime.split(":")[0]);
			String min = "00";
			if(Integer.parseInt(curentTime.split(":")[1])>30){
				hour++;
			}
			else{
				min = "30";
			}
			
			LinearLayout.LayoutParams pm3 = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
	    	pm3.setMargins(15, 10, 5, 10);
	        final String timeString = hour + ":" + min;
			final TextView tvLabel2 = new TextView(this);
			tvLabel2.setTextColor(Color.BLACK);
			tvLabel2.setText("At: " + timeString);
			tvLabel2.setLayoutParams(pm3);
			layout.addView(tvLabel2);

			/*
			String[] itemData = new String[48];
			int j = 0;
			for (int i = 0; i < 24; i++) {
				itemData[j] = i + ":" + "00";
				j++;
				itemData[j] = i + ":" + "30";
				j++;
			}

			final Spinner spinner2 = new Spinner(this);
			spinner2.setId(2);
			spinner2.setLayoutParams(new LinearLayout.LayoutParams(250,
					LayoutParams.WRAP_CONTENT));
			adapter = new ArrayAdapter<CharSequence>(this,
					android.R.layout.simple_spinner_item, itemData);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner2.setAdapter(adapter);
			layout.addView(spinner2);
			*/
			
			Button btnSave = (Button) new Button(this);
			btnSave.setText("Save");
			btnSave.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layout.addView(btnSave, params);
			btnSave.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					
					removeAndCancelAlarms(trackId);
					
					String val1 = spinner1.getSelectedItem().toString();
					//String val2 = spinner2.getSelectedItem().toString();
					String[] tokens = new String[2];
					tokens[0] = timeString.split(":")[0].trim();
					tokens[1] = timeString.split(":")[1].trim();
					
					DBAdapter db = new DBAdapter(getBaseContext());
					db.open();
					if (val1.equals("Daily")) {
						dbRemindInserter(db, Integer.parseInt(tokens[0]),
								Integer.parseInt(tokens[1]), trackId);
					} else if (val1.equals("Every 6 Hours")) {
						int hour = Integer.parseInt(tokens[0]);
						int min = Integer.parseInt(tokens[1]);
						dbRemindInserter(db, hour, min, trackId);

						int nextHour = (hour + 6) % 24;
						dbRemindInserter(db, nextHour, min, trackId);
						nextHour = (nextHour + 6) % 24;
						dbRemindInserter(db, nextHour, min, trackId);
						nextHour = (nextHour + 6) % 24;
						dbRemindInserter(db, nextHour, min, trackId);
					}
					db.close();
					
					Intent intent = new Intent(
							"study.ms.bonn.lab.qs.REMIND_TIME");
					Bundle extras = new Bundle();
					extras.putInt("trackId", trackId);
					extras.putString("type", "init");
					extras.putString("info", info);
					intent.putExtras(extras);
					startActivity(intent);
					// Toast.makeText(getBaseContext(), "OK Saved => " + val1 +
					// val2, Toast.LENGTH_SHORT).show();
				}
			});
		} else if (type.equals("custom_daily")) {
			LinearLayout.LayoutParams pm3 = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
	    	pm3.setMargins(0, 20, 0, 0);
	    	
			Button btnTimeView = (Button) new Button(this);
			btnTimeView.setText("Select Times");
			btnTimeView.setLayoutParams(pm3);
			
			DBAdapter db = new DBAdapter(getBaseContext());
	    	db.open();
	    	Cursor c = db.queryDatabase("SELECT * FROM tbl_remind WHERE fk_track_id='"+trackId+"'");
	    	Log.e("PPP", "fff");
	    	if(c.moveToFirst()){
				do{
					String hour = c.getString(2);
					int min = c.getInt(3);
					String minStr = min + "";
					if(min == 0){
						minStr = "00";
					}
					
					created.add(hour + ":" + minStr);
				} while(c.moveToNext());
			}
	    	db.close();
			
			btnTimeView.setOnClickListener(new ButtonClickHandler());
			layout.addView(btnTimeView);

			Button btnSave = (Button) new Button(this);
			btnSave.setText("Save");
			btnSave.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layout.addView(btnSave, params);
			btnSave.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					removeAndCancelAlarms(trackId);
					
					DBAdapter db = new DBAdapter(getBaseContext());
					db.open();
					for (int i = 0; i < options.length; i++) {
						if (selections[i]) {
							String[] tokens = options[i].toString().split(":");
							dbRemindInserter(db, Integer.parseInt(tokens[0]),
									Integer.parseInt(tokens[1]), trackId);
						}
					}
					db.close();
					
					Intent intent = new Intent(
							"study.ms.bonn.lab.qs.REMIND_TIME");
					Bundle extras = new Bundle();
					extras.putInt("trackId", trackId);
					extras.putString("type", "init");
					extras.putString("info", info);
					intent.putExtras(extras);
					startActivity(intent);
				}
			});
		}
		addContentView(layout, params);
	}

	private void dbRemindInserter(DBAdapter db, int hour, int min, int trackId) {
		String sql = "SELECT * from tbl_remind WHERE rm_hour=" + hour
				+ " AND rm_min=" + min;
		Cursor c = db.queryDatabase(sql);
		if (c.getCount() < 1) {
			Intent intent = new Intent(getBaseContext(),
					MyBroadcastReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					getBaseContext().getApplicationContext(), trackId, intent,
					0);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, min);
			calendar.set(Calendar.SECOND, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
					pendingIntent);
		}

		sql = "SELECT * from tbl_remind WHERE rm_hour=" + hour + " AND rm_min="
				+ min + " AND fk_track_id='" + trackId + "'";
		c = db.queryDatabase(sql);
		if (c.getCount() < 1) {
			sql = "INSERT INTO tbl_remind VALUES(NULL, '" + trackId + "', '"
					+ hour + "', '" + min + "', " + 1 + ", '" + trackId
					+ "', 'all_days')";
			db.execute(sql);
		}
	}

	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			showDialog(0);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// options[0] = "Hello1";
		// options[1] = "Hello2";
		// options[2] = "Hello3";

		int j = 0;
		for (int i = 0; i < 24; i++) {
			options[j] = i + ":" + "00";
			if(created.contains(i + ":" + "00")){
				selections[j] = true;
			}
			j++;
			options[j] = i + ":" + "30";
			if(created.contains(i + ":" + "30")){
				selections[j] = true;
			}
			j++;
		}
		
		return new AlertDialog.Builder(this)
				.setTitle("Available Times")
				.setMultiChoiceItems(options, selections,
						new DialogSelectionClickHandler())
				.setPositiveButton("OK", new DialogButtonClickHandler())
				.create();
	}

	public class DialogSelectionClickHandler implements
			DialogInterface.OnMultiChoiceClickListener {
		public void onClick(DialogInterface dialog, int clicked,
				boolean selected) {
			// Log.i( "ME", options[ clicked ] + " selected: " + selected );
		}
	}

	public class DialogButtonClickHandler implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int clicked) {
			// switch( clicked )
			// {
			// case DialogInterface.BUTTON_POSITIVE:
			// printSelected();
			// break;
			// }
		}
	}
	
	private void removeAndCancelAlarms(int trackId){
		DBAdapter db = new DBAdapter(getBaseContext());
		db.open();
		String sql = "DELETE FROM tbl_remind WHERE fk_track_id='"
				+ trackId + "'";
		db.execute(sql);
		db.close();

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent myIntent = new Intent(getBaseContext(),
				MyBroadcastReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), trackId, myIntent, 0);

		alarmManager.cancel(pendingIntent);
	}

	private String doubleDigitString(int num) {
		String r = num + "";
		if (r.length() < 2) {
			r = "0" + r;
		}
		return r;
	}

	// protected void printSelected(){
	// for( int i = 0; i < options.length; i++ ){
	// Toast.makeText(getBaseContext(), options[ i ] + " selected: " +
	// _selections[i], Toast.LENGTH_SHORT).show();
	// }
	// }
}
