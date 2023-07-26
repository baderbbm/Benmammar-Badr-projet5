package com.openclassrooms.SafetyNetAlerts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    private MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Ajouter un dossier médical
    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        if (addedMedicalRecord != null) {
            return ResponseEntity.ok(addedMedicalRecord);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mettre à jour un dossier médical existant
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstName,
                                                             @PathVariable String lastName,
                                                             @RequestBody MedicalRecord medicalRecord) {
        MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecord);
        if (updatedMedicalRecord != null) {
            return ResponseEntity.ok(updatedMedicalRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un dossier médical
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName,
                                                    @PathVariable String lastName) {
        boolean success = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
