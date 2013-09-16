package com.example.mapdemo.mymap;

/**
 * 
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mapdemo.R;
import com.example.mapdemo.model.User;
import com.example.mapdemo.service.IServiceListener;
import com.example.mapdemo.service.Service;
import com.example.mapdemo.service.ServiceResponse;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Linh Nguyen This activity supports to create new Perming account.
 */
public class AddUserActivity extends Activity implements TextWatcher,
		IServiceListener {
	private static final int GET_IMAGE = 0;
	// Button createAccount;
//	EditText email;
	EditText name;
	EditText phoneNumber;
	EditText address;
	private SharedPreferences prefs;
	private Button createAccount;
	private ProgressDialog dialog;
	private AddUserActivity context;
	private View progressBar;
	private String nameValue;
	private String phoneNumberValue;
	private String notify;
	private EditText useremail;
	private Service service;
	private String emailValue;
	private String addresValue;
	private EditText password;
	private EditText userId;
	private String passwordValue;
	private String userIdValue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_user);

		context = AddUserActivity.this;
		service = new Service(this);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		dialog = new ProgressDialog(context);
//		email = (EditText) findViewById(R.id.etEmail);
		userId = (EditText)findViewById(R.id.etUserId);
		password = (EditText)findViewById(R.id.etPassword);
		name = (EditText) findViewById(R.id.etName);
		// email.addTextChangedListener(this);
		// nickemail = (EditText) findViewById(R.id.nickemail);
		phoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
		// phoneNumber.addTextChangedListener(this);
		address = (EditText) findViewById(R.id.etAddress);
		// address.addTextChangedListener(this);
		// confirmaddress.addTextChangedListener(this);
		progressBar = (ProgressBar) findViewById(R.id.prgBar);
		createAccount = (Button) findViewById(R.id.btnDone);
		createAccount.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (checkInputData()) {
					showDialog();
					exeSignup();
				} else {
					// Util.showDialog(context, notify);
				}
			}
		});

	}

	protected void exeSignup() {
		User user = new User(userIdValue, passwordValue,nameValue, phoneNumberValue, addresValue, null, new ArrayList<LatLng>(),"0");
		MapsActivity.userList.add(user);
		Toast.makeText(AddUserActivity.this, "Add user successful", Toast.LENGTH_LONG).show();
		hideDialog();
	}

	protected boolean checkInputData() {
		// TODO Auto-generated method stub
//		emailValue = email.getText().toString();
		userIdValue = userId.getText().toString();
		passwordValue = password.getText().toString();
		nameValue = name.getText().toString();
		phoneNumberValue = phoneNumber.getText().toString();
		addresValue = address.getText().toString();
		notify = "";
		boolean status = true;
		// if(emailValue.equals("")){
		// notify+="\nInput First email";
		// status = false;
		// email.setError("Please input user email");
		//
		// }
		if (nameValue.equals("")) {
			notify += "Input user name";
			status = false;
			name.setError("Please input user name");
		}
		if (phoneNumberValue.equals("")) {
			notify += "Input phoneNumber";
			status = false;
			phoneNumber.setError("Please input user phoneNumber");
		}
		if (addresValue.equals("")) {
			notify += "Input address";
			status = false;
			address.setError("Please input user address");
		}
		if (userIdValue.equals("")) {
			notify += "Input address";
			status = false;
			address.setError("Please input user id");
		}
		if (passwordValue.equals("")) {
			notify += "Input address";
			status = false;
			address.setError("Please input user password");
		}
		// else{
		// if(!Util.isValidEmail(emailValue)){
		// notify+="Your email format is not correct. Please try again";
		// status = false;
		// phoneNumber.setError("user email format is not correct. Please try again");
		//
		// }
		// }
		// if(useremailValue.equals("")){
		// notify+="Input Useremail";
		// status = false;
		// useremail.setError("Please input your useremail");
		// }

		return status;
	}

	public static int copyStream(InputStream input, OutputStream output)
			throws IOException {
		byte[] stuff = new byte[1024];
		int read = 0;
		int total = 0;
		while ((read = input.read(stuff)) != -1) {
			output.write(stuff, 0, read);
			total += read;
		}
		return total;
	}

	private Bitmap getBitmap2(String path) {
		try {

			final int IMAGE_MAX_SIZE = 1024;// 1200000; // 1.2MP
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			InputStream in = new FileInputStream(path);
			BitmapFactory.decodeStream(in, null, o);
			in.close();
			in = new FileInputStream(path);

			// Decode image size

			double scale = 1;
			int currentWidth = o.outWidth;
			if (currentWidth > IMAGE_MAX_SIZE) {
				scale = currentWidth / IMAGE_MAX_SIZE;

			}

			Bitmap b = null;
			if (scale > 1) {

				double y = (double) o.outHeight / scale;
				double x = 1024;
				b = BitmapFactory.decodeFile(path);
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
						(int) y, true);
				b = scaledBitmap;
				System.gc();

			} else {
				b = BitmapFactory.decodeFile(path);
			}

			/*
			 * Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
			 * b.getHeight() + "");
			 */
			return b;
		} catch (Exception e) {
			// Log.e("", e.getMessage(), e);
			return null;
		}
	}

	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
//		if (email.getText().toString().equals("")) {
//			email.setError("email is required!");
//		}
		if (name.getText().toString().equals("")) {
			name.setError("email is required!");
		}
		if (phoneNumber.getText().toString().equals("")) {
			phoneNumber.setError("phoneNumber is required!");
		}
		if (address.getText().toString().equals("")) {
			address.setError("address is required!");
		}

	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	private void showDialog() {
		// dialog.show();
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideDialog() {
		// dialog.dismiss();
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBack(View v) {
		finish();
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub

	}

}
