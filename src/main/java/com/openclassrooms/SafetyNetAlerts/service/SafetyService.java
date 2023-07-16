package com.openclassrooms.SafetyNetAlerts.service;
import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.dto.Child;
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverageResponse;
import com.openclassrooms.SafetyNetAlerts.model.dto.PersonCaserne;
import com.openclassrooms.SafetyNetAlerts.model.dto.Resident;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentInfo;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentStation;
import com.openclassrooms.SafetyNetAlerts.repository.FirestationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.repository.SafetyRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SafetyService {
	
    private SafetyRepository safetyRepository; 
    private PersonRepository personRepository; 
    private FirestationRepository firestationRepository; 
    private MedicalRecordRepository medicalRecordRepository;

    public SafetyService(SafetyRepository safetyRepository, PersonRepository personRepository,
		FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository) {
	super();
	this.safetyRepository = safetyRepository;
	this.personRepository = personRepository;
	this.firestationRepository = firestationRepository;
	this.medicalRecordRepository = medicalRecordRepository;
}

    public FirestationCoverageResponse retrievePersonsByFirestation(String stationNumber) { 
    	 if (stationNumber.isEmpty()) {
    	        return new FirestationCoverageResponse(); 
    	    }
        return getPeopleByAddresses(getAddressesByStationNumber(stationNumber)); 
    }



	public List<Child> retrieveChildrenByAddress(String address) {
    	 if (address.isEmpty()) {
 	        return new ArrayList<Child>();
 	    }
        return getChildrenByAddress(address);         
    }
   
    
    public List<String> retrievePhoneNumbersByFirestation(String firestationNumber) {
    	 if (firestationNumber.isEmpty()) {
  	        return new ArrayList<String>(); 
  	    }
    	return getPhoneNumbersByStationNumber(firestationNumber) ;
    }
    
   
    public List<Resident> retrieveResidentsByAddress(String address) {
    	 if (address.isEmpty()) {
  	        return new ArrayList<Resident>(); 
  	    }
    	return getResidentsByAddress(address); 
    }
   
    public List<ResidentStation> retrieveHouseholdsByStations(String stationNumbers) {
    	 if (stationNumbers.isEmpty()) {
   	        return new ArrayList<ResidentStation>(); 
   	    }
    	return getAllResidentsByFirestation(stationNumbers);
        }
       
    
    public List<ResidentInfo> retrievePersonInfoByName(String firstName, String lastName) {
    	 if (firstName.isEmpty() && lastName.isEmpty()) {
    	        return new ArrayList<ResidentInfo>(); 
    	    }
    	 
    	 if (firstName.isEmpty() || lastName.isEmpty()) {
 	        return getPersonInfo(lastName);
 	    }
    	 
        return getPersonInfo(firstName, lastName);
    }
    
    public List<String> retrieveCommunityEmailsByCity(String city) {
    	 if (city.isEmpty()) {
    	        return new ArrayList<String>(); 
    	    }
        return getCommunityEmailsByCity(city);
    }

	// Récupérer une liste d'adresses associées à une caserne de pompiers spécifique

	public List<String> getAddressesByStationNumber(String stationNumber) {
			List<Firestation> firestations = firestationRepository.extractFirestations(safetyRepository.loadData());
			List<String> addresses = new ArrayList<>();
			for (Firestation firestation : firestations) {
				if (firestation.getStation().equals(stationNumber)) {
					addresses.add(firestation.getAddress());
				}
			}
			return addresses;
	}

	// Récupérer des informations sur les personnes associées à une liste d'adresses
	// données

	public FirestationCoverageResponse getPeopleByAddresses(List<String> addresses) {
			List<Person> people = personRepository.extractPeople(safetyRepository.loadData()); 

			List<PersonCaserne> peopleByAddresses = new ArrayList<>();
			int adultsCount = 0;
			int childrenCount = 0;

			// medicalRecords pour traiter les dates de naissances

			List<Map<String, Object>> medicalRecords = safetyRepository.loadData().get("medicalrecords");

			for (Person person : people) {
				if (addresses.contains(person.getAddress())) {
					PersonCaserne personCaserne = new PersonCaserne();

					// Optional est utilisé pour représenter une valeur qui peut être présente ou
					// absente
					// Le flux est ensuite filtré en utilisant la méthode filter() avec une
					// expression lambda
					// findFirst() est ensuite appelée sur le flux filtré pour récupérer le premier
					// élément correspondant

					java.util.Optional<Map<String, Object>> medicalRecordOptional = medicalRecords.stream()
							.filter(record -> record.get("firstName").equals(person.getFirstName())
									&& record.get("lastName").equals(person.getLastName()))
							.findFirst();

					if (medicalRecordOptional.isPresent()) {
						Map<String, Object> medicalRecord = medicalRecordOptional.get();
						
						//PersonCaserne personCaserne = new PersonCaserne(); 
						personCaserne.setFirstName(person.getFirstName());
						personCaserne.setLastName(person.getLastName());
						personCaserne.setAddress(person.getAddress());
						personCaserne.setPhone(person.getPhone());

						// Set birthdate
						String birthdateString = (String) medicalRecord.get("birthdate");
						LocalDate birthDate = LocalDate.parse(birthdateString,
						DateTimeFormatter.ofPattern("MM/dd/yyyy"));
						LocalDate currentDate = LocalDate.now();
						Period period = Period.between(birthDate, currentDate);

						if (period.getYears() <= 18) {
							childrenCount++;
						} else {
							adultsCount++;
						}
					}

					peopleByAddresses.add(personCaserne);

				}
			}

			FirestationCoverageResponse response = new FirestationCoverageResponse();
			response.setPeople(peopleByAddresses);
			response.setAdultsCount(adultsCount);
			response.setChildrenCount(childrenCount);

			return response;
	}

	// Récupère une liste de personnes en fonction d'une adresse donnée

	public List<Person> getPeopleByAddress(String address) {

			List<Person> people = personRepository.extractPeople(safetyRepository.loadData()); 

			List<Person> peopleByAddresses = new ArrayList<>();

			// List<Map<String, Object>> medicalRecords = data.get("medicalrecords");

			for (Person person : people) {
				if (address.contains(person.getAddress())) {
					peopleByAddresses.add(person);
				}
			}
			return peopleByAddresses;
	}

	// Retourner une liste d'enfants résidant à une adresse spécifiée ainsi que la
	// liste des membres de ce foyer

	public List<Child> getChildrenByAddress(String address) {

			List<Person> people = getPeopleByAddress(address);

			List<Map<String, Object>> medicalRecords = safetyRepository.loadData().get("medicalrecords");

			List<Child> childrenByAddress = new ArrayList<>();

			for (Person person : people) {

				java.util.Optional<Map<String, Object>> medicalRecordOptional = medicalRecords.stream()
						.filter(record -> record.get("firstName").equals(person.getFirstName())
								&& record.get("lastName").equals(person.getLastName()))
						.findFirst();
				if (medicalRecordOptional.isPresent()) {
					Map<String, Object> medicalRecord = medicalRecordOptional.get();
					// Set birthdate
					String birthdateString = (String) medicalRecord.get("birthdate");
					LocalDate birthDate = LocalDate.parse(birthdateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
					LocalDate currentDate = LocalDate.now();
					Period period = Period.between(birthDate, currentDate);

					if (period.getYears() <= 18) {
						List<String> householdMembers = getHouseholdMembers(person, address);
						Child child = new Child(person.getFirstName(), person.getLastName(), period.getYears(),
								householdMembers);
						childrenByAddress.add(child);
					}
				}
			}
			return childrenByAddress;
	}

	// Retourne une liste des membres du foyer d'une personne

	public List<String> getHouseholdMembers(Person person, String address) {
		List<Person> householdMembers = getPeopleByAddress(address);
		List<String> householdMemberNames = new ArrayList<>();

		for (Person member : householdMembers) {
			if (!(member.getFirstName()).equals(person.getFirstName()) && address.contains(member.getAddress())) {
				householdMemberNames.add(member.getFirstName() + " " + member.getLastName());
			}
		}

		return householdMemberNames;
	}

	public List<String> getPhoneNumbersByStationNumber(String stationNumber) {
		List<String> phoneNumbers = new ArrayList<>();

		// Obtenir la liste des adresses desservies par la caserne de pompiers
		List<String> addresses = getAddressesByStationNumber(stationNumber);

		for (String address : addresses) {
			// Obtenir les personnes résidant à l'adresse spécifiée
			List<Person> residents = getPeopleByAddress(address);

			for (Person resident : residents) {
				// Ajouter le numéro de téléphone du résident à la liste
				phoneNumbers.add(resident.getPhone());
			}
		}

		return phoneNumbers;
	}
	
	
	// Retourne une liste de résidents associés à une adresse spécifiée. 
	
	 public List<Resident> getResidentsByAddress(String address) {

	            List<Person> people = personRepository.extractPeople(safetyRepository.loadData());
	            List<Firestation> firestations = firestationRepository.extractFirestations(safetyRepository.loadData());
	            List<MedicalRecord> medicalRecords = medicalRecordRepository.extractMedicalRecords(safetyRepository.loadData());

	            List<Resident> residents = new ArrayList<>();

	            for (Person person : people) {
	                if (address.equals(person.getAddress())) {
	                    String personFirstName = person.getFirstName();
	                    String personLastName = person.getLastName();
	                    String personPhone = person.getPhone();
	                   // String personEmail =person.getEmail();
	                    int personAge = getAgeFromMedicalRecords(person, medicalRecords);

	                    // Trouver la caserne de pompiers desservant cette adresse
	                    String firestationNumber = null;
	                    for (Firestation firestation : firestations) {
	                        if (firestation.getAddress().equals(address)) {
	                            firestationNumber = firestation.getStation();
	                            break;
	                        }
	                    }

	                    // Créer une nouvelle instance de Resident avec les informations nécessaires
	                    Resident resident = new Resident();
	                    resident.setFirstName(personFirstName);
	                    resident.setLastName(personLastName);
	                    resident.setPhone(personPhone);
	                    resident.setAge(personAge);
	                   // resident.setEmail(personEmail);
	                    resident.setStation(firestationNumber);

	                    // Ajouter les antécédents médicaux à partir du dossier médical correspondant
	                    for (MedicalRecord medicalRecord : medicalRecords) {
	                        if (medicalRecord.getFirstName().equals(personFirstName)
	                                && medicalRecord.getLastName().equals(personLastName)) {
	                            resident.setMedications(medicalRecord.getMedications());
	                            resident.setAllergies(medicalRecord.getAllergies());
	                            break;
	                        }
	                    }

	                    residents.add(resident);
	                }
	            }

	            return residents;
	    }

	    private int getAgeFromMedicalRecords(Person person, List<MedicalRecord> medicalRecords) {
	        for (MedicalRecord medicalRecord : medicalRecords) {
	            if (medicalRecord.getFirstName().equals(person.getFirstName())
	                    && medicalRecord.getLastName().equals(person.getLastName())) {
	                return medicalRecord.getAge();
	            }
	        }
	        return -1; // L'âge n'a pas pu être trouvé
	    }
	    
	    public List<ResidentStation> getAllResidentsByFirestation(String stationNumber) {

	            List<Person> people = personRepository.extractPeople(safetyRepository.loadData());
	            List<Firestation> firestations = firestationRepository.extractFirestations(safetyRepository.loadData());
	            List<MedicalRecord> medicalRecords = medicalRecordRepository.extractMedicalRecords(safetyRepository.loadData());

	            List<ResidentStation> residents = new ArrayList<>();

	            // Obtenir la liste des adresses desservies par la caserne de pompiers spécifiée
	            List<String> addresses = getAddressesByStationNumber(stationNumber);

	            for (String address : addresses) {
	                List<Person> residentsByAddress = getPeopleByAddress(address);

	                for (Person person : residentsByAddress) {
	                    String personFirstName = person.getFirstName();
	                    String personLastName = person.getLastName();
	                    String personPhone = person.getPhone();
	                    String personEmail = person.getEmail();
	                    int personAge = getAgeFromMedicalRecords(person, medicalRecords);

	                    // Créer une nouvelle instance de Resident avec les informations nécessaires
	                    ResidentStation resident = new ResidentStation();
	                    resident.setFirstName(personFirstName);
	                    resident.setLastName(personLastName);
	                    resident.setPhone(personPhone);
	                    resident.setAge(personAge);
	                   // resident.setAdress(address);
	                   // resident.setStation(stationNumber);
	                   // resident.setEmail(personEmail);

	                    // Ajouter les antécédents médicaux à partir du dossier médical correspondant
	                    for (MedicalRecord medicalRecord : medicalRecords) {
	                        if (medicalRecord.getFirstName().equals(personFirstName)
	                                && medicalRecord.getLastName().equals(personLastName)) {
	                            resident.setMedications(medicalRecord.getMedications());
	                            resident.setAllergies(medicalRecord.getAllergies());
	                            break;
	                        }
	                    }

	                    residents.add(resident);
	                }
	            }

	            return residents;
	    }
	    
	  
	    public List<ResidentInfo> getPersonInfo(String firstName, String lastName) { 
	    	
	    	// System.out.println(firstName+"      "+lastName);
	        List<ResidentInfo> residents = new ArrayList<>();

	            // Récupérer les résidents en fonction du prénom et du nom
	           // List<Person> people = extractPeople(data);

	            List<Person> people = getPeopleByFirstNameAndLastName(personRepository.extractPeople(safetyRepository.loadData()), firstName, lastName);

	            // Récupérer les informations complémentaires nécessaires (firestations, medicalrecords, etc.)
	            List<Firestation> firestations = firestationRepository.extractFirestations(safetyRepository.loadData());
	            List<MedicalRecord> medicalRecords = medicalRecordRepository.extractMedicalRecords(safetyRepository.loadData());

	            // Parcourir les résidents et récupérer leurs informations
	            for (Person person : people) {
	                ResidentInfo resident = new ResidentInfo();
	                resident.setFirstName(person.getFirstName());
	                resident.setLastName(person.getLastName());
	              resident.setAdress(person.getAddress());
	               resident.setEmail(person.getEmail());
	               // resident.setPhone(person.getPhone());
                    int personAge = getAgeFromMedicalRecords(person, medicalRecords);
                    resident.setAge(personAge);

	                // Trouver la caserne de pompiers desservant cette adresse
	                String firestationNumber = null;
	                for (Firestation firestation : firestations) {
	                    if (firestation.getAddress().equals(person.getAddress())) {
	                        firestationNumber = firestation.getStation();
	                        break;
	                    }
	                }
	               // resident.setStation(firestationNumber);

	                // Récupérer les antécédents médicaux du résident
	                for (MedicalRecord medicalRecord : medicalRecords) {
	                    if (medicalRecord.getFirstName().equals(person.getFirstName())
	                            && medicalRecord.getLastName().equals(person.getLastName())) {
	                        resident.setMedications(medicalRecord.getMedications());
	                        resident.setAllergies(medicalRecord.getAllergies());
	                        break;
	                    }
	                }

	                residents.add(resident);
	            }
	        return residents;
	    }
	    
	    public List<Person> getPeopleByFirstNameAndLastName(List<Person> people, String firstName, String lastName) {
	        List<Person> matchingPeople = new ArrayList<>();

	        for (Person person : people) {
	            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
	                matchingPeople.add(person);
	            }
	        }

	        return matchingPeople;
	    }

	    
	    public List<ResidentInfo> getPersonInfo(String lastName) { 
	    	
	    	// System.out.println(firstName+"      "+lastName);
	        List<ResidentInfo> residents = new ArrayList<>();

	            // Récupérer les résidents en fonction du prénom et du nom
	           // List<Person> people = extractPeople(data);

	            List<Person> people = getPeopleByLastName(personRepository.extractPeople(safetyRepository.loadData()), lastName);

	            // Récupérer les informations complémentaires nécessaires (firestations, medicalrecords, etc.)
	            List<Firestation> firestations = firestationRepository.extractFirestations(safetyRepository.loadData());
	            List<MedicalRecord> medicalRecords = medicalRecordRepository.extractMedicalRecords(safetyRepository.loadData());

	            // Parcourir les résidents et récupérer leurs informations
	            for (Person person : people) {
	                ResidentInfo resident = new ResidentInfo();
	                resident.setFirstName(person.getFirstName());
	                resident.setLastName(person.getLastName());
	              resident.setAdress(person.getAddress());
	               resident.setEmail(person.getEmail());
	               // resident.setPhone(person.getPhone());
                    int personAge = getAgeFromMedicalRecords(person, medicalRecords);
                    resident.setAge(personAge);

	                // Trouver la caserne de pompiers desservant cette adresse
	                String firestationNumber = null;
	                for (Firestation firestation : firestations) {
	                    if (firestation.getAddress().equals(person.getAddress())) {
	                        firestationNumber = firestation.getStation();
	                        break;
	                    }
	                }
	               // resident.setStation(firestationNumber);

	                // Récupérer les antécédents médicaux du résident
	                for (MedicalRecord medicalRecord : medicalRecords) {
	                    if (medicalRecord.getFirstName().equals(person.getFirstName())
	                            && medicalRecord.getLastName().equals(person.getLastName())) {
	                        resident.setMedications(medicalRecord.getMedications());
	                        resident.setAllergies(medicalRecord.getAllergies());
	                        break;
	                    }
	                }

	                residents.add(resident);
	            }
	        return residents;
	    }
	    public List<Person> getPeopleByLastName(List<Person> people, String lastName) {
	        List<Person> matchingPeople = new ArrayList<>();

	        for (Person person : people) {
	            if (person.getLastName().equals(lastName)) {
	                matchingPeople.add(person);
	            }
	        }

	        return matchingPeople;
	    }

	    public List<String> getCommunityEmailsByCity(String city) {
	            List<Person> people = personRepository.extractPeople(safetyRepository.loadData());
	            List<String> emails = new ArrayList<>();
	            for (Person person : people) {
	                if (person.getCity().equals(city)) {
	                    emails.add(person.getEmail());
	                }
	            }
	            return emails;
	    }
}
