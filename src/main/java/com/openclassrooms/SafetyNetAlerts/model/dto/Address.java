package com.openclassrooms.SafetyNetAlerts.model.dto;

import java.util.List;

public class Address {
	private String address;
	private List<ResidentStation> residentStation;
	
	public Address() {
		
	}

	public Address(String address, List<ResidentStation> residentStation) {
		this.address = address;
		this.residentStation = residentStation;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public List<ResidentStation> getResidentStation() {
		return residentStation;
	}
	public void setResidentStation(List<ResidentStation> residentStation) {
		this.residentStation = residentStation;
	}
}
