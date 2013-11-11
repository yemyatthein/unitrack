package study.ms.bonn.lab.qs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.TimeZone;

import study.ms.bonn.lab.qs.utility.DBAdapter;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NewTrackerActivity extends Activity {

	static String[] dataTypes = new String[] { "text", "numeric", "set" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_tracker);

		Button btnNewTracker = (Button) findViewById(R.id.btnSubmit);

		btnNewTracker.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText edText = (EditText) findViewById(R.id.editInfo);
				String t = edText.getText().toString();
				String[] tg = t.split("\\n");
				createNewTracker(tg, getBaseContext());
			}
		});
	}

	public static String[] createNewTracker(String[] tg, Context context) {

		DBAdapter db = new DBAdapter(context);
		String[] identifiers = new String[2];
		db.open();
		// ArrayList<String> sqlStringCollection = new ArrayList<String>();
		int trackId = -3;

		if (tg.length > 2) {
			for (int i = 0; i < tg.length; i++) {
				String sql = "";
				String[] tokens = tg[i].split(":");
				if (i < 1) {
					if (tokens.length == 2) {
						sql = "INSERT INTO tbl_trackers VALUES(NULL, '"
								+ tokens[0] + "', '" + tokens[1] + "')";
						db.execute(sql);
						sql = "SELECT track_id from tbl_trackers WHERE name='"
								+ tokens[0] + "'";

						Cursor c = db.queryDatabase(sql);
						c.moveToFirst();
						trackId = c.getInt(0);

						identifiers[0] = "" + trackId;
						identifiers[1] = tokens[1];
					}
					else{
						rollbackTrackerEntry(db, trackId);
						Toast.makeText(context, "Syntax error",
								Toast.LENGTH_LONG).show();
					}
				} else if (tokens[0].equals("REMIND_AT")) {
					/*
					 * Cursor c =
					 * db.queryDatabase("SELECT * from tbl_remind WHERE rm_hour="
					 * + tokens[1] + " AND rm_min=" + tokens[2]);
					 * if(c.getCount()<1){ Toast.makeText(getBaseContext(),
					 * "Im setting alarm, sir", Toast.LENGTH_SHORT).show();
					 * Intent intent = new Intent(getBaseContext(),
					 * MyBroadcastReceiver.class); PendingIntent pendingIntent =
					 * PendingIntent
					 * .getBroadcast(getBaseContext().getApplicationContext(),
					 * trackId, intent, 0); Calendar calendar =
					 * Calendar.getInstance();
					 * calendar.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
					 * calendar.set(Calendar.HOUR_OF_DAY,
					 * Integer.parseInt(tokens[1]));
					 * calendar.set(Calendar.MINUTE,
					 * Integer.parseInt(tokens[2]));
					 * calendar.set(Calendar.SECOND, 0); AlarmManager
					 * alarmManager = (AlarmManager)
					 * getSystemService(ALARM_SERVICE);
					 * alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					 * calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
					 * pendingIntent); } sql =
					 * "INSERT INTO tbl_remind VALUES(NULL, '" + trackId +
					 * "', '" + tokens[1] + "', '" + tokens[2] + "', " + 1 +
					 * ", '" + trackId + "')"; db.execute(sql);
					 */
				} else {
					if (tokens[1].contains("set") && tokens.length == 3) {
						sql = "INSERT INTO tb_attributes VALUES(NULL, '"
								+ trackId + "', '" + tokens[0] + "', '"
								+ tokens[1] + "', '" + tokens[2]
								+ "', NULL, NULL, NULL)";
						if (tokens[0].trim().length() == 0
								|| tokens[2].trim().length() == 0) {
							rollbackTrackerEntry(db, trackId);
							sql = "";
						}
						if (!Arrays.asList(dataTypes).contains(tokens[1])) {
							rollbackTrackerEntry(db, trackId);
							sql = "";
						}
					} else if (tokens[1].contains("numeric")
							&& tokens.length == 5) {
						sql = "INSERT INTO tb_attributes VALUES(NULL, '"
								+ trackId + "', '" + tokens[0] + "', '"
								+ tokens[1] + "', NULL, '" + tokens[2] + "', '"
								+ tokens[3] + "', '" + tokens[4] + "')";
					} else if (tokens[1].contains("numeric")
							&& tokens.length < 5) {
						sql = "INSERT INTO tb_attributes VALUES(NULL, '"
								+ trackId + "', '" + tokens[0] + "', '"
								+ tokens[1] + "', NULL, NULL, NULL, '"
								+ tokens[2] + "')";
					} else {
						sql = "INSERT INTO tb_attributes VALUES(NULL, '"
								+ trackId + "', '" + tokens[0] + "', '"
								+ tokens[1] + "', NULL, NULL, NULL, NULL)";
					}

					if (!sql.isEmpty()) {
						db.execute(sql);
					} else {
						Toast.makeText(context, "Syntax error",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		} else {
			Toast.makeText(context, "Fill in the configuration values",
					Toast.LENGTH_LONG).show();
		}
		db.close();

		return identifiers;
	}

	private static void rollbackTrackerEntry(DBAdapter db, int trackId) {
		String sql = "DELETE FROM tbl_trackers WHERE track_id='" + trackId
				+ "'";
		db.execute(sql);
	}
}