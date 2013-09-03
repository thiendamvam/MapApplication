/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapdemo.adapter.UserAdapter;
import com.example.mapdemo.model.User;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * This shows how to place markers on a map.
 */
public class MarkerDemoActivity extends FragmentActivity
        implements OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener, LocationListener {
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    private static final LatLng HANGXANH = new LatLng(10.801216, 106.711278);
    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);   
    
    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements InfoWindowAdapter {
        private final RadioGroup mOptions;

        // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
            mOptions = (RadioGroup) findViewById(R.id.custom_info_window_options);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
                // This means that getInfoContents will be called.
                return null;
            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
                // This means that the default info contents will be used.
                return null;
            }
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            if (marker.equals(mBrisbane)) {
                badge = R.drawable.badge_qld;
            } else if (marker.equals(mAdelaide)) {
                badge = R.drawable.badge_sa;
            } else if (marker.equals(mHANGXANH)) {
                badge = R.drawable.badge_nsw;
            } else if (marker.equals(mMelbourne)) {
                badge = R.drawable.badge_victoria;
            } else if (marker.equals(mPerth)) {
                badge = R.drawable.badge_wa;
            } else {
                // Passing 0 to setImageResource will clear the image view.
                badge = 0;
            }
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }

    private GoogleMap mMap;

    private Marker mPerth;
    private Marker mHANGXANH;
    private Marker mBrisbane;
    private Marker mAdelaide;
    private Marker mMelbourne;
    private TextView mTopText;
	private LinearLayout lnUserList;
	private ListView lvUserList;
	//
	private EditText txtSearchList;
	private UserAdapter userAdapter;
	private ArrayList<User>  arraySort;
	//
//	private HashMap<String,LatLng> latLngUpdate;
	private HashMap<String, LatLng> historyLatLng;
	private ArrayList<User> userList;
	ArrayList<LatLng> latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_demo);

        mTopText = (TextView) findViewById(R.id.top_text);
        lnUserList = (LinearLayout)findViewById(R.id.lnUserList);
        lvUserList = (ListView)findViewById(R.id.lvUserList);
        txtSearchList = (EditText)findViewById(R.id.txtSearchList);

        getUserData();


        setUpMapIfNeeded();
        initUserList();
        focusMap();
        lvUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
        		switch(position){
        			case 0:
        				mMap.moveCamera(CameraUpdateFactory.newLatLng(ADELAIDE));
        				break;
        			case 1:
        				mMap.moveCamera(CameraUpdateFactory.newLatLng(BRISBANE));
        				break;
        			case 2:
        				mMap.moveCamera(CameraUpdateFactory.newLatLng(HANGXANH));
        				break;
        			case 3:
        				mMap.moveCamera(CameraUpdateFactory.newLatLng(MELBOURNE));
        				break;
        			case 4:
        				mMap.moveCamera(CameraUpdateFactory.newLatLng(PERTH));
        				break;
        			default:
        				return;
        		}
        	}
		});
        lvUserList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id){
        		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MarkerDemoActivity.this);
        		dialogBuilder.setMessage("Do you want to delete "+lvUserList.getItemAtPosition(position)+" ?");
        		dialogBuilder.setPositiveButton("Yes", new OnClickListener() {								
					public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub	
						userList.remove(position);
						userAdapter = new UserAdapter(MarkerDemoActivity.this, userList);
						lvUserList.setAdapter(userAdapter);
						Log.d("Listview", "userArrayList ====>" + userList);
					}
				});
        		dialogBuilder.setNegativeButton("No", new OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});        		
        		AlertDialog dialog = dialogBuilder.create();
        		dialog.show();
        		return true;
        	}
		});
        txtSearchList.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (!txtSearchList.getText().toString().equalsIgnoreCase("")){
                    arraySort = new ArrayList<User>();
                    arraySort.clear();
                    String text = txtSearchList.getText().toString();
                    for(int i=0 ;i< userList.size();i++){
                        //if(globalconstant.mosq_list.get(globalconstant.hashformosq.get(globalconstant.tempList.get(i))).name.toUpperCase().toString().contains(text.toUpperCase())){
                        if(userList.get(i).getName().toUpperCase().toString().contains(text.toUpperCase())){
                        	arraySort.add(userList.get(i));
                        }
                    }
                    userAdapter = new UserAdapter(MarkerDemoActivity.this, arraySort);
    				lvUserList.setAdapter(userAdapter);
                }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (txtSearchList.getText().toString().equalsIgnoreCase("")){
					userAdapter = new UserAdapter(MarkerDemoActivity.this, userList);
					lvUserList.setAdapter(userAdapter);					
				}
			}
		});
    }
    
    private void focusMap() {
		// TODO Auto-generated method stub
    	mMap.animateCamera(CameraUpdateFactory.zoomIn());

 
    	// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
    	CameraPosition cameraPosition = new CameraPosition.Builder()
    	    .target(HANGXANH)      // Sets the center of the map to Mountain View
    	    .zoom(mMap.getMaxZoomLevel())                   // Sets the zoom
    	    .bearing(90)                // Sets the orientation of the camera to east
    	    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
    	    .build();                   // Creates a CameraPosition from the builder
    	mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	private void getUserData() {
		// TODO Auto-generated method stub
//      userList = new HashMap<String, User>();  
      //put user
//      LatLng item = new LatLng(1000, 2000);
//      latLng.add(item);
//      LatLng currentItem = new LatLng(6777, 67665);
//      
//      User user1 = new User("1", "A", "12345-1", item,latLng);
//      User user2 = new User("1", "A", "12345-1", currentItem,latLng);
	}

	private void initUserList() {
		// TODO Auto-generated method stub
		userList = new ArrayList<User>();
		User item = new User("1", "abc", "abc", new LatLng(111, 333), new ArrayList<LatLng>());
		userList.add(item);
		UserAdapter userAdapter = new UserAdapter(MarkerDemoActivity.this, userList);
		lvUserList.setAdapter(userAdapter);
	}

    public void onMenuClicked(View v){
    	if(lnUserList.getVisibility()==View.VISIBLE){
    		lnUserList.setVisibility(View.GONE);
    	}else{
    		lnUserList.setVisibility(View.VISIBLE);
    	}
    }
	@Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 117.0f ) );
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        // Add lots of markers to the map.
        addMarkersToMap();
      
        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);

        // Pan to see all markers in view.
        // Cannot zoom to bounds until the map has a size.
        final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(PERTH)
                            .include(HANGXANH)
                            .include(ADELAIDE)
                            .include(BRISBANE)
                            .include(MELBOURNE)
                            .build();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                      mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                      mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });
        }
        drawWay();
    }
    public void drawUserListInMap(User user){
    	
    }
	public Handler handleUpdateUserLocation = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			int value = msg.arg1;
