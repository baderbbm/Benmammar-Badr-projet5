package com.openclassrooms.SafetyNetAlerts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.service.FirestationService;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
	private FirestationService firestationService;

	public FirestationController(FirestationService firestationService) {
		this.firestationService = firestationService;
	}

	// Ajouter un mapping caserne/adresse
	@PostMapping
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
	@PutMapping
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
	@DeleteMapping
	public ResponseEntity<Void> deleteFirestation(@RequestParam(value = "address", required = false) String address,
												  @RequestParam(value = "station", required = false) String station) {
		if (address == null && station == null) {
			return ResponseEntity.badRequest().build(); // Require at least one parameter (address or station)
		}

		boolean success = false;

		if (address != null) {
			success = firestationService.deleteFirestationByAddress(address);
		} else if (station != null) {
			success = firestationService.deleteFirestationByStation(station);
		}

		if (success) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
