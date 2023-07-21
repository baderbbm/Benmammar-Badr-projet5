package com.openclassrooms.SafetyNetAlerts.model.dto;

import java.util.List;

public class ResidentInfo {

	private String firstName;
	private String lastName;
	private String adress;
	private String email;
	private int age;
	private List<String> medications;
	private List<String> allergies;

	public ResidentInfo(String firstName, String lastName, String adress, String email, int age,
			List<String> medications, List<String> allergies) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.adress = adress;
		this.email = email;
		this.age = age;
		this.medications = medications;
		this.allergies = allergies;
	}

	public ResidentInfo() {

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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
