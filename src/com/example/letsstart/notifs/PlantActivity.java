package com.example.letsstart.notifs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import com.example.letsstart.R;

public class PlantActivity extends Activity {
	final Context c =this;
	Button b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plant);
		
		/*
		RelativeLayout dialogLayout = (RelativeLayout) findViewById(R.id.dialoglayout);
		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.plantlayout);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		mainLayout.addView(dialogLayout, params);
		
		b = (Button) findViewById(R.id.button1);
		
		// add button listener
				b.setOnClickListener(new View.OnClickListener() {
		 
				  @Override
				  public_logo void onClick(View arg0) {
					  
					// custom dialog
					final Dialog dialog = new Dialog(c);
					dialog.setContentView(R.layout.dialog);
					dialog.setTitle("title");
					ImageView OK = (ImageView) dialog.findViewById(R.id.galleryimageView);
					// if button is clicked, close the custom dialog
					OK.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public_logo void onClick(View v) {

							dialog.dismiss();
							Log.v("dialog", "dismissed");
						}
					});
		 
					dialog.show();
					Log.v("dialog", "shown");
				  }
				});
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plant, menu);
		return true;
	}

}
