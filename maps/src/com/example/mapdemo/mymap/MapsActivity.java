package com.example.mapdemo.mymap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapdemo.R;
import com.example.mapdemo.adapter.UserAdapter;
import com.example.mapdemo.model.User;
import com.example.mapdemo.service.IServiceListener;
import com.example.mapdemo.service.Service;
import com.example.mapdemo.service.ServiceAction;
import com.example.mapdemo.service.ServiceResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener,
		IServiceListener {

	private ArrayList<User> userList;
	private HashMap<String, ArrayList<LatLng>> userPositionList;
	private static final LatLng HANGXANH = new LatLng(10.801216, 106.711278);
	private boolean isSanbox = true;

	// Define elements
	private GoogleMap mMap;
	ArrayList<LatLng> markerPoints;
	private Marker mSydney;
//	private HashMap<String, String> myMaker;
	private LatLng USER_LOCAL = null;
	private LatLng MARKER1 = new LatLng(10.8026294, 106.6501343);
	private LatLng MARKER2 = new LatLng(10.7951191, 106.6671596);
	private LatLng MARKER3 = new LatLng(10.8409141, 106.6730833);
	private LatLng MARKER4 = new LatLng(10.7994188, 106.7065515);
	// private static final LatLng MY_HOME = new LatLng(10.72277, 106.710235);
	public static final String TAG_BUNDLEBRANCH = "branch_data";
	public static final String TAG_HOTELLAT = "lat";
	public static final String TAG_HOTELLON = "lon";
	public static final String TAG_HOTELTITLE_EN = "title_en";
	public static final String TAG_HOTELADDRESS_EN = "address_en";
	public static final String TAG_HOTELPHONE = "phone";
	public static final String TAG_NAMEFAX = "fax";
	public static final String TAG_HOTELEMAIL_EN = "email_en";
	public static final String TAG_HOTELICON = "thumbnail";

	// Dialog elements
	private Double lon, lat;
	private String title_en, address_en, phone, fax, email_en;
	private Service service;
	private Context context;
	private LinearLayout lnUserList;
	private ListView lvUserList;
	private EditText txtSearchList;

	/** Demonstrates customizing the info window and/or its contents. */
	class CustomInfoWindowAdapter implements InfoWindowAdapter {

		// These a both viewgroups containing an ImageView with id "badge" and
		// two TextViews with id
		// "title" and "snippet".
		private final View mWindow;

		CustomInfoWindowAdapter() {
			mWindow = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
//			myMaker = userList.g
//			render(marker, mWindow, myMaker);
			render(marker, mWindow, userList.get(0));
			return mWindow;
		}

		@Override
		public View getInfoContents(Marker marker) {
			// TODO Auto-generated method stub
			return null;
		}
		private void render(Marker marker, View view,
				User user) {

			// Init markerPoint
			markerPoints = new ArrayList<LatLng>();

			// Define elements
			TextView title = ((TextView) view.findViewById(R.id.title));
			TextView address = ((TextView) view.findViewById(R.id.address));
			TextView phone = ((TextView) view.findViewById(R.id.phone));
			TextView fax = ((TextView) view.findViewById(R.id.fax));
			TextView email = ((TextView) view.findViewById(R.id.email));
			Button btnDirection = ((Button) view
					.findViewById(R.id.btnDirection));

			// Put value to elements
//			title.setText(makerDetails.get(TAG_HOTELTITLE_EN));
//			address.setText(makerDetails.get(TAG_HOTELADDRESS_EN));
//			phone.setText("Phone: " + makerDetails.get(TAG_HOTELPHONE));
//			fax.setText("Name: " + makerDetails.get(TAG_NAMEFAX));
//			email.setText("Email: " + makerDetails.get(TAG_HOTELEMAIL_EN));

			phone.setText("Phone: " + user.getPhoneNumber());
			fax.setText("Name: " + user.getName());
			email.setText("Email: " + user.getId());

			
			btnDirection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// Adding new item to the ArrayList
					// initPointToDraw();
					// exeDrawWay();
					// drawWay();
				}
			});

			// drawWay();
		}

