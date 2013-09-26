package com.example.mapdemo.service.jsonservice;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mapdemo.model.LatLng;
import com.example.mapdemo.model.ResponseData;
import com.example.mapdemo.model.User;

public class DataParser {

	private JSONObject _root;
	public static String issueIntro = "";
	private String issueString;

	// private StoreIssueDataControler issueControler;
	public DataParser() {
		// issueController.resetStorySetData();
	}

	public boolean parse(String input, boolean isJson) {
		try {
			issueString = input;
			if (isJson)
				_root = new JSONObject(input);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}




	public Object parserUserPositionResult() {
		// TODO Auto-generated method stub
		ArrayList<LatLng> data = new ArrayList<LatLng>();
		ResponseData responseData = new ResponseData();
		try {
			JSONArray array = _root.optJSONArray("result");
			if (array != null) {
				int length = array.length();
				try {
					for (int i = 0; i < length; i++) {
						JSONObject object = (JSONObject) array.get(i);
						LatLng item = new LatLng();
						item.setId(object.optString("id"));
						item.setLat(object.optString("lat"));
						item.setLon(object.optString("lon"));
						item.setAddress(object.optString("address"));
						data.add(item);

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		responseData.setData(data);
		return responseData;

	
	
	}

	public Object parserUserListResult(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ArrayList<User> data = new ArrayList<User>();
		ResponseData responseData = new ResponseData();
		try {
			JSONArray array = _root.optJSONArray("result");
			if (array != null) {
				int length = array.length();
				try {
					for (int i = 0; i < length; i++) {
						JSONObject object = (JSONObject) array.get(i);
						User item = new User();
						item.setId(object.optString("id"));
						item.setAddress(object.optString("password"));
						item.setName(object.optString("phone_number"));
						item.setPhoneNumber(object.optString("name"));
						item.setAddress(object.optString("address"));
						item.setAddress(object.optString("type"));
						data.add(item);

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		responseData.setData(data);
		return responseData;

	
	}

	public Object parserUpdateLocationResult(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ResponseData responseData = new ResponseData();
		ArrayList<User> data = new ArrayList<User>();
		try {
			JSONArray array = _root.optJSONArray("result");
			if (array != null) {
				int length = array.length();
				try {
					for (int i = 0; i < length; i++) {
						JSONObject object = (JSONObject) array.get(i);
						User item = new User();
						item.setId(object.optString("id"));
						item.setAddress(object.optString("password"));
						item.setName(object.optString("phone_number"));
						item.setPhoneNumber(object.optString("name"));
						item.setAddress(object.optString("address"));
						item.setAddress(object.optString("type"));
						data.add(item);

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		responseData.setData(data);
		return responseData;

	
	}

	public Object parserAddUserResult(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ArrayList<User> data = new ArrayList<User>();
		ResponseData responseData = new ResponseData();
		try {
			JSONObject object = _root.optJSONObject("result");
			responseData.setStatus(object.optString("status"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return data;

	
	}

	public Object parserLoginResult(String result) {
		// TODO Auto-generated method stub
		ArrayList<User> data = new ArrayList<User>();
		ResponseData responseData = new ResponseData();
		try {
			JSONObject object = _root.optJSONObject("result");
			responseData.setData(object.optString("status"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return responseData;
	}

	public Object parserGetUserById(String result) {
		// TODO Auto-generated method stub
		try {
			ResponseData responseData = new ResponseData();
			User item = null;
			JSONArray array = _root.optJSONArray("result");
			if (array != null) {
				int length = array.length();
				try {
					for (int i = 0; i < length; i++) {
						JSONObject object = (JSONObject) array.get(i);
						 item = new User();
						item.setId(object.optString("id"));
						item.setAddress(object.optString("password"));
						item.setName(object.optString("phone_number"));
						item.setPhoneNumber(object.optString("name"));
						item.setAddress(object.optString("address"));
						item.setAddress(object.optString("type"));
						i= length;

					}

				} catch (Exception e) {
					// TODO: handle exception
					
				}
			}
			responseData.setData(item);
			return responseData;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
