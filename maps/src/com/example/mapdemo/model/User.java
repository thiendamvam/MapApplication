package com.example.mapdemo.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class User {
	private String id;
	private String name;
	private String phoneNumber;
	private LatLng currentLatLng;
	private ArrayList<LatLng> historyLatLng;

	public User(String string, String string2, String string3,
			LatLng currentItem, ArrayList<LatLng> latLng) {
		// TODO Auto-generated constructor stub
		this.id = string;
		this.name = string2;
		this.phoneNumber = string3;
		this.currentLatLng = currentItem;
		this.historyLatLng = latLng;
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
}
