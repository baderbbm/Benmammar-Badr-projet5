package com.openclassrooms.SafetyNetAlerts.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.repository.FirestationRepository;

@Service
public class FirestationService {
	private FirestationRepository firestationRepository;

 public FirestationService(FirestationRepository firestationRepository) {
		this.firestationRepository = firestationRepository;
	}

	public List<Firestation> getFirestations() {
		return firestationRepository.getFirestations();
	}
	
	public Firestation addFirestation(Firestation firestation) {
		getFirestations().add(firestation);
		return firestation;
	}

	public Firestation updateFirestation(Firestation firestation) {
		for (Firestation existingFirestation : getFirestations()) {
			if (existingFirestation.getAddress().equals(firestation.getAddress())) {
				existingFirestation.setStation(firestation.getStation());
				return existingFirestation;
			}
		}
		return null;
	}

	public boolean deleteFirestationByAddress(String address) {
		Firestation foundFirestation = null;
		for (Firestation firestation : getFirestations()) {
			if (firestation.getAddress().equals(address)) {
				foundFirestation = firestation;
				break;
			}
		}
		if (foundFirestation != null) {
			getFirestations().remove(foundFirestation);
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteFirestationByStation(String station) {
		boolean foundFirestation = false;
		Iterator<Firestation> iterator = getFirestations().iterator();

		while (iterator.hasNext()) {
			Firestation firestation = iterator.next();
			if (firestation.getStation().equals(station)) {
				iterator.remove();
				foundFirestation = true;
			}
		}

		return foundFirestation;
	}

}
