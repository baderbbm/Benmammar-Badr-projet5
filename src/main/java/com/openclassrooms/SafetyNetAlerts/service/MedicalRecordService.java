package com.openclassrooms.SafetyNetAlerts.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;

import java.util.List;

@Service
public class MedicalRecordService {
	private MedicalRecordRepository  medicalRecordRepository ;


	public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
		this.medicalRecordRepository=medicalRecordRepository;
	}

	public List<MedicalRecord> getMedicalRecords() {
		return medicalRecordRepository.getMedicalRecord();
	}

	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
		getMedicalRecords().add(medicalRecord);
		return medicalRecord;
	}

	public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedMedicalRecord) {
		for (MedicalRecord existingMedicalRecord : getMedicalRecords()) {
			if (existingMedicalRecord.getFirstName().equals(firstName)
					&& existingMedicalRecord.getLastName().equals(lastName)) {
				existingMedicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
				existingMedicalRecord.setMedications(updatedMedicalRecord.getMedications());
				existingMedicalRecord.setAllergies(updatedMedicalRecord.getAllergies());
				return existingMedicalRecord;
			}
		}
		return null;
	}

	public boolean deleteMedicalRecord(String firstName, String lastName) {
		MedicalRecord foundMedicalRecord = null;
		for (MedicalRecord medicalRecord : getMedicalRecords()) {
			if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
				foundMedicalRecord = medicalRecord;
				break;
			}
		}
		if (foundMedicalRecord != null) {
			getMedicalRecords().remove(foundMedicalRecord);
			return true;
		} else {
			return false;
		}
	}
}
