package com.openclassrooms.SafetyNetAlerts;

import com.openclassrooms.SafetyNetAlerts.controller.FirestationController;
import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.service.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirestationControllerTest {
    private FirestationService firestationService;
    private FirestationController firestationController;

    @BeforeEach
    void setUp() {
        firestationService = mock(FirestationService.class);
        firestationController = new FirestationController(firestationService);
    }

    @Test
    void addFirestation_ValidInput_ReturnsAddedFirestation() {
        String address = "123 Main Street";
        String station = "1";
        Firestation firestation = new Firestation(address, station);
        
        when(firestationService.addFirestation(any(Firestation.class))).thenReturn(firestation);

        ResponseEntity<Firestation> response = firestationController.addFirestation(firestation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(firestation, response.getBody());
        verify(firestationService, times(1)).addFirestation(any(Firestation.class));
    }

    @Test
    void addFirestation_InvalidInput_ReturnsInternalServerError() {
        
        String address = "123 Main Street";
        String station = "1";
        Firestation firestation = new Firestation(address, station);

        when(firestationService.addFirestation(any(Firestation.class))).thenReturn(null);

        
        ResponseEntity<Firestation> response = firestationController.addFirestation(firestation);

        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(firestationService, times(1)).addFirestation(any(Firestation.class));
    }

    @Test
    void updateFirestation_ExistingFirestation_ReturnsUpdatedFirestation() {
        
        String address = "123 Main Street";
        String station = "1";
        Firestation firestation = new Firestation(address, station);
        when(firestationService.updateFirestation(any(Firestation.class))).thenReturn(firestation);

        
        ResponseEntity<Firestation> response = firestationController.updateFirestation(address, station);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(firestation, response.getBody());
        verify(firestationService, times(1)).updateFirestation(any(Firestation.class));
    }

    @Test
    void updateFirestation_NonExistingFirestation_ReturnsNotFound() {
        
        String address = "123 Main Street";
        String station = "1";
        when(firestationService.updateFirestation(any(Firestation.class))).thenReturn(null);

        
        ResponseEntity<Firestation> response = firestationController.updateFirestation(address, station);

       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(firestationService, times(1)).updateFirestation(any(Firestation.class));
    }

    @Test
    void deleteFirestation_ExistingFirestation_ReturnsOk() {
        
        String address = "1509 Culver St";
        String station = "3";
        when(firestationService.deleteFirestationByStation(station)).thenReturn(true);

        
        ResponseEntity<Void> response = firestationController.deleteFirestation(address, station);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(firestationService, times(0)).deleteFirestationByStation(station);
    }

    @Test
    void deleteFirestation_NonExistingFirestation_ReturnsNotFound() {
        
        String address = "123 Main Street";
        String station = "1";
        when(firestationService.deleteFirestationByAddress(address)).thenReturn(false);

        
        ResponseEntity<Void> response = firestationController.deleteFirestation(address, station);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(firestationService, times(1)).deleteFirestationByAddress(address);
    }
    
    @Test
    void deleteFirestation_NonExistingFirestation_ReturnsNotFoundd() {
        
        String address = null;
        String station = "10";
        when(firestationService.deleteFirestationByStation(station)).thenReturn(false);

        
        ResponseEntity<Void> response = firestationController.deleteFirestation(address, station);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(firestationService, times(1)).deleteFirestationByStation(station);
    }
}
