package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.dto.Child;
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverageResponse;
import com.openclassrooms.SafetyNetAlerts.model.dto.Resident;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentInfo;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentStation;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SafetyService {
	
    private PersonRepository personRepository; 

    public SafetyService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public FirestationCoverageResponse retrievePersonsByFirestation(String stationNumber) { 
    	 if (stationNumber.isEmpty()) {
    	        return new FirestationCoverageResponse(); 
    	    }
        return personRepository.getPeopleByAddresses(personRepository.getAddressesByStationNumber(stationNumber)); 
    }

    public List<Child> retrieveChildrenByAddress(String address) {
    	 if (address.isEmpty()) {
 	        return new ArrayList<Child>();
 	    }
        return personRepository.getChildrenByAddress(address);         
    }
   
    
    public List<String> retrievePhoneNumbersByFirestation(String firestationNumber) {
    	 if (firestationNumber.isEmpty()) {
  	        return new ArrayList<String>(); 
  	    }
    	return personRepository.getPhoneNumbersByStationNumber(firestationNumber) ;
    }
    
   
    public List<Resident> retrieveResidentsByAddress(String address) {
    	 if (address.isEmpty()) {
  	        return new ArrayList<Resident>(); 
  	    }
    	return personRepository.getResidentsByAddress(address); 
    }
   
    public List<ResidentStation> retrieveHouseholdsByStations(String stationNumbers) {
    	 if (stationNumbers.isEmpty()) {
   	        return new ArrayList<ResidentStation>(); 
   	    }
    	return personRepository.getAllResidentsByFirestation(stationNumbers);
        }
       
    
    public List<ResidentInfo> retrievePersonInfoByName(String firstName, String lastName) {
    	 if (firstName.isEmpty() && lastName.isEmpty()) {
    	        return new ArrayList<ResidentInfo>(); 
    	    }
        return personRepository.getPersonInfo(firstName, lastName);
    }
    
    public List<String> retrieveCommunityEmailsByCity(String city) {
    	 if (city.isEmpty()) {
    	        return new ArrayList<String>(); 
    	    }
        return personRepository.getCommunityEmailsByCity(city);
    }

}
