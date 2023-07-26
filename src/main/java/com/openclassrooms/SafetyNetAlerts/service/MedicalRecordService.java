package com.openclassrooms.SafetyNetAlerts.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;

import java.util.List;

@Service
public class MedicalRecordService {
    private List<MedicalRecord> medicalRecords;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecords = medicalRecordRepository.getMedicalRecord();
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedMedicalRecord) {
        for (MedicalRecord existingMedicalRecord : medicalRecords) {
            if (existingMedicalRecord.getFirstName().equals(firstName) && existingMedicalRecord.getLastName().equals(lastName)) {
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
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                foundMedicalRecord = medicalRecord;
                break;
            }
        }
        if (foundMedicalRecord != null) {
            medicalRecords.remove(foundMedicalRecord);
            return true;
        } else {
            return false;
        }
    }
}
