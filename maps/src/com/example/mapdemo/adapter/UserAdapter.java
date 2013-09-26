package com.example.mapdemo.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mapdemo.R;
import com.example.mapdemo.model.User;

public class UserAdapter extends BaseAdapter {

	private  List<User> list;
	private  Activity context;
	private HashMap<String,View> viewList = new HashMap<String, View>();
	private ViewHolderSection holder;
	public static List<UserAdapter.ViewHolderSection> listSectionView = new ArrayList<UserAdapter.ViewHolderSection>();

	public UserAdapter(Activity context, ArrayList<User> userList){
		this.list = userList;
		this.context = context;
	}
	public static class ViewHolderSection {
		public TextView text;
		public Button btnStart;
		public User userData;
		public ViewHolderSection(){
			
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		User item = list.get(position);
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.user_item, null);
			final ViewHolderSection viewHolder = new ViewHolderSection();
			viewHolder.text = (TextView) view.findViewById(R.id.tvUserName);
			viewHolder.btnStart = (Button)view.findViewById(R.id.btnStartUser);
			viewHolder.btnStart.setBackgroundColor(Color.BLUE);
			view.setTag(viewHolder);
			viewHolder.text.setText(item.getId());
			viewHolder.btnStart.setTag(viewHolder);
			viewHolder.userData = item;
//			view.setBackgroundColor(Color.parseColor("#e9e9e9"));
		} else {
			view = convertView;
			holder = (ViewHolderSection) view.getTag();
		}

		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}