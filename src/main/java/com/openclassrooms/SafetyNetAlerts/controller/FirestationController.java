package com.openclassrooms.SafetyNetAlerts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.service.FirestationService;

@RestController
public class FirestationController {
    private FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    // Ajouter un mapping caserne/adresse
    @PostMapping("/firestation")
    public ResponseEntity<Firestation> addFirestation(@RequestParam("address") String address,
                                                      @RequestParam("station") String station) {
    	
        Firestation firestation = new Firestation(address, station);
        Firestation addedFirestation = firestationService.addFirestation(firestation);
        if (addedFirestation != null) {
            return ResponseEntity.ok(addedFirestation);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mettre à jour le numéro de la caserne de pompiers d'une adresse
    @PutMapping("/firestation")
    public ResponseEntity<Firestation> updateFirestation(@RequestParam("address") String address,
            @RequestParam("station") String station) {
        Firestation firestation = new Firestation(address, station);
        Firestation updatedFirestation = firestationService.updateFirestation(firestation);
        if (updatedFirestation != null) {
            return ResponseEntity.ok(updatedFirestation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer le mapping d'une caserne ou d'une adresse
    @DeleteMapping("/firestation")
    public ResponseEntity<Void> deleteFirestation(@RequestParam("address") String address,
                                                  @RequestParam("station") String station) {
        boolean success = firestationService.deleteFirestation(address, station);
        if (success) {
        	return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
