package com.openclassrooms.SafetyNetAlerts.model.dto;

import java.util.List;


public class FirestationCoverage {

	private List<PersonCaserne> people;
	private int adultsCount;
	private int childrenCount;

	public List<PersonCaserne> getPeople() {
		return people;
	}

	public void setPeople(List<PersonCaserne> people) {
		this.people = people;
	}

	public int getAdultsCount() {
		return adultsCount;
	}

	public void setAdultsCount(int adultsCount) {
		this.adultsCount = adultsCount;
	}

	public int getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}
}
