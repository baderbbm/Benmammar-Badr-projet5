package com.openclassrooms.SafetyNetAlerts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    private MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Ajouter un dossier médical
    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("/medicalRecord - POST - Adding a new medical record: {}", medicalRecord);

        MedicalRecord addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        if (addedMedicalRecord != null) {
            logger.info("Medical record added successfully: {}", addedMedicalRecord);
            return ResponseEntity.ok(addedMedicalRecord);
        } else {
            logger.error("Failed to add medical record: {}", medicalRecord);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mettre à jour un dossier médical existant
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstName,
                                                             @PathVariable String lastName,
                                                             @RequestBody MedicalRecord medicalRecord) {
        logger.info("/medicalRecord - PUT - Updating medical record - First Name: {}, Last Name: {}, Medical Record: {}", firstName, lastName, medicalRecord);

        MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecord);
        if (updatedMedicalRecord != null) {
            logger.info("Medical record updated successfully: {}", updatedMedicalRecord);
            return ResponseEntity.ok(updatedMedicalRecord);
        } else {
            logger.warn("Medical record not found - First Name: {}, Last Name: {}", firstName, lastName);
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un dossier médical
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        logger.info("/medicalRecord - DELETE - Deleting medical record - First Name: {}, Last Name: {}", firstName, lastName);

        boolean success = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (success) {
            logger.info("Medical record deleted successfully - First Name: {}, Last Name: {}", firstName, lastName);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Medical record not found - First Name: {}, Last Name: {}", firstName, lastName);
            return ResponseEntity.notFound().build();
        }
    }
}
