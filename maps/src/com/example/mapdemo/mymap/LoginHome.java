package com.example.mapdemo.mymap;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapdemo.R;
import com.example.mapdemo.model.ResponseData;
import com.example.mapdemo.model.User;
import com.example.mapdemo.service.IServiceListenerJson;
import com.example.mapdemo.service.jsonservice.Service;
import com.example.mapdemo.service.jsonservice.ServiceAction;
import com.example.mapdemo.service.jsonservice.ServiceResponse;
import com.google.android.gms.maps.model.LatLng;

public class LoginHome extends FragmentActivity implements IServiceListenerJson{
	private EditText tvUserId;
	private String userId;
	private Service service;
	private TextView tvHeader;
	private ProgressDialog dilog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.login);
		tvUserId = (EditText)findViewById(R.id.etUserId);
		service = new Service(LoginHome.this);
		tvHeader = (TextView)findViewById(R.id.tvHeaderTitle);
		tvHeader.setText("Login");
		dilog = new ProgressDialog(LoginHome.this);
	}

	public void onDoneClicked(View v){
		if(tvUserId.getText()!=null){
			userId = tvUserId.getText().toString();
			if(userId!=null){
				service.login(userId, "");
				dilog.show();
			}else{
				Toast.makeText(LoginHome.this, "Please input user id", Toast.LENGTH_LONG).show();
			}			
		}else{
			Toast.makeText(LoginHome.this, "Please input user id", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onCompleted(
			com.example.mapdemo.service.jsonservice.Service service,
			ServiceResponse result) {
		// TODO Auto-generated method stub
		if(dilog.isShowing()){
			dilog.cancel();
		}
		ResponseData responseData = (ResponseData)result.getData();
		if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionLogin) {
			String status = (String)responseData.getData();
			if(status.equals("200")){
				Intent intent  = new Intent(LoginHome.this, MapsActivity.class);
				intent.putExtra("user_id", userId);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(LoginHome.this, "Error",  Toast.LENGTH_LONG).show();
			}
		} else if (!result.isSuccess()
				&& result.getAction() == ServiceAction.ActionLogin) {
			Toast.makeText(LoginHome.this, "Can not connect to server!",  Toast.LENGTH_LONG).show();
		} 
	}
}
