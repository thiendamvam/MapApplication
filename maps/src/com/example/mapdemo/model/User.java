package com.example.mapdemo.model;

import com.google.android.gms.maps.model.LatLng;

public class User {
	private String id;
	private String name;
	private String phoneNumber;
	private LatLng latLng;
	/**
	 * @return the latLng
	 */
	public LatLng getLatLng() {
		return latLng;
	}
	/**
	 * @param latLng the latLng to set
	 */
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
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
}
