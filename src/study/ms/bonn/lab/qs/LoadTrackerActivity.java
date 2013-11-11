package study.ms.bonn.lab.qs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoadTrackerActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_tracker);

		Button btnLoadData = (Button) findViewById(R.id.btnLoad);
		btnLoadData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText t = (EditText) findViewById(R.id.txtUrl);

				try {
					URL textUrl = new URL(t.getText().toString());
					BufferedReader bufferReader = new BufferedReader(
							new InputStreamReader(textUrl.openStream()));
					String StringBuffer;
					ArrayList<String> tglist = new ArrayList<String>();
					while ((StringBuffer = bufferReader.readLine()) != null) {
						tglist.add(StringBuffer);
					}
					String[] tg = tglist.toArray(new String[tglist.size()]);
					String[] ids = NewTrackerActivity.createNewTracker(tg, getBaseContext());
					
					Intent intent = new Intent("study.ms.bonn.lab.qs.TRACKER_START");
					Bundle extras = new Bundle();
					extras.putInt("trackerId", Integer.parseInt(ids[0]));
					extras.putString("info", ids[1]);
					intent.putExtras(extras);
	        		startActivity(intent);
					
					bufferReader.close();
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
