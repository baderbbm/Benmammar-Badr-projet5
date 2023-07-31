package com.openclassrooms.SafetyNetAlerts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.service.FirestationService;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    private FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    // Ajouter un mapping caserne/adresse
    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        logger.info("/firestation - POST - Adding a new firestation: {}", firestation);

        Firestation addedFirestation = firestationService.addFirestation(firestation);
        if (addedFirestation != null) {
            logger.info("Firestation added successfully: {}", addedFirestation);
            return ResponseEntity.ok(addedFirestation);
        } else {
            logger.error("Failed to add firestation: {}", firestation);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mettre à jour le numéro de la caserne de pompiers d'une adresse
    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestParam("address") String address,
                                                         @RequestParam("station") String station) {
        logger.info("/firestation - PUT - Updating firestation - Address: {}, Station: {}", address, station);

        Firestation firestation = new Firestation(address, station);
        Firestation updatedFirestation = firestationService.updateFirestation(firestation);
        if (updatedFirestation != null) {
            logger.info("Firestation updated successfully: {}", updatedFirestation);
            return ResponseEntity.ok(updatedFirestation);
        } else {
            logger.warn("Firestation not found - Address: {}, Station: {}", address, station);
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer le mapping d'une caserne ou d'une adresse
    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(@RequestParam(value = "address", required = false) String address,
                                                  @RequestParam(value = "station", required = false) String station) {
        if (address == null && station == null) {
            return ResponseEntity.badRequest().build();
        }

        logger.info("/firestation - DELETE - Deleting firestation - Address: {}, Station: {}", address, station);

        boolean success = false;

        if (address != null) {
            success = firestationService.deleteFirestationByAddress(address);
        } else if (station != null) {
            success = firestationService.deleteFirestationByStation(station);
        }

        if (success) {
            logger.info("Firestation deleted successfully - Address: {}, Station: {}", address, station);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Firestation not found - Address: {}, Station: {}", address, station);
            return ResponseEntity.notFound().build();
        }
    }
}
