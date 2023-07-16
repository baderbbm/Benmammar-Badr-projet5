package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class FirestationRepository {

	private List<Firestation> firestations;

	public FirestationRepository(List<Firestation> firestations) {
		this.firestations = firestations;
	}

	public List<Firestation> extractFirestations(Map<String, List<Map<String, Object>>> data) {
		List<Map<String, Object>> firestationDataList = data.get("firestations");
		List<Firestation> firestations = new ArrayList<>();

		for (Map<String, Object> firestationData : firestationDataList) {
			Firestation firestation = new Firestation();
			firestation.setAddress((String) firestationData.get("address"));
			firestation.setStation((String) firestationData.get("station"));
			firestations.add(firestation);
		}
		return firestations;
	}


}
