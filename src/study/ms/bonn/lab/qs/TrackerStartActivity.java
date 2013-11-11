package study.ms.bonn.lab.qs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TrackerStartActivity extends Activity {
	int trackerId;
	String info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_start);
        
        Bundle extras = getIntent().getExtras();
        info = extras.getString("info");
        trackerId = extras.getInt("trackerId");
        //Toast.makeText(getBaseContext(), sampleTrackedId + "", Toast.LENGTH_SHORT).show();
        
        TextView tview = (TextView)findViewById(R.id.txtTitle);
        tview.setText(info);
        
        ImageButton btnBrowse = (ImageButton) findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent("study.ms.bonn.lab.qs.LIST_ITEM");
        		Bundle extras = new Bundle();
        		extras.putInt("trackId", trackerId);
        		intent.putExtras(extras);
        		startActivity(intent);
			}
		});
        
        ImageButton btnCreate = (ImageButton) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent("study.ms.bonn.lab.qs.CREATE");
        		Bundle extras = new Bundle();
        		extras.putInt("trackId", trackerId);
        		extras.putString("info", info);
        		intent.putExtras(extras);
        		startActivity(intent);
			}
		});
        
        ImageButton btnReminder = (ImageButton) findViewById(R.id.btnRemindTime);
        btnReminder.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent("study.ms.bonn.lab.qs.REMIND_TIME");
        		Bundle extras = new Bundle();
        		extras.putInt("trackId", trackerId);
        		extras.putString("type", "init");
        		extras.putString("info", info);
        		intent.putExtras(extras);
        		startActivity(intent);
			}
		});
        
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBackViewAll);
        btnBack.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent("study.ms.bonn.lab.qs.LIST_TRACKERS");
				startActivity(intent);
			}
		});
        ImageButton btnMain = (ImageButton) findViewById(R.id.btnBackMain);
        btnMain.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
			}
		});
       
    }
}