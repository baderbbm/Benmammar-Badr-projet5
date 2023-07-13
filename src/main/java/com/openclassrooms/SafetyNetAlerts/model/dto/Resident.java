package com.openclassrooms.SafetyNetAlerts.model.dto;

import java.util.List;

public class Resident {
    private String firstName;
    private String lastName;
    private String phone;
    private String station;
    private int age;
    private List<String> medications;
    private List<String> allergies;
    
    public Resident(String firstName, String lastName, String phone, String station, int age, List<String> medications,
			List<String> allergies) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.station = station;
		this.age = age;
		this.medications = medications;
		this.allergies = allergies;
	}
	
	
	public Resident(String firstName, String lastName, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
	}

	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	
 
    
	public Resident() {

	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<String> getMedications() {
		return medications;
	}
	public void setMedications(List<String> medications) {
		this.medications = medications;
	}
	public List<String> getAllergies() {
		return allergies;
	}
	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}
}