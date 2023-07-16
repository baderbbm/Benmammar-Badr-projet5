package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MedicalRecordRepository {

	private List<MedicalRecord> medicalRecord;

	public MedicalRecordRepository(List<MedicalRecord> medicalRecord) {
		this.medicalRecord = medicalRecord;
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
