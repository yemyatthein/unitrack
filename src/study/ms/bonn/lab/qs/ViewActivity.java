package study.ms.bonn.lab.qs;

import study.ms.bonn.lab.qs.utility.DBAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_item);
        
        LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        parm.setMargins(10, 10, 10, 0);
        TextView t = (TextView) findViewById(R.id.txt);
        t.setBackgroundColor(Color.WHITE);
        t.setPadding(8, 8, 8, 8);
        t.setTextColor(Color.BLACK);
        t.setLayoutParams(parm);

        Bundle extras = getIntent().getExtras();
        int postId = extras.getInt("postId");
        String timeString = extras.getString("timeString");
        
        DBAdapter db = new DBAdapter(this);
    	db.open();
    	Cursor c = db.queryDatabase("SELECT * FROM tbl_values AS tv, tb_attributes AS ta " +
    								"WHERE ta.attri_id=tv.fk_attri_id AND tv.fk_post_id=" + postId);
    	t.setText(timeString);
    	if(c.moveToFirst()){
    		do{
    			t.setText(t.getText()+ "\n\n" + c.getString(5) + ":\n" + c.getString(2));
    		} while(c.moveToNext());
    	}
    }
}