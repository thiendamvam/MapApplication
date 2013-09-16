package com.example.mapdemo.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.example.mapdemo.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Service implements Runnable {

	private static final String TAG = Service.class.getSimpleName();
	private HttpURLConnection _connection;
	private ServiceAction _action;
	private ArrayList<IServiceListener> _listener;
	private boolean _connecting;
	private Thread _thread;
	private String _actionURI;
	private Map<String, String> _params;
	private boolean _includeHost;
	private boolean _isGet;
	private Service _service;
	private boolean _isBitmap;
	private HttpClient httpclient;

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			for (IServiceListener listener : _listener) {
				if (listener != null)
					listener.onCompleted(_service, (ServiceResponse) msg.obj);
			}

		}
	};

	public Service(IServiceListener... listeners) {
		_action = ServiceAction.ActionNone;
		_listener = new ArrayList<IServiceListener>();
		if (listeners != null)
			for (IServiceListener listener : listeners) {
				_listener.add(listener);
			}
		_connecting = false;
		_includeHost = true;
		_service = this;
		_isBitmap = false;
	}

	public void addListener(IServiceListener listener) {
		_listener.add(listener);
	}

	public boolean isConnecting() {
		return _connecting;
	}

	public void stop() {
		// cleanUp();
		if (httpclient != null)
			httpclient.getConnectionManager().shutdown();
		_action = ServiceAction.ActionNone;
		_connecting = false;
	}

	private boolean request(String uri, Map<String, String> params,
			boolean includeHost, boolean isGet) {
		_isGet = isGet;
		request(uri, params, includeHost);
		return true;
	}

	private boolean request(String uri, Map<String, String> params,
			boolean includeHost) {
		if (_connecting)
			return false;
		_connecting = true;
		_actionURI = uri;
		_params = params;
		_includeHost = includeHost;
		_thread = new Thread(this);
		_thread.start();
		return true;
	}

	private void processError(ResultCode errorCode) {
		// if (_listener == null || _action == ServiceAction.ActionNone
		// || !_connecting)
		// return;
		Message msg = handler.obtainMessage(0, new ServiceResponse(_action,
				null, errorCode));
		handler.sendMessage(msg);
	}

	private void dispatchResult(String result) {
		if (_listener == null || _action == ServiceAction.ActionNone
				|| !_connecting)
			return;

		ServiceAction act = _action;
		Object resObj = null;
		ServiceResponse response = null;
		DataParser parser = new DataParser();
		switch (act) {
		// TODO
		case ActionNone:
			break;

		case ActionGetUserPositionList:
			resObj = parser.parserUserPositionResult();
			break;
		case ActionAllUser:
			resObj = parser.parserUserListResult(result);
			break;
		case ActionUpdateUserLocation:
			resObj = parser.parserUpdateLocationResult(result);
			break;
		case ActionAddUser:
			resObj = parser.parserAddUserResult(result);
			break;
		case ActionLogin:
			resObj = parser.parserLoginResult(result);
			break;
		default:
			resObj = result;
			break;
		}
		if (resObj == null)
			response = new ServiceResponse(act, null, ResultCode.Failed);
		else
			response = new ServiceResponse(act, resObj);
		stop();
		Message msg = handler.obtainMessage(0, response);
		handler.sendMessage(msg);
	}

	private void dispatchResult(Bitmap result) {
		if (_listener == null || _action == ServiceAction.ActionNone
				|| !_connecting)
			return;
		ServiceAction act = _action;
		ServiceResponse response = null;
		if (result == null)
			response = new ServiceResponse(act, null, ResultCode.Failed);
		else
			response = new ServiceResponse(act, result);
		stop();
		Message msg = handler.obtainMessage(0, response);
		handler.sendMessage(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Log.d(_action.toString(), "=========== run ===========\n" + _actionURI);
		httpclient = new DefaultHttpClient();
		// AndroidHttpClient httpclient = AndroidHttpClient
		// .newInstance(_actionURI);
		HttpParams httpParameters = httpclient.getParams();
		// HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
		HttpConnectionParams.setSoTimeout(httpParameters, 600000);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);
		try {
			final String urlString;
			if (_action == ServiceAction.ActionGetLocation) {
				String textSearch = _params.get("text-search");
				textSearch = textSearch.replace(" ", "+");
				urlString = "http://ws.geonames.org/search?q=" + textSearch
						+ "&style=full&maxRows=10";
			} else if (_action == ServiceAction.ActionGetDistance) {
				urlString = _actionURI;
			} else
				urlString = _includeHost ? API.host + _actionURI : _actionURI;
			HttpRequestBase request = null;

			if (_isGet) {
				request = new HttpGet();
				if (_params != null) {
					attachUriWithQuery(request, Uri.parse(urlString), _params);
				}
			} else {
				request = new HttpPost(urlString);
				if (_params != null) {
					UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
							paramsToList(_params), HTTP.UTF_8);
					((HttpPost) request).setEntity(formEntity);
				}
			}

			// Set default headers
			HttpResponse response = httpclient.execute(request);

			InputStream in = null;

			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				Header[] header = response.getHeaders("Content-Encoding");
				if (header != null && header.length != 0) {
					for (Header h : header) {
						if (h.getName().trim().equalsIgnoreCase("gzip"))
							in = new GZIPInputStream(response.getEntity()
									.getContent());
					}
				}

				if (in == null)
					in = new BufferedInputStream(response.getEntity()
							.getContent());

				if (_isBitmap) {
					Bitmap bm = BitmapFactory.decodeStream(in);
					dispatchResult(bm);
				} else {
					String temp = convertStreamToString(in);// text.toString();
					Log.d(_action.toString(), "==" + temp + "");
					in.close();
					dispatchResult(temp);
				}

			} else if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND)
				processError(ResultCode.Failed);
			else if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_SERVER_ERROR)
				processError(ResultCode.ServerError);
			else
				processError(ResultCode.NetworkError);

		} catch (Exception e) {
			e.printStackTrace();
			processError(ResultCode.NetworkError);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	private void attachUriWithQuery(HttpRequestBase request, Uri uri,
			Map<String, String> params) {
		try {
			if (params == null) {
				// No params were given or they have already been
				// attached to the Uri.
				request.setURI(new URI(uri.toString()));
			} else {
				Uri.Builder uriBuilder = uri.buildUpon();

				// Loop through our params and append them to the Uri.
				for (BasicNameValuePair param : paramsToList(params)) {
					uriBuilder.appendQueryParameter(param.getName(),
							param.getValue());
				}

				uri = uriBuilder.build();
				request.setURI(new URI(uri.toString()));
			}
		} catch (URISyntaxException e) {
			Log.e(TAG, "URI syntax was incorrect: " + uri.toString());
		}
	}

	private static List<BasicNameValuePair> paramsToList(
			Map<String, String> params) {
		ArrayList<BasicNameValuePair> formList = new ArrayList<BasicNameValuePair>(
				params.size());

		for (String key : params.keySet()) {
			Object value = params.get(key);

			// We can only put Strings in a form entity, so we call the
			// toString()
			// method to enforce. We also probably don't need to check for null
			// here
			// but we do anyway because Bundle.get() can return null.
			if (value != null)
				formList.add(new BasicNameValuePair(key, value.toString()));
		}

		return formList;
	}

	private String convertStreamToString(InputStream is) {
		// TODO Auto-generated method stub

		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				return "";
			} finally {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
			return writer.toString();
		} else {
			return "";
		}

	}

	public int getConnectionTimeout() {
		return _connection.getConnectTimeout();
	}

	public void getBillDetail(String billId, String encryptedToken) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetBillDetail;
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", encryptedToken);
		params.put("bill_id", billId);
		request("/m/member/order/show", params, true, false);
	}

	public void getUserData() {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionAllUser;
		Map<String, String> params = new HashMap<String, String>();
		request("/api/get_user_list.php", params, true, true);
	}

	public void getUserPositionList(String id) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetUserPositionList;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		request("/api/get_user_position.php", params, true, true);
	}

	public void updateLocation(String id, String lat, String lon, String address) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionUpdateUserLocation;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("lat", lat);
		params.put("lon", lon);
		params.put("address", address);
		request("/api/add_latlng.php", params, true, true);
	}

	public void login(String id, String password) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionLogin;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("password", password);
		request("/api/authen.php", params, true, true);
	}

	public void addUser(String id, String password, String name,
			String phoneNumber, String address, String type) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionAddUser;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("password", password);
		params.put("name", name);
		params.put("phone_number", phoneNumber);
		params.put("address", address);
		params.put("type", type);
		request("/api/add_user.php", params, true, true);
	}
}
