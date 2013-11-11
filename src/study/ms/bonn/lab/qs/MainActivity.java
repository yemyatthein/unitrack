package study.ms.bonn.lab.qs;

import java.util.Calendar;
import java.util.TimeZone;

import study.ms.bonn.lab.qs.utility.DBAdapter;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	int sampleTrackedId = 1;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("DFG", "Just started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Alarm
		/*
		 * Intent intent = new Intent(this, MyBroadcastReceiver.class);
		 * PendingIntent pendingIntent =
		 * PendingIntent.getBroadcast(this.getApplicationContext(), 123, intent,
		 * 0); Calendar calendar = Calendar.getInstance();
		 * calendar.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
		 * calendar.set(Calendar.HOUR_OF_DAY, 18); calendar.set(Calendar.MINUTE,
		 * 30); AlarmManager alarmManager = (AlarmManager)
		 * getSystemService(ALARM_SERVICE);
		 * alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		 * calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
		 * pendingIntent);
		 */
		// --------

		ImageButton btnNewTracker = (ImageButton) findViewById(R.id.btnNewTracker);
		btnNewTracker.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("study.ms.bonn.lab.qs.NEW_TRACKER");
				startActivity(intent);
			}
		});

		ImageButton btnLoadData = (ImageButton) findViewById(R.id.btnLoadTracker);
		btnLoadData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("study.ms.bonn.lab.qs.LOAD_DATA");
				startActivity(intent);
			}
		});

		ImageButton btnViewALl = (ImageButton) findViewById(R.id.btnViewAll);
		btnViewALl.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("study.ms.bonn.lab.qs.LIST_TRACKERS");
				startActivity(intent);
			}
		});

		ImageButton btnReset = (ImageButton) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(MainActivity.this)
				.setMessage("By resetting database, you will delete all trackers setting " +
						"and their respective data. Are you sure want to do this?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								deleteAll();
							}
						})
				.setNegativeButton("No",null).show();
			}

			public void deleteAll() {
				DBAdapter db = new DBAdapter(getBaseContext());
				db.open();
				db.execute("DELETE FROM tbl_trackers");
				db.execute("DELETE FROM tb_attributes");
				db.execute("DELETE FROM tbl_post");
				db.execute("DELETE FROM tbl_values");
				db.execute("DELETE FROM tbl_notify");

				Cursor c = db.queryDatabase("SELECT * from tbl_remind");
				int reqCode = 0;
				if (c.moveToFirst()) {
					do {
						// t.setText(t.getText()+ "\n\n" + c.getString(5) +
						// ":\n" + c.getString(2));
						reqCode = c.getInt(5);
						AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
						Intent myIntent = new Intent(getBaseContext(),
								MyBroadcastReceiver.class);
						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(getApplicationContext(), reqCode,
										myIntent, 0);
						alarmManager.cancel(pendingIntent);
					} while (c.moveToNext());
				}
				db.execute("DELETE FROM tbl_remind");
				db.close();
			}
		});

	}

	@Override
	protected void onDestroy() {

		/*
		 * AlarmManager alarmManager = (AlarmManager)
		 * getSystemService(ALARM_SERVICE); Intent myIntent = new
		 * Intent(getBaseContext(), MyBroadcastReceiver.class); PendingIntent
		 * pendingIntent = PendingIntent.getBroadcast( getApplicationContext(),
		 * 123, myIntent, 0); alarmManager.cancel(pendingIntent);
		 */
		super.onDestroy();
	}

}