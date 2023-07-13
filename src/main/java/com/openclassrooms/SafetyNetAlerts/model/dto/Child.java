package com.openclassrooms.SafetyNetAlerts.model.dto;

import java.util.List;


public class Child {
    private String firstName;
    private String lastName;
    private int age;
    private List<String> householdMembers;

    public Child() {
    
    }
    
    public Child(String firstName, String lastName, int age, List<String> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
    }
    
    public Child(String firstName, String lastName, List<String> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.householdMembers = householdMembers;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHouseholdMembers() {
        return householdMembers;
    }

    public void setHouseholdMembers(List<String> householdMembers) {
        this.householdMembers = householdMembers;
    }
    
    
    /*
	@Override
	public String toString() {
		return "[firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", householdMembers="
				+ householdMembers + "]";
	}
	
	
	@Override
	public String toString() {
		return "[firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
	}
	*/
}