//		private void render(Marker marker, View view,
//				HashMap<String, String> makerDetails) {
//
//			// Init markerPoint
//			markerPoints = new ArrayList<LatLng>();
//
//			// Define elements
//			TextView title = ((TextView) view.findViewById(R.id.title));
//			TextView address = ((TextView) view.findViewById(R.id.address));
//			TextView phone = ((TextView) view.findViewById(R.id.phone));
//			TextView fax = ((TextView) view.findViewById(R.id.fax));
//			TextView email = ((TextView) view.findViewById(R.id.email));
//			Button btnDirection = ((Button) view
//					.findViewById(R.id.btnDirection));
//
//			// Put value to elements
//			title.setText(makerDetails.get(TAG_HOTELTITLE_EN));
//			address.setText(makerDetails.get(TAG_HOTELADDRESS_EN));
//			phone.setText("Phone: " + makerDetails.get(TAG_HOTELPHONE));
//			fax.setText("Name: " + makerDetails.get(TAG_NAMEFAX));
//			email.setText("Email: " + makerDetails.get(TAG_HOTELEMAIL_EN));
//
//			btnDirection.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					// Adding new item to the ArrayList
//					// initPointToDraw();
//					// exeDrawWay();
//					// drawWay();
//				}
//			});
//
//			// drawWay();
//		}

		// protected void initPointToDraw() {
		// // TODO Auto-generated method stub
		// LatLng myLocation = new LatLng(mMap.getMyLocation().getLatitude(),
		// mMap.getMyLocation().getLongitude());
		// if(USER_LOCAL==null){
		// USER_LOCAL = new LatLng(10.801216, 106.711278);
		// }else{
		// USER_LOCAL = new LatLng(USER_LOCAL.latitude+0.001,
		// USER_LOCAL.longitude+0.001);
		// }
		// LatLng desLocation = USER_LOCAL;
		//
		// // add to marker array
		// for (int i = 0; i <= 1; i++) {
		// switch (i) {
		// case 0:
		// // Creating MarkerOptions
		// MarkerOptions options = new MarkerOptions();
		// markerPoints.add(myLocation);
		// // Setting the position of the marker
		// options.position(myLocation);
		// options.icon(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		// // Add new marker to the Google Map Android API V2
		// mMap.addMarker(options);
		// break;
		//
		// case 1:
		// // Creating MarkerOptions
		// MarkerOptions options2 = new MarkerOptions();
		// markerPoints.add(desLocation);
		// // Setting the position of the marker
		// options2.position(desLocation);
		// options2.icon(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_RED));
		// // Add new marker to the Google Map Android API V2
		// mMap.addMarker(options2);
		// break;
		//
		// default:
		// break;
		// }
		// }
		//
		// }
		//
		// // public boolean isUdapte = true;
		//
		// public Handler handleUpdateUserLocation = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		//
		// Log.d("handleUpdateUserLocation","update point");
		// initPointToDraw();
		// exeDrawWay();
		//
		// }
		// };
		//
		// public void updateUserLocation() {
		// new Timer().schedule(new TimerTask() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// handleUpdateUserLocation.sendEmptyMessage(1);
		// if (isUpdate) {
		// updateUserLocation();
		// }
		// }
		// }, 1000);
		// }
		//
		// private void drawWay() {
		// markerPoints.clear();
		// mMap.clear();
		// updateUserLocation();
		// }
		//
		// private void exeDrawWay() {
		// // TODO Auto-generated method stub
		// // Checks, whether start and end locations are captured
		// if (markerPoints.size() >= 2) {
		// LatLng origin = markerPoints.get(0);
		// LatLng dest = markerPoints.get(1);
		//
		// // Getting URL to the Google Directions API
		// String url = getDirectionsUrl(origin, dest);
		//
		// DownloadTask downloadTask = new DownloadTask();
		//
		// // Start downloading json data from Google Directions
		// // API
		// downloadTask.execute(url);
		// }
		// }
	}

	protected void initPointToDraw() {
		// TODO Auto-generated method stub

		markerPoints = new ArrayList<LatLng>();
		LatLng myLocation = null;
		if (mMap.getMyLocation() != null) {
			myLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap
					.getMyLocation().getLongitude());
		} else {
			myLocation = HANGXANH;
		}
		if (USER_LOCAL == null) {
			USER_LOCAL = new LatLng(10.801216, 106.711278);
		} else {
			USER_LOCAL = new LatLng(USER_LOCAL.latitude + 0.001,
					USER_LOCAL.longitude + 0.001);
		}
		LatLng desLocation = USER_LOCAL;

		// add to marker array
		for (int i = 0; i <= 1; i++) {
			switch (i) {
			case 0:
				// Creating MarkerOptions
				MarkerOptions options = new MarkerOptions();
				markerPoints.add(myLocation);
				// Setting the position of the marker
				options.position(myLocation);
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				// Add new marker to the Google Map Android API V2
				mMap.addMarker(options);
				break;

			case 1:
				// Creating MarkerOptions
				MarkerOptions options2 = new MarkerOptions();
				markerPoints.add(desLocation);
				// Setting the position of the marker
				options2.position(desLocation);
				options2.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				// Add new marker to the Google Map Android API V2
				mMap.addMarker(options2);
				break;

			default:
				break;
			}
		}

	}

	String getDirectionsUrl(LatLng origin, LatLng dest) {

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
			if (result != null) {

				try {

					ArrayList<LatLng> points = null;
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

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}
	}

	public void goBack(View v) {
		MapsActivity.this.finish();
	}

	private void addMarkersToMap() {

		// define LetLng variable
		// change LatLng variable to value get from server

		// Uses a custom icon.
		mSydney = mMap.addMarker(new MarkerOptions()
				.position(USER_LOCAL)
				.title("Thien")
				.snippet("snippet")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

	}

	private void initUserList() {
		// TODO Auto-generated method stub
		UserAdapter userAdapter = new UserAdapter(MapsActivity.this, userList);

		lvUserList.setAdapter(userAdapter);
	}

	boolean isUpdate = true;
	private Button btnStop;

	public void onStopClicked(View v) {
		if (btnStop.getText().toString().equals("Stop")) {
			isUpdate = false;
			btnStop.setText("Start");
		} else {
			isUpdate = true;
			btnStop.setText("Stop");
		}
	}

	public void onMenuClicked(View v) {
		if (lnUserList.getVisibility() == View.VISIBLE) {
			lnUserList.setVisibility(View.GONE);
		} else {
			lnUserList.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps_main);

		lnUserList = (LinearLayout) findViewById(R.id.lnUserList);
		lvUserList = (ListView) findViewById(R.id.lvUserList);
		txtSearchList = (EditText) findViewById(R.id.txtSearchList);
		btnStop = (Button) findViewById(R.id.btnStop);
		userList = new ArrayList();
		userPositionList = new HashMap<String, ArrayList<LatLng>>();

		ImageLoader imgLoader = new ImageLoader(this);
		int loader = R.drawable.ic_launcher;
//		myMaker = new HashMap<String, String>();
		Bundle bundle = getIntent().getBundleExtra(TAG_BUNDLEBRANCH);

		if (bundle != null && bundle.getString(TAG_HOTELTITLE_EN) != null) {

//			myMaker.put(TAG_HOTELTITLE_EN, bundle.getString(TAG_HOTELTITLE_EN));
//			myMaker.put(TAG_HOTELADDRESS_EN,
//					bundle.getString(TAG_HOTELADDRESS_EN));
//			myMaker.put(TAG_HOTELPHONE, bundle.getString(TAG_HOTELPHONE));
//			myMaker.put(TAG_NAMEFAX, bundle.getString(TAG_NAMEFAX));
//			myMaker.put(TAG_HOTELEMAIL_EN, bundle.getString(TAG_HOTELEMAIL_EN));

			try {

				if (bundle.getString(TAG_HOTELLAT) != null
						&& bundle.getString(TAG_HOTELLON) != null) {
					Double lat = Double.parseDouble(bundle
							.getString(TAG_HOTELLAT));
					Double lon = Double.parseDouble(bundle
							.getString(TAG_HOTELLON));
					USER_LOCAL = new LatLng(lat, lon);
				} else {
					USER_LOCAL = new LatLng(10.801216, 106.711278);
					Toast.makeText(this, "Cannot find location",
							Toast.LENGTH_SHORT).show();
				}

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			// ERROR HANDLING - put lat lng of default location in case no data
			// found
			USER_LOCAL = new LatLng(10.801216, 106.711278);
			Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
		}
		setUpMapIfNeeded();
		service = new Service(MapsActivity.this);
		context = MapsActivity.this;
		getUserData();
		initUserList();
		// initPointToDraw();
		drawWay();

		lvUserList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						switch (position) {
						case 0:
							mMap.moveCamera(CameraUpdateFactory
									.newLatLng(MARKER1));
							break;
						case 1:
							mMap.moveCamera(CameraUpdateFactory
									.newLatLng(MARKER2));
							break;
						case 2:
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									HANGXANH, 20));
							break;
						case 3:
							mMap.moveCamera(CameraUpdateFactory
									.newLatLng(MARKER4));
							break;
						case 4:
							mMap.moveCamera(CameraUpdateFactory
									.newLatLng(MARKER3));
							break;
						default:
							return;
						}
					}
				});
		lvUserList
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, final int position, long id) {
						// AlertDialog.Builder dialogBuilder = new
						// AlertDialog.Builder(MapsActivity.this);
						// dialogBuilder.setMessage("Do you want to delete "+lvUserList.getItemAtPosition(position)+" ?");
						// final UserAdapter userAdapter;
						// dialogBuilder.setPositiveButton("Yes",new
						// View.OnClickListener() {
						//
						// @Override
						// public void onClick(View v) {
						// // TODO Auto-generated method stub
						// userList.remove(position);
						// userAdapter = new UserAdapter(MapsActivity.this,
						// userList);
						// lvUserList.setAdapter(userAdapter);
						// Log.d("Listview", "userArrayList ====>" + userList);
						// }
						// });
						// dialogBuilder.setNegativeButton("No", new
						// View.OnClickListener() {
						//
						// @Override
						// public void onClick(View v) {
						// // TODO Auto-generated method stub
						//
						// }
						// });
						// AlertDialog dialog = dialogBuilder.create();
						// dialog.show();
						return true;
					}
				});
		txtSearchList.addTextChangedListener(new TextWatcher() {

			private ArrayList<User> arraySort;
			private UserAdapter userAdapter;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (!txtSearchList.getText().toString().equalsIgnoreCase("")) {
					arraySort = new ArrayList<User>();
					arraySort.clear();
					String text = txtSearchList.getText().toString();
					for (int i = 0; i < userList.size(); i++) {
						// if(globalconstant.mosq_list.get(globalconstant.hashformosq.get(globalconstant.tempList.get(i))).name.toUpperCase().toString().contains(text.toUpperCase())){
						if (userList.get(i).getName().toUpperCase().toString()
								.contains(text.toUpperCase())) {
							arraySort.add(userList.get(i));
						}
					}
					userAdapter = new UserAdapter(MapsActivity.this, arraySort);
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
				if (txtSearchList.getText().toString().equalsIgnoreCase("")) {
					userAdapter = new UserAdapter(MapsActivity.this, userList);
					lvUserList.setAdapter(userAdapter);
				}
			}
		});

	}

	private void getUserData() {
		// TODO Auto-generated method stub
		if (isSanbox) {
			initHardCode();
			// drawWay();
		} else {
			service.getUserData();
		}

	}

	private void initHardCode() {
		// TODO Auto-generated method stub
		for (int i = 0; i< 1; i++) {
			if (i == 0) {
				ArrayList<LatLng> userPositionList = new ArrayList<LatLng>();
				userPositionList.add(MARKER1);
				userPositionList.add(MARKER2);
				userPositionList.add(MARKER3);
				userPositionList.add(MARKER4);
				User user = new User("" + i, "Thien Dam", " ho Chi minh",
						HANGXANH, userPositionList);
				this.userPositionList.put("" + i, userPositionList);
				userList.add(user);
			} else {
				ArrayList<LatLng> userPositionList = new ArrayList<LatLng>();
				userPositionList.add(MARKER3);
				userPositionList.add(MARKER4);
				User user = new User("" + i, "Dam Thien", "Sai Gon", MARKER3,
						userPositionList);
				this.userPositionList.put("" + i, userPositionList);
				userList.add(user);
			}

		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		// USER_LOCAL = new LatLng(10.72277, 106.710235);

		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(true);

		// Enable MyLocation Button in the Map
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// Add lots of markers to the map.
		addMarkersToMap();

		// Setting an info window adapter allows us to change the both the
		// contents and look of the
		// info window.
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

		// Set listeners for marker events. See the bottom of this class for
		// their behavior.
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);

		// Pan to see all markers in view.
		// Cannot zoom to bounds until the map has a size.
		final View mapView = getSupportFragmentManager().findFragmentById(
				R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@SuppressWarnings("deprecation")
						// We use the new method when supported
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							LatLngBounds bounds = new LatLngBounds.Builder()
									.include(USER_LOCAL).build();
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								mapView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								mapView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}
							mMap.moveCamera(CameraUpdateFactory
									.newLatLngBounds(bounds, 50));
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									USER_LOCAL, 15));

							// Data will be loaded from sqlite database and
							// added to Hashmap
