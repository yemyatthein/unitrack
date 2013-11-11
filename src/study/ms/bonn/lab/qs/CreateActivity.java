package study.ms.bonn.lab.qs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import study.ms.bonn.lab.qs.utility.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class CreateActivity extends Activity {
	
	ArrayList<HashMap<String, String>> attributeData;
	HashMap<Integer, String> valueData;
	String curentDateandTime;
	int trackId;
	String info;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.create);
        init();
        createGUI();
        //sampleGUICreator();
    }
    
    private void init(){
    	
    	Bundle extras = getIntent().getExtras();
        
    	trackId = extras.getInt("trackId");
        info = extras.getString("info");
        
        //Toast.makeText(getBaseContext(), trackId + "", Toast.LENGTH_SHORT).show();
    	attributeData = new ArrayList<HashMap<String,String>>();
    	valueData = new HashMap<Integer, String>();
    	DBAdapter db = new DBAdapter(this);
    	db.open();
    	Cursor c = db.queryDatabase("SELECT * FROM tb_attributes WHERE fk_track_id=" + trackId);
    	
    	if(c.moveToFirst()){
    		do{
    			HashMap<String, String> valueMap = new HashMap<String, String>();

    			valueMap.put(c.getColumnName(0), c.getString(0));
    			valueMap.put(c.getColumnName(1), c.getString(1));
    			valueMap.put(c.getColumnName(2), c.getString(2));
    			valueMap.put(c.getColumnName(3), c.getString(3));
    			valueMap.put(c.getColumnName(4), c.getString(4));
    			valueMap.put(c.getColumnName(5), c.getString(5));
    			valueMap.put(c.getColumnName(6), c.getString(6));
    			valueMap.put(c.getColumnName(7), c.getString(7));
    			
    			attributeData.add(valueMap);
    		} while(c.moveToNext());
    	}
    }
        
    private void createGUI(){
    	final HashMap<Integer, ArrayList<String>> decimalConstraints = new HashMap<Integer, ArrayList<String>>();
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
    	ImageButton btnHome = new ImageButton(this);
    	btnHome.setImageResource(R.drawable.e);
    	btnHome.setLayoutParams(pm2);
    	btnHome.setScaleType(ScaleType.FIT_CENTER);
        btnHome.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent("study.ms.bonn.lab.qs.TRACKER_START");
				Bundle extras = new Bundle();
				extras.putInt("trackerId", trackId);
				extras.putString("info", info);
				intent.putExtras(extras);
        		startActivity(intent);
			}
		});
        
        LinearLayout.LayoutParams pm3 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
    	pm3.setMargins(5, 10, 5, 20);
        LinearLayout layoutIn = new LinearLayout(this);
    	layoutIn.setOrientation(LinearLayout.VERTICAL);
    	layoutIn.setBackgroundColor(Color.parseColor("#B06749"));
    	layoutIn.setLayoutParams(pm3);
    	layoutIn.addView(btnHome);
    	layout.addView(layoutIn);
        
        
        TextView timeDisplay = new TextView(this);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        timeDisplay.setTextColor(Color.BLACK);
        curentDateandTime = sdf.format(new Date());
        timeDisplay.setText(curentDateandTime);
        layout.addView(timeDisplay);

