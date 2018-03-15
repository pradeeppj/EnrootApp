package com.example.letsstart.notifs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letsstart.R;
import com.example.letsstart.common.Message;
import com.example.letsstart.common.MyMessages;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationActivity extends Activity {

	private GoogleMap googleMap;
	private ListView list;
	private ListAdapter adapter;
	private ArrayList<HashMap<String,String>> listarray;
	private ImageButton slider;
	private static final String IMAGEVIEW_TAG = "Android Logo";
	String msg="msg";
	
	 private LinearLayout.LayoutParams layoutParams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		Log.v("eadsf","");
		try {
            // Loading map
			initializeMap();
			initializeList();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		list.setOnItemClickListener(new OnItemClickListener(){
   		 @SuppressLint("ShowToast")
			public void onItemClick(AdapterView<?> parent, View view,
   	              int position, long id) {
   			 	String latlong,text[];
   			 	LatLng ll;
   			 	
   			 	
   			 	TextView temp = (TextView) view.findViewById(R.id.latlongTextView); 
   			 	latlong = (String) temp.getText();
   			 	text=latlong.split(";");
   			 	double lati,longi;
   			 	lati = Double.parseDouble(text[0]);
   			 	longi = Double.parseDouble(text[1]);
   			 	
   			 	ll = new LatLng(lati, longi);
   			 	
   			 	Toast.makeText(getApplicationContext(), "Location: "+ll.toString(), Toast.LENGTH_SHORT).show();
	   			
   			 	// 	create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(lati, longi)).title(latlong);
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                
                LatLng fromPosition = new LatLng(13.687140112679154, 100.53525868803263);
                LatLng toPosition = ll;

                Log.d("latlong", ll.toString());
                GMapV2Direction md = new GMapV2Direction();
                /*
                Document doc = md.getDocument(fromPosition, toPosition, GMapV2Direction.MODE_DRIVING);
                ArrayList<LatLng> directionPoint = md.getDirection(doc);
                PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);
                
                for(int i = 0 ; i < directionPoint.size() ; i++) {          
                rectLine.add(directionPoint.get(i));
                }
*/
                //googleMap.addPolyline(rectLine);
                
                // adding marker
                googleMap.addMarker(marker);
   			 	
   			 	CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lati, longi)).zoom(12).build();
	   			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	   			
	   			
   		 	}
		
		});
	
		slider = (ImageButton) findViewById(R.id.slider);
		// Sets the tag
	    slider.setTag(IMAGEVIEW_TAG);
		slider.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent motionEvent) {
				 if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				
					ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
		            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
		            ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
			        
		            DragShadowBuilder myShadow = new DragShadowBuilder(slider);
		            v.startDrag(dragData,  // the data to be dragged
		                    myShadow,  // the drag shadow builder
		                    null,      // no need to use local data
		                    0          // flags (not currently used, set to 0)
		                    );
				 }
				 
				 
				return false;
			}
		});
		
		 // Create and set the drag event listener for the View
	      slider.setOnDragListener(new View.OnDragListener() {
			
			@Override
			public boolean onDrag(View v, DragEvent event) {
				View map = findViewById(R.id.map);
				list = (ListView) findViewById(R.id.MsgListView);
				switch(event.getAction())                   
		         {
		            case DragEvent.ACTION_DRAG_STARTED:
		               layoutParams = (LinearLayout.LayoutParams)  v.getLayoutParams();
		               
		               Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
		               // Do nothing
		               break;
		            case DragEvent.ACTION_DRAG_ENTERED:
		               Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
		               //int x_cord = (int) event.getX();
		               int y_cord = (int) event.getY();  
		               break;
		            case DragEvent.ACTION_DRAG_EXITED :
		               Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
		               //x_cord = (int) event.getX();
		               y_cord = (int) event.getY();
		               //layoutParams.leftMargin = x_cord;
		               //layoutParams.topMargin = y_cord;
		               //v.setLayoutParams(layoutParams);
		               layoutParams.height=y_cord;
		               map.setLayoutParams(layoutParams);
		               break;
		            case DragEvent.ACTION_DRAG_LOCATION  :
		               Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
		               //x_cord = (int) event.getX();
		               y_cord = (int) event.getY();
		               break;
		            case DragEvent.ACTION_DRAG_ENDED   :
		               Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
		               // Do nothing
		               break;
		            case DragEvent.ACTION_DROP:
		               Log.d(msg, "ACTION_DROP event");
		               // Do nothing
		               break;
		            default: break;
		            }
		            return true;
			}
		});
	
	}

	@Override
    protected void onResume() {
        super.onResume();
        initializeMap();
        initializeList();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}
	
	/**
     * function to load map. If map is not created it will create it for you
     * */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment)getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
            
            // check if map is created successfully or not
            if (googleMap == null) {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                        .show();
                Log.v("Not Working", "working!");
            
            }
            else
            {
//            	Toast.makeText(getApplicationContext(),
//                    "Yes! able to create maps", Toast.LENGTH_SHORT)
//                    .show();
            	Log.v("Working", "working!");
            	 // latitude and longitude
                double latitude = 26.1833;
                double longitude = 91.7333;
                 
                // create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Notification");
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                 
                // adding marker
                googleMap.addMarker(marker);
                
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                //googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                //googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(latitude,longitude)).zoom(8).build();
         
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        
            
        }
    }
	private void initializeList(){
		list = (ListView) findViewById(R.id.MsgListView);
		listarray = new ArrayList<HashMap<String,String>>();
		adapter = new SimpleAdapter(NotificationActivity.this,listarray,R.layout.item,new String[] {"user","place","date","time","distance","latlong"},new int[] {R.id.UserTextView,R.id.PlaceTextView,R.id.DateTextView,R.id.TimeTextView,R.id.DistanceTextView,R.id.latlongTextView});
        list.setAdapter(adapter);

        Message m ;
       for(int i = 0 ; i < MyMessages.getMyPrivateMessages().size() ; i++) {
           m = MyMessages.getMyPrivateMessages().get(i);
           HashMap<String, String> item = new HashMap<String, String>();
           item.put("user", m.getMessage());
           item.put("place", "SAC");
           item.put("date", m.getCreatedAt().toString());
           item.put("distance","unknown");
           item.put("latlong", m.getLocation().getLatitude()+";"+m.getLocation().getLongitude());

           listarray.add(item);
       }


        
	}

}
