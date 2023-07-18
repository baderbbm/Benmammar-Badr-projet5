package com.openclassrooms.SafetyNetAlerts;

import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.repository.FirestationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.SafetyRepository;
import com.openclassrooms.SafetyNetAlerts.service.FirestationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class FirestationServiceTest {
	
   private FirestationService firestationService;

    @BeforeEach
    void setUp() {
        FirestationRepository firestationRepository = new FirestationRepository();
        SafetyRepository safetyRepository = new SafetyRepository();
        List<Firestation> firestations = firestationRepository.extractFirestations(safetyRepository.loadData());
        firestationService = new FirestationService(firestationRepository, safetyRepository);
        firestationService.setFirestations(firestations);
    }

    @Test
    void addFirestation() {
        // Créez une caserne d'incendie de test
        Firestation firestation = new Firestation("123 Main Street", "1");

        // Ajoutez la caserne d'incendie en utilisant la méthode addFirestation
        Firestation addedFirestation = firestationService.addFirestation(firestation);  

        // Vérifiez que la caserne d'incendie a été ajoutée avec succès
        assertNotNull(addedFirestation);
        assertTrue(firestationService.getFirestations().contains(firestation));
    }

    @Test
    void updateFirestation() {
        // Créez une caserne d'incendie existante
        Firestation existingFirestation = new Firestation("123 Main Street", "1");

        // Ajoutez la caserne d'incendie existante à la liste de casernes d'incendie du service
        List<Firestation> firestations = new ArrayList<>();
        firestations.add(existingFirestation);
        firestationService.setFirestations(firestations);

        // Créez une caserne d'incendie de test avec des informations mises à jour
        Firestation updatedFirestation = new Firestation("123 Main Street", "2");

        // Mettez à jour la caserne d'incendie en utilisant la méthode updateFirestation
        Firestation result = firestationService.updateFirestation(updatedFirestation);

        // Vérifiez que la caserne d'incendie a été mise à jour avec succès
        assertNotNull(result);
        assertEquals(updatedFirestation.getStation(), result.getStation());
    }

    @Test
    void deleteFirestation() {
        // Créez une caserne d'incendie existante
        Firestation existingFirestation = new Firestation("123 Main Street", "1");

        // Ajoutez la caserne d'incendie existante à la liste de casernes d'incendie du service
        List<Firestation> firestations = new ArrayList<>();
        firestations.add(existingFirestation);
        firestationService.setFirestations(firestations);

        // Supprimez la caserne d'incendie en utilisant la méthode deleteFirestation
        boolean success = firestationService.deleteFirestation(existingFirestation.getAddress(), existingFirestation.getStation());

        // Vérifiez que la caserne d'incendie a été supprimée avec succès
        assertTrue(success);
        assertFalse(firestationService.getFirestations().contains(existingFirestation));
    }
}