//        int intNumericMin = 0;
        
        for(final HashMap<String, String> mapItem: attributeData){
	        TextView tv = new TextView(this);
	        tv.setLayoutParams(params);
	        tv.setTextColor(Color.BLACK);
	        tv.setText(mapItem.get("key"));
	        String dataType = mapItem.get("data_type");
	        
	        EditText editText = new EditText(this);
	        if(dataType.equals("text")){
	        	editText.setLayoutParams(params);
	        	editText.setInputType(InputType.TYPE_CLASS_TEXT);	        	
	        	editText.setId(Integer.parseInt(mapItem.get("attri_id").trim()));
	        	layout.addView(tv);
		        layout.addView(editText);
	        }
	        else if(dataType.equals("numeric") && mapItem.get("numeric_cons_max") != null && mapItem.get("numeric_cons_min") != null && mapItem.get("numeric_type").equals("integer")){
	        	SeekBar seek = new SeekBar(this);
	        	
	        	int max = Integer.parseInt(mapItem.get("numeric_cons_max"));
	        	final int min = Integer.parseInt(mapItem.get("numeric_cons_min"));
	        	
	        	ArrayList<String> aval = new ArrayList<String>();
	        	aval.add(mapItem.get("numeric_cons_min"));
	        	aval.add(mapItem.get("numeric_cons_max"));
	        	decimalConstraints.put(Integer.parseInt(mapItem.get("attri_id").trim()), aval);
	        	
	//        	intNumericMin = min;
	        	int diff = max - min;
	        	seek.setMax(diff);
	        	seek.setId(Integer.parseInt(mapItem.get("attri_id").trim()));
	        	
	        	final TextView tvStatus = new TextView(this);
	        	tvStatus.setText("Value: " + min);
	        	tvStatus.setTextColor(Color.BLACK);
	        	seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	        		
	        		@Override
	        		public void onStopTrackingTouch(SeekBar seekBar) {
	        		}
	        		
	        		@Override
	        		public void onStartTrackingTouch(SeekBar seekBar) {
	        		}
	        		
	        		@Override
	        		public void onProgressChanged(SeekBar seekBar, int progress,
	        				boolean fromUser) {
	        			int temp = progress + min;
	        			tvStatus.setText("Value: " + temp);
	        		}
	        	});
	        	layout.addView(tv);
	        	layout.addView(seek);
	        	layout.addView(tvStatus);
	        }
	        else if(dataType.equals("numeric") && mapItem.get("numeric_cons_max") != null && mapItem.get("numeric_cons_min") != null && mapItem.get("numeric_type").equals("double")){
	        	editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
	        	//editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
	        	editText.setLayoutParams(new LinearLayout.LayoutParams(250, LayoutParams.WRAP_CONTENT));
	        	editText.setId(Integer.parseInt(mapItem.get("attri_id").trim()));
	        	editText.setTag(mapItem.get("key"));
	        	ArrayList<String> aval = new ArrayList<String>();
	        	aval.add(mapItem.get("numeric_cons_min"));
	        	aval.add(mapItem.get("numeric_cons_max"));
	        	decimalConstraints.put(Integer.parseInt(mapItem.get("attri_id").trim()), aval);
	        	layout.addView(tv);
		        layout.addView(editText);
	        }
	        else if(dataType.equals("numeric") && mapItem.get("numeric_cons_max") == null && mapItem.get("numeric_type").equals("double")){
	        	editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
	        	//editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
	        	editText.setLayoutParams(new LinearLayout.LayoutParams(250, LayoutParams.WRAP_CONTENT));
	        	editText.setId(Integer.parseInt(mapItem.get("attri_id").trim()));	        	
	        	layout.addView(tv);
		        layout.addView(editText);
		        //Toast.makeText(getBaseContext(), mapItem.get("numeric_cons_min"), Toast.LENGTH_SHORT).show();
	        }
	        else if(dataType.equals("numeric") && mapItem.get("numeric_cons_max") == null && mapItem.get("numeric_type").equals("integer")){
	        	editText.setInputType(InputType.TYPE_CLASS_NUMBER);
	        	editText.setLayoutParams(new LinearLayout.LayoutParams(250, LayoutParams.WRAP_CONTENT));
	        	editText.setId(Integer.parseInt(mapItem.get("attri_id").trim()));	        	
	        	layout.addView(tv);
		        layout.addView(editText);
		        //Toast.makeText(getBaseContext(), mapItem.get("numeric_cons_min"), Toast.LENGTH_SHORT).show();
	        }
	        else if(dataType.equals("set")){
	        	final String[] itemData =  mapItem.get("set_values").split("\\|");
	        	Spinner spinner = new Spinner(this);
	        	spinner.setLayoutParams(new LinearLayout.LayoutParams(250, LayoutParams.WRAP_CONTENT));
	        	spinner.setId(Integer.parseInt(mapItem.get("attri_id").trim()));
	        	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, 
	            		android.R.layout.simple_spinner_item, 
	            		itemData
	            		);
	            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            spinner.setAdapter(adapter);	            layout.addView(tv);
		        layout.addView(spinner);
	        }
	        valueData.put(Integer.parseInt(mapItem.get("attri_id").trim()), "DEFAULT_VALUE");
        }
