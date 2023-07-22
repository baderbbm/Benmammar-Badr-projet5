package com.openclassrooms.SafetyNetAlerts.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.repository.FirestationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.SafetyRepository;

@Service
public class FirestationService {
	private List<Firestation> firestations;

	public FirestationService(FirestationRepository firestationRepository, SafetyRepository safetyRepository) {
		this.firestations = firestationRepository.getFirestations();

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

	public boolean deleteFirestationByAddress(String address) {
		Firestation foundFirestation = null;
		for (Firestation firestation : firestations) {
			if (firestation.getAddress().equals(address)) {
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

	public boolean deleteFirestationByStation(String station) {
		boolean foundFirestation = false;
		Iterator<Firestation> iterator = firestations.iterator();

		while (iterator.hasNext()) {
			Firestation firestation = iterator.next();
			if (firestation.getStation().equals(station)) {
				iterator.remove();
				foundFirestation = true;
			}
		}

		return foundFirestation;
	}

	public void setFirestations(List<Firestation> firestations) {
		this.firestations = firestations;
	}
}
