package study.ms.bonn.lab.qs;

import java.text.SimpleDateFormat;
import java.util.Date;

import study.ms.bonn.lab.qs.utility.DBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class ReminderConstraintActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind_constraint);
		Bundle extras = getIntent().getExtras();
		final int trackId = extras.getInt("trackId");

		String onlyOn = "";

		DBAdapter db = new DBAdapter(getBaseContext());
		db.open();
		Cursor c = db
				.queryDatabase("SELECT only_on FROM tbl_remind WHERE fk_track_id='"
						+ trackId + "'");
		if (c.moveToFirst()) {
			do {
				onlyOn = c.getString(0).trim();
				break;
			} while (c.moveToNext());
		}
		db.close();

		final CheckBox ckAll = (CheckBox) findViewById(R.id.chkAllDays);
		if (onlyOn.equals("all_days")) {
			ckAll.setChecked(true);
		}
		final CheckBox ckMon = (CheckBox) findViewById(R.id.chkMonday);
		if (onlyOn.contains("mon")) {
			ckMon.setChecked(true);
		}
		final CheckBox ckTue = (CheckBox) findViewById(R.id.chkTuesday);
		if (onlyOn.contains("tue")) {
			ckTue.setChecked(true);
		}
		final CheckBox ckWed = (CheckBox) findViewById(R.id.chkWednesday);
		if (onlyOn.contains("wed")) {
			ckWed.setChecked(true);
		}
		final CheckBox ckThu = (CheckBox) findViewById(R.id.chkThursday);
		if (onlyOn.contains("thu")) {
			ckThu.setChecked(true);
		}
		final CheckBox ckFri = (CheckBox) findViewById(R.id.chkFriday);
		if (onlyOn.contains("fri")) {
			ckFri.setChecked(true);
		}
		final CheckBox ckSat = (CheckBox) findViewById(R.id.chkSaturday);
		if (onlyOn.contains("sat")) {
			ckSat.setChecked(true);
		}
		final CheckBox ckSun = (CheckBox) findViewById(R.id.chkSunday);
		if (onlyOn.contains("sun")) {
			ckSun.setChecked(true);
		}

		ckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					ckMon.setChecked(false);
					ckTue.setChecked(false);
					ckWed.setChecked(false);
					ckThu.setChecked(false);
					ckFri.setChecked(false);
					ckSat.setChecked(false);
					ckSun.setChecked(false);
					ckAll.setChecked(true);
				}
			}
		});
		class Helper1 {
			void uncheckAllDays() {
				ckAll.setChecked(false);
			}
		}
		ckMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				(new Helper1()).uncheckAllDays();
			}
		});
		ckTue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				(new Helper1()).uncheckAllDays();
			}
		});
		ckWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				(new Helper1()).uncheckAllDays();
			}
		});
		ckThu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				(new Helper1()).uncheckAllDays();
			}
		});
		ckFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				(new Helper1()).uncheckAllDays();
			}
		});
		ckSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				(new Helper1()).uncheckAllDays();
			}
		});
		ckSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				(new Helper1()).uncheckAllDays();
			}
		});

		Button btnSave = (Button) findViewById(R.id.btnConstraintSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String cString = "";
				if (ckAll.isChecked()) {
					cString = "all_days";
				} else {
					if (ckMon.isChecked()) {
						cString += "mon ";
					}

					if (ckTue.isChecked()) {
						cString += "tue ";
					}

					if (ckWed.isChecked()) {
						cString += "wed ";
					}

					if (ckThu.isChecked()) {
						cString += "thu ";
					}

					if (ckFri.isChecked()) {
						cString += "fri ";
					}

					if (ckSat.isChecked()) {
						cString += "sat ";
					}

					if (ckSun.isChecked()) {
						cString += "sun";
					}
				}
				if (!cString.isEmpty()) {

					DBAdapter db = new DBAdapter(getBaseContext());
					db.open();
					String sql = "UPDATE tbl_remind SET only_on='" + cString
							+ "' WHERE fk_track_id='" + trackId + "'";
					db.execute(sql);
					db.close();

					Intent intent = new Intent(
							"study.ms.bonn.lab.qs.REMIND_TIME");
					Bundle ext = new Bundle();
					ext.putInt("trackId", trackId);
					ext.putString("type", "init");
					intent.putExtras(ext);
					startActivity(intent);
				}
				else{
					Toast.makeText(getBaseContext(), "At least one Constraint must be picked.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}