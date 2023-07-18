package com.openclassrooms.SafetyNetAlerts.model.dto;

import java.util.List;


public class Child {
    private String firstName;
    private String lastName;
    private int age;
    private List<String> householdMembers;

    public Child(String firstName, String lastName, int age, List<String> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
    }
    
    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }



    public int getAge() {
        return age;
    }


    public List<String> getHouseholdMembers() {
        return householdMembers;
    }

}