//        for(Integer item: decimalConstraints.keySet()){
//        	Toast.makeText(getBaseContext(), item + "=>" + decimalConstraints.get(item).get(0), Toast.LENGTH_SHORT).show();
//        }
        Button btnSubmit = new Button(this);
        btnSubmit.setText("Save Data");
        btnSubmit.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
        layout.addView(btnSubmit);
        
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean isEmpty = false;
				boolean constraintViolated = false;
				String msg = "";
				for(Integer item: valueData.keySet()){
					View view = findViewById(item.intValue());
					String valueToFill = "";
					if(view != null){
						if(view instanceof EditText){
							EditText ed = (EditText) view;
							
							valueToFill = ed.getText().toString();
							if(decimalConstraints.containsKey(item)){
								if(isDouble(valueToFill)){
									String ptName = ed.getTag().toString();
									double minVal = Double.parseDouble(decimalConstraints.get(item.intValue()).get(0));
									double maxVal = Double.parseDouble(decimalConstraints.get(item.intValue()).get(1));
									double current = Double.parseDouble(valueToFill);
									if(current>maxVal || current < minVal){
										constraintViolated = true;
										msg = "Constraint Violated in '" + ptName 
												+ "'. The value must be between " + minVal + " and " + maxVal;
										break;
									}
								}
							}
						}
						else if(view instanceof Spinner){
							Spinner sp = (Spinner) view;
							valueToFill = sp.getSelectedItem().toString();
						}
						else if(view instanceof SeekBar){
							SeekBar sb = (SeekBar) view;
							int minVal = Integer.parseInt(decimalConstraints.get(item.intValue()).get(0));
							int temp = minVal + sb.getProgress();
							valueToFill = temp + "";
						}
					}
					if(valueToFill.trim().isEmpty()){
						isEmpty = true;
						msg = "Please fill in the values";
						break;
					}
					valueData.put(item, valueToFill);
				}
				if(!isEmpty && !constraintViolated){
					saveToDatabase(valueData, trackId);
					
					Intent intent = new Intent("study.ms.bonn.lab.qs.LIST_ITEM");
	        		Bundle extras = new Bundle();
	        		extras.putInt("trackId", trackId);
	        		intent.putExtras(extras);
	        		startActivity(intent);
				}
				else
					Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
			}
			
			private void saveToDatabase(HashMap<Integer, String> valueData, int trackId) {
				String[] timeDate = curentDateandTime.split(" ");
				String[] hourMin = timeDate[0].split(":");
				String[] dayMonthYear = timeDate[1].split("/");
				
				String sqlString = "INSERT INTO tbl_post VALUES(NULL, '" + trackId + "', '" + dayMonthYear[0] + 
									"', '" + dayMonthYear[1] + "', '" + dayMonthYear[2] + "', '" + 
									hourMin[0] + "', '" + hourMin[1] + "')";
				DBAdapter db = new DBAdapter(getBaseContext());
		    	db.open();
		    	db.execute(sqlString);
		    	sqlString = "SELECT post_id from tbl_post WHERE fk_track_id='" + trackId + "' AND day='" + dayMonthYear[0] + "' AND " + 
		    					"month='" + dayMonthYear[1] + "' AND year='" + dayMonthYear[2] + 
		    					"' AND hour='" + hourMin[0] + "' AND min='" + hourMin[1] + "'";
		    	Cursor c = db.queryDatabase(sqlString);
		    	c.moveToFirst();
		    	int postId = c.getInt(0);
		    	
		    	for(Integer item: valueData.keySet()){
		    		int attriId = item.intValue();
		    		sqlString = "INSERT into tbl_values VALUES('" + postId + "', '" + attriId + "', '" + valueData.get(item) + "')";
		    		db.execute(sqlString);
		    	}
		    	
		    	db.close();
			}
		});
        
        LinearLayout.LayoutParams layoutParameter = new LinearLayout.LayoutParams(
        								LayoutParams.FILL_PARENT,
        								LayoutParams.WRAP_CONTENT);
        this.addContentView(layout, layoutParameter);
    }
    
    private boolean isDouble(String value) {
    	try{
    		Double.parseDouble(value);
    	}catch(Exception ex){
    		return false;    		
    	}
    	return true;
	}
    
}