//			String string = (String)msg.obj;
			exeDrawWay();

		}
	};
	public boolean isUdapte = false;
    public void updateUserLocation(){
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handleUpdateUserLocation.sendEmptyMessage(1);
				if(isUdapte){
					updateUserLocation();
				}
			}
			}, 50);
    }
	private void drawWay() {
		updateUserLocation();
	}
	private void exeDrawWay() {
		// TODO Auto-generated method stub
//		for(int i= 0; i< userListPoint.size(); i++){
			LatLng point = HANGXANH;
			LatLng newLatLng = new LatLng(10.801941,106.647482);
			drawDirection(point, newLatLng);
//		}
	}

	private void addMarkersToMap() {
        // Uses a colored icon.
        mBrisbane = mMap.addMarker(new MarkerOptions()
                .position(HANGXANH)
                .title("A")
                .snippet("Info: 2,074,200")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(HANGXANH));
//        // Uses a custom icon.
//        mHANGXANH = mMap.addMarker(new MarkerOptions()
//                .position(HANGXANH)
//                .title("B")
//                .snippet("Info: 4,627,300")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
//
//        // Creates a draggable marker. Long press to drag.
//        mMelbourne = mMap.addMarker(new MarkerOptions()
//                .position(MELBOURNE)
//                .title("C")
//                .snippet("Info: 4,137,400")
//                .draggable(true));
//
//        // A few more markers for good measure.
//        mPerth = mMap.addMarker(new MarkerOptions()
//                .position(PERTH)
//                .title("D")
//                .snippet("Info: 1,738,800"));
//        mAdelaide = mMap.addMarker(new MarkerOptions()
//                .position(ADELAIDE)
//                .title("E")
//                .snippet("Info: 1,213,000"));

        // Creates a marker rainbow demonstrating how to create default marker icons of different
        // hues (colors).
//        int numMarkersInRainbow = 12;
//        for (int i = 0; i < numMarkersInRainbow; i++) {
//            mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(
//                            -30 + 10 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)),
//                            135 - 10 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
//                    .title("Marker " + i)
//                    .icon(BitmapDescriptorFactory.defaultMarker(i * 360 / numMarkersInRainbow)));
//        }
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /** Called when the Clear button is clicked. */
    public void onClearMap(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }

    /**
     * Refresh user location after 5 minuts.
     */
    

    
    /** Called when the Reset button is clicked. */
    public void onResetMap(View view) {
        if (!checkReady()) {
            return;
        }
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
        addMarkersToMap();
        
    }

    /*
     * Draw way in map
     */
    //
    // Marker related listeners.
    //
	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		private ArrayList<LatLng> points;

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(5);
				lineOptions.color(Color.RED);
				
				

			}

			// Drawing polyline in the Google Map for the i-th route
			mMap.addPolyline(lineOptions);
		}
	}

    public void drawDirection(LatLng origin, LatLng dest){

    	Log.d("drawDirection","drawDirection");
		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(origin, dest);

		DownloadTask downloadTask = new DownloadTask();

		// Start downloading json data from Google Directions
		// API
		downloadTask.execute(url);
    }
    


	/*
     * (non-Javadoc)
     * Draw way in mapDraw way in map
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(mPerth)) {
            // This causes the marker at Perth to bounce into position when it is clicked.
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(1 - interpolator
                            .getInterpolation((float) elapsed / duration), 0);
                    marker.setAnchor(0.5f, 1.0f + 2 * t);

                    if (t > 0.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        } else if (marker.equals(mAdelaide)) {
            // This causes the marker at Adelaide to change color.
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(new Random().nextFloat() * 360));
        }
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getBaseContext(), "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        mTopText.setText("onMarkerDragStart");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mTopText.setText("onMarkerDragEnd");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Toast.makeText(MarkerDemoActivity.this, "locaitonchange", Toast.LENGTH_SHORT).show();
	}
}
