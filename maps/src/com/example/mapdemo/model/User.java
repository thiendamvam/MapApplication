package com.example.mapdemo.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class User {
	private String id;
	private String name;
	private String password;
	private String phoneNumber;
	private String address;
	private LatLng currentLatLng;
	private String type;
	private ArrayList<LatLng> historyLatLng;

	public User(String id, String password, String name, String phoneNumber,String address,
			LatLng currentItem, ArrayList<LatLng> latLng, String type) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.currentLatLng = currentItem;
		this.historyLatLng = latLng;
		this.type = type;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the historyLatLng
	 */
	public ArrayList<LatLng> getHistoryLatLng() {
		return historyLatLng;
	}
	/**
	 * @param historyLatLng the historyLatLng to set
	 */
	public void setHistoryLatLng(ArrayList<LatLng> historyLatLng) {
		this.historyLatLng = historyLatLng;
	}
	/**
	 * @return the currentLatLng
	 */
	public LatLng getCurrentLatLng() {
		return currentLatLng;
	}
	/**
	 * @param currentLatLng the currentLatLng to set
	 */
	public void setCurrentLatLng(LatLng currentLatLng) {
		this.currentLatLng = currentLatLng;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
