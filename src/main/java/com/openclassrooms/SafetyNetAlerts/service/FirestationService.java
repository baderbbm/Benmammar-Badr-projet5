package com.openclassrooms.SafetyNetAlerts.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.repository.FirestationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.SafetyRepository;

@Service
public class FirestationService {
    private List<Firestation> firestations;


    public FirestationService(FirestationRepository firestationRepository, SafetyRepository safetyRepository) {
        this.firestations = firestationRepository.extractFirestations(safetyRepository.loadData());
    }
    
    public List<Firestation> getFirestations() {
        return firestations;
    }
    
    public Firestation addFirestation(Firestation firestation) {
        firestations.add(firestation);
        return firestation;
    }

    public Firestation updateFirestation(Firestation firestation) {
        for (Firestation existingFirestation : firestations) {
            if (existingFirestation.getAddress().equals(firestation.getAddress())) {
                existingFirestation.setStation(firestation.getStation());
                return existingFirestation;
            }
        }
        return null; 
    }

    public boolean deleteFirestation(String address, String station) {
        Firestation foundFirestation = null;
        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equals(address) && firestation.getStation().equals(station)) {
                foundFirestation = firestation;
                break;
            }
        }
        if (foundFirestation != null) {
            firestations.remove(foundFirestation);
            return true;
        } else {
            return false; 
        }
    }

	public void setFirestations(List<Firestation> firestations) {
		this.firestations=firestations;
	}
    
    
    
}