//							myMaker.put(TAG_HOTELTITLE_EN,
//									myMaker.get(TAG_HOTELTITLE_EN));
//							myMaker.put(TAG_HOTELADDRESS_EN,
//									myMaker.get(TAG_HOTELADDRESS_EN));
//							myMaker.put(TAG_HOTELPHONE,
//									myMaker.get(TAG_HOTELPHONE));
//							myMaker.put(TAG_NAMEFAX, myMaker.get(TAG_NAMEFAX));
//							myMaker.put(TAG_HOTELEMAIL_EN,
//									myMaker.get(TAG_HOTELEMAIL_EN));
						}
					});
		}
	}

	//
	// Marker related listeners.
	//

	@Override
	public boolean onMarkerClick(final Marker marker) {
		// This causes the marker at Perth to bounce into position when it is
		// clicked.
		// We return false to indicate that we have not consumed the event and
		// that we wish
		// for the default behavior to occur (which is for the camera to move
		// such that the
		// marker is centered and for the marker's info window to open, if it
		// has one).
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		showMarkerInfo(marker);
	}

	public void showMarkerInfo(Marker marker) {
		// TODO Auto-generated method stub

		// Toast.makeText(getBaseContext(), "Click Info Window",
		// Toast.LENGTH_SHORT).show();

		markerPoints.clear();
		mMap.clear();

		// Adding new item to the ArrayList
		LatLng myLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap
				.getMyLocation().getLongitude());
		LatLng desLocation = USER_LOCAL;

		// add to marker array
		for (int i = 0; i <= 1; i++) {
			switch (i) {
			case 0:
				// Creating MarkerOptions
				MarkerOptions options = new MarkerOptions();
				markerPoints.add(myLocation);
				// Setting the position of the marker
				options.position(myLocation);
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				// Add new marker to the Google Map Android API V2
				mMap.addMarker(options);
				break;

			case 1:
				// Creating MarkerOptions
				MarkerOptions options2 = new MarkerOptions();
				markerPoints.add(desLocation);
				// Setting the position of the marker
				options2.position(desLocation);
				options2.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				// Add new marker to the Google Map Android API V2
				mMap.addMarker(options2);
				break;

			default:
				break;
			}
		}

		// Checks, whether start and end locations are captured
		if (markerPoints.size() >= 2) {
			LatLng origin = markerPoints.get(0);
			LatLng dest = markerPoints.get(1);

			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(origin, dest);

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
		}

	}

	@Override
	public void onMarkerDragStart(Marker marker) {
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
	}

	@Override
	public void onMarkerDrag(Marker marker) {
	}

	public void onBack(View v) {
		finish();
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetUserData) {
			userList = (ArrayList<User>) result.getData();
			getUserPositionList();
		} else if (!result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetUserData) {
			Toast.makeText(context, "get user data fail \ntry again!",
					Toast.LENGTH_SHORT).show();
		} else if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetUserPositionList) {
			userPositionList = (HashMap<String, ArrayList<LatLng>>) result
					.getData();
			// drawWay();
		} else if (!result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetUserPositionList) {
			Toast.makeText(context, "get user data fail \ntry again!",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void drawWay() {
		// TODO Auto-generated method stub
		// markerPoints.clear();
		// mMap.clear();
		updateUserLocation();
	}

	public Handler handleUpdateUserLocation = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// int value = msg.arg1;
			// String string = (String)msg.obj;
			Log.d("handleUpdateUserLocation", "update point");
			// initPointToDraw();
			exeDrawWay();

		}
	};
	public boolean isUdapte = true;

	public void updateUserLocation() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handleUpdateUserLocation.sendEmptyMessage(1);
				if (isUpdate) {
					updateUserLocation();
				}
			}
		}, 1000);
	}

	private void exeDrawWay() {
		// TODO Auto-generated method stub
		int userSize = userList.size();
		for (User user : userList) {
			String id = user.getId();
			ArrayList<LatLng> userPoints = userPositionList.get(id);
			if (userPoints != null) {
				if (userPoints.size() >= 2) {
					int size = userPoints.size();
					
					LatLng myLocation = null;
					if (mMap.getMyLocation() != null) {
						myLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap
								.getMyLocation().getLongitude());
					} else {
						myLocation = userPoints.get(size - 1);;

					}
					LatLng origin = userPoints.get(size - 2);
//					LatLng dest = userPoints.get(size - 1);
					LatLng dest = myLocation;
					
					mMap.addMarker(new MarkerOptions()
					.position(myLocation)
					.title(user.getName())
					.snippet(user.getPhoneNumber())
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		
					// Getting URL to the Google Directions API
					String url = getDirectionsUrl(origin, dest);

					DownloadTask downloadTask = new DownloadTask();

					// Start downloading json data from Google Directions
					// API
					downloadTask.execute(url);
				}
			}
		}

		// if (markerPoints.size() >= 2) {
		// int size = markerPoints.size();
		// LatLng origin = markerPoints.get(size-2);
		// LatLng dest = markerPoints.get(size-1);
		//
		// // Getting URL to the Google Directions API
		// String url = getDirectionsUrl(origin, dest);
		//
		// DownloadTask downloadTask = new DownloadTask();
		//
		// // Start downloading json data from Google Directions
		// // API
		// downloadTask.execute(url);
		// }
	}

	public void drawDirection(LatLng origin, LatLng dest) {

		Log.d("drawDirection", "drawDirection");
		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(origin, dest);

		DownloadTask downloadTask = new DownloadTask();

		// Start downloading json data from Google Directions
		// API
		downloadTask.execute(url);
	}

	private void getUserPositionList() {
		// TODO Auto-generated method stub
		service.getUserPositionList();
	}

	public void onAddUserClicked(View v) {
		Intent i = new Intent(MapsActivity.this, AddUserActivity.class);
		startActivity(i);
	}
}
