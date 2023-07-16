package com.openclassrooms.SafetyNetAlerts.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SafetyRepository {

	private List<Person> people;

	public SafetyRepository(List<Person> people) {
		this.people = people;
	}
	
    public  Map<String, List<Map<String, Object>>> loadData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();  
            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
                    new File("./src/main/resources/data.json"),
                    new TypeReference<Map<String, List<Map<String, Object>>>>() {});
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        return null;
        }
       
    }

	public List<Person> extractPeople(Map<String, List<Map<String, Object>>> data) {
		List<Map<String, Object>> personDataList = data.get("persons");
		List<Person> people = new ArrayList<>();

		for (Map<String, Object> personData : personDataList) {
			Person person = new Person();
			person.setFirstName((String) personData.get("firstName"));
			person.setLastName((String) personData.get("lastName"));
			person.setAddress((String) personData.get("address"));
			person.setCity((String) personData.get("city"));
			person.setZip((String) personData.get("zip"));
			person.setPhone((String) personData.get("phone"));
			person.setEmail((String) personData.get("email"));
			people.add(person);
		}
		return people;
	}

	public List<Firestation> extractFirestations(Map<String, List<Map<String, Object>>> data) {
		List<Map<String, Object>> firestationDataList = data.get("firestations");
		List<Firestation> firestations = new ArrayList<>();

		for (Map<String, Object> firestationData : firestationDataList) {
			Firestation firestation = new Firestation();
			firestation.setAddress((String) firestationData.get("address"));
			firestation.setStation((String) firestationData.get("station"));
			firestations.add(firestation);
		}
		return firestations;
	}

	public List<MedicalRecord> extractMedicalRecords(Map<String, List<Map<String, Object>>> data) {
	    List<Map<String, Object>> medicalRecordDataList = data.get("medicalrecords");
	    List<MedicalRecord> medicalRecords = new ArrayList<>();

	    for (Map<String, Object> medicalRecordData : medicalRecordDataList) {
	        MedicalRecord medicalRecord = new MedicalRecord();
	        medicalRecord.setFirstName((String) medicalRecordData.get("firstName"));
	        medicalRecord.setLastName((String) medicalRecordData.get("lastName"));
	        
	        // Conversion de la chaîne de caractères en LocalDate
	        String birthdateString = (String) medicalRecordData.get("birthdate");
	        LocalDate birthdate = LocalDate.parse(birthdateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	        medicalRecord.setBirthdate(birthdate);
	        
	        medicalRecord.setMedications((List<String>) medicalRecordData.get("medications"));
	        medicalRecord.setAllergies((List<String>) medicalRecordData.get("allergies"));
	        medicalRecords.add(medicalRecord);
	    }
	    return medicalRecords;
	}
}
