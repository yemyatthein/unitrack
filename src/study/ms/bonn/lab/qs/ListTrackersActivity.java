package study.ms.bonn.lab.qs;

import study.ms.bonn.lab.qs.utility.DBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class ListTrackersActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
    	layout.setOrientation(LinearLayout.VERTICAL);
    	layout.setBackgroundResource(R.drawable.back);
    	
        LayoutParams params = new LinearLayout.LayoutParams(
        						LayoutParams.FILL_PARENT,
        						LayoutParams.WRAP_CONTENT);
        
        
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
        		Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
			}
		});
        //layout.addView(btnMain);
        
        LinearLayout.LayoutParams pm3 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
    	pm3.setMargins(5, 10, 5, 20);
        LinearLayout layoutIn = new LinearLayout(this);
    	layoutIn.setOrientation(LinearLayout.VERTICAL);
    	layoutIn.setBackgroundColor(Color.parseColor("#B06749"));
    	layoutIn.setLayoutParams(pm3);
    	layoutIn.addView(btnMain);
    	layout.addView(layoutIn);
        
        DBAdapter db = new DBAdapter(this);
    	db.open();
    	Cursor c = db.queryDatabase("SELECT * FROM tbl_trackers");
    	if(c.moveToFirst()){
    		do{
    			//Toast.makeText(getBaseContext(), c.getString(1), Toast.LENGTH_SHORT).show();
    			Button btn = new Button(this);
    			
    			final int trackerId = c.getInt(0);
    			final String info = c.getString(2);
    			
    			btn.setLayoutParams(params);
    			btn.setText(c.getString(1));
    			btn.setOnClickListener(new View.OnClickListener() {
    				public void onClick(View v) {
    					Intent intent = new Intent("study.ms.bonn.lab.qs.TRACKER_START");
    					Bundle extras = new Bundle();
    					extras.putInt("trackerId", trackerId);
    					extras.putString("info", info);
    					intent.putExtras(extras);
    	        		startActivity(intent);
					}
				});
    			layout.addView(btn);
    		} while(c.moveToNext());
    	}
    	
    	
    	
    	LinearLayout.LayoutParams layoutParameter = new LinearLayout.LayoutParams(
    			LinearLayout.LayoutParams.FILL_PARENT,
    			LinearLayout.LayoutParams.WRAP_CONTENT);
    	this.addContentView(layout, layoutParameter);
    }
}