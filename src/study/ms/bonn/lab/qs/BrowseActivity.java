package study.ms.bonn.lab.qs;

import java.util.ArrayList;
import java.util.HashSet;
import study.ms.bonn.lab.qs.utility.DBAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BrowseActivity extends ListActivity {
	String[] entries;
	ArrayList<EntityPost> allPosts;
	int trackId;
	
	class EntityPost{
		int postId;
		String timeString;
		ArrayList<String> contents;
		EntityPost(int postId) {
			this.postId = postId;
			contents = new ArrayList<String>();
		}
		void addContent(String value){
			contents.add(value);
		}
		ArrayList<String> getConents(){
			return contents;
		}
		void setTimeString(String time){
			this.timeString = time;
		}
		String getTimeString(){
			return timeString;
		}
		int getPostId(){
			return postId;
		}
	}
	
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entries));
		getListView().setBackgroundResource(R.drawable.back);
		getListView().setDivider(new ColorDrawable(Color.parseColor("#000000")));
		getListView().setDividerHeight(2);
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id){
		EntityPost ep = allPosts.get(position);
		int postId = ep.getPostId();
		String timeString = ep.getTimeString();
		
		Intent in = new Intent("study.ms.bonn.lab.qs.VIEW_DETAIL");
		
		Bundle extras = new Bundle();
		extras.putInt("postId", postId);
		extras.putString("timeString", timeString);
		
		in.putExtras(extras);
		startActivity(in);
	}
	
	private void init(){
		
		Bundle extras = getIntent().getExtras();
		trackId = extras.getInt("trackId");
		
		allPosts = new ArrayList<BrowseActivity.EntityPost>();
		DBAdapter db = new DBAdapter(this);
    	db.open();
    	Cursor c = db.queryDatabase("SELECT * FROM tbl_post AS tp, tbl_values AS tv " +
    								"WHERE tp.fk_track_id=" + trackId + " AND tp.post_id=tv.fk_post_id " +
    								"ORDER BY tp.post_id  DESC;");
    	if (c.moveToFirst())
		{
    		HashSet<Integer> set = new HashSet<Integer>();
			c.moveToFirst();
			EntityPost epost = null;
			for(int i=0; i<c.getCount(); i++){
				boolean isNew = false;
				int postId = c.getInt(0);
				if(!set.contains(postId)){
					isNew = true;
					set.add(postId);
					epost = new EntityPost(postId);
					String time = "On " + c.getInt(c.getColumnIndex("hour")) + ":" + c.getInt(c.getColumnIndex("min")) + " " 
									+ c.getInt(c.getColumnIndex("day")) + "/" + c.getInt(c.getColumnIndex("month")) + "/" 
									+ c.getInt(c.getColumnIndex("year"));
					epost.setTimeString(time);
				}
				String content = c.getString(9);
				epost.addContent(content);
				if(isNew){
					allPosts.add(epost);
				}
				if(!c.moveToNext()) break;
			}
		}
    	db.close();
    	
    	entries = new String[allPosts.size()];
    	for(int i=0; i<allPosts.size(); i++){
    		entries[i] = allPosts.get(i).getConents().get(0) + " " + allPosts.get(i).getTimeString();
    	}
	}
	
}