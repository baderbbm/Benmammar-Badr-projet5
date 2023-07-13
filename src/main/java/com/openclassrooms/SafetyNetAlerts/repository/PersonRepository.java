package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.dto.PersonCaserne;
import com.openclassrooms.SafetyNetAlerts.model.dto.Child;
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverageResponse;
import com.openclassrooms.SafetyNetAlerts.model.dto.Resident;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentInfo;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentStation;

import org.apache.el.stream.Optional;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PersonRepository {

	private List<Person> people;

	public PersonRepository(List<Person> people) {
		this.people = people;
	}

	private static List<Person> extractPeople(Map<String, List<Map<String, Object>>> data) {
		List<Map<String, Object>> personDataList = data.get("persons");
		List<Person> people = new ArrayList<>();

		for (Map<String, Object> personData : personDataList) {
			Person person = new Person();
			person.setFirstName((String) personData.get("firstName"));
			person.setLastName((String) personData.get("lastName"));
			person.setAddress((String) personData.get("address"));
			person.setCity((String) personData.get("city"));
			person.setZip((String) personData.get("zip"));
			person.setPhone((String) personData.get("phone"));
			person.setEmail((String) personData.get("email"));
			people.add(person);
		}
		return people;
	}

	private List<Firestation> extractFirestations(Map<String, List<Map<String, Object>>> data) {
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

	private List<MedicalRecord> extractMedicalRecords(Map<String, List<Map<String, Object>>> data) {
	    List<Map<String, Object>> medicalRecordDataList = data.get("medicalrecords");
	    List<MedicalRecord> medicalRecords = new ArrayList<>();

	    for (Map<String, Object> medicalRecordData : medicalRecordDataList) {
	        MedicalRecord medicalRecord = new MedicalRecord();
	        medicalRecord.setFirstName((String) medicalRecordData.get("firstName"));
	        medicalRecord.setLastName((String) medicalRecordData.get("lastName"));
	        
	        // Conversion de la chaîne de caractères en LocalDate
	        String birthdateString = (String) medicalRecordData.get("birthdate");
	        LocalDate birthdate = LocalDate.parse(birthdateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	        medicalRecord.setBirthdate(birthdate);
	        
	        medicalRecord.setMedications((List<String>) medicalRecordData.get("medications"));
	        medicalRecord.setAllergies((List<String>) medicalRecordData.get("allergies"));
	        medicalRecords.add(medicalRecord);
	    }
	    return medicalRecords;
	}


	// Récupérer une liste d'adresses associées à une caserne de pompiers spécifique

	public List<String> getAddressesByStationNumber(String stationNumber) {

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// Charger le fichier JSON dans la structure data

			Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
					new File("./src/main/resources/data.json"),
					new TypeReference<Map<String, List<Map<String, Object>>>>() {
					});

			List<Firestation> firestations = extractFirestations(data);

			List<String> addresses = new ArrayList<>();

			for (Firestation firestation : firestations) {
				if (firestation.getStation().equals(stationNumber)) {
					addresses.add(firestation.getAddress());
				}
			}
			return addresses;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	// Récupérer des informations sur les personnes associées à une liste d'adresses
	// données

	public FirestationCoverageResponse getPeopleByAddresses(List<String> addresses) {
		try {
			// ObjectMapper fait partie de la bibliothèque Jackson, utilisée pour mapper des
			// objets Java
			// vers des représentations JSON et vice versa

			ObjectMapper objectMapper = new ObjectMapper();

			// Charger le fichier JSON dans la structure data

			Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
					new File("./src/main/resources/data.json"),
					new TypeReference<Map<String, List<Map<String, Object>>>>() {
					});

			List<Person> people = extractPeople(data);

			List<PersonCaserne> peopleByAddresses = new ArrayList<>();
			int adultsCount = 0;
			int childrenCount = 0;

			// medicalRecords pour traiter les dates de naissances

			List<Map<String, Object>> medicalRecords = data.get("medicalrecords");

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
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Return null instead of an empty ArrayList in case of an exception
		}
	}

	// Récupère une liste de personnes en fonction d'une adresse donnée

	public List<Person> getPeopleByAddress(String address) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			// Load the JSON file into a data structure
			Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
					new File("./src/main/resources/data.json"),
					new TypeReference<Map<String, List<Map<String, Object>>>>() {
					});

			List<Person> people = extractPeople(data);

			List<Person> peopleByAddresses = new ArrayList<>();

			// List<Map<String, Object>> medicalRecords = data.get("medicalrecords");

			for (Person person : people) {
				if (address.contains(person.getAddress())) {
					peopleByAddresses.add(person);
				}
			}
			return peopleByAddresses;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Retourner une liste d'enfants résidant à une adresse spécifiée ainsi que la
	// liste des membres de ce foyer

	public List<Child> getChildrenByAddress(String address) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
					new File("./src/main/resources/data.json"),
					new TypeReference<Map<String, List<Map<String, Object>>>>() {
					});

			List<Person> people = getPeopleByAddress(address);

			List<Map<String, Object>> medicalRecords = data.get("medicalrecords");

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

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();

	            // Charger le fichier JSON dans une structure de données
	            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
	                    new File("./src/main/resources/data.json"),
	                    new TypeReference<Map<String, List<Map<String, Object>>>>() {});

	            List<Person> people = extractPeople(data);
	            List<Firestation> firestations = extractFirestations(data);
	            List<MedicalRecord> medicalRecords = extractMedicalRecords(data);

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
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
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
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();

	            // Charger le fichier JSON dans une structure de données
	            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
	                    new File("./src/main/resources/data.json"),
	                    new TypeReference<Map<String, List<Map<String, Object>>>>() {});

	            List<Person> people = extractPeople(data);
	            List<Firestation> firestations = extractFirestations(data);
	            List<MedicalRecord> medicalRecords = extractMedicalRecords(data);

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
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	  
	    public List<ResidentInfo> getPersonInfo(String firstName, String lastName) { 
	    	
	    	// System.out.println(firstName+"      "+lastName);
	        List<ResidentInfo> residents = new ArrayList<>();

	        try {
	            // Charger le fichier JSON dans une structure de données
	            ObjectMapper objectMapper = new ObjectMapper();
	            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
	                    new File("./src/main/resources/data.json"),
	                    new TypeReference<Map<String, List<Map<String, Object>>>>() {
	                    });

	            // Récupérer les résidents en fonction du prénom et du nom
	           // List<Person> people = extractPeople(data);

	            List<Person> people = getPeopleByFirstNameAndLastName(extractPeople(data), firstName, lastName);

	            // Récupérer les informations complémentaires nécessaires (firestations, medicalrecords, etc.)
	            List<Firestation> firestations = extractFirestations(data);
	            List<MedicalRecord> medicalRecords = extractMedicalRecords(data);

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
	        } catch (Exception e) {
	            e.printStackTrace();
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


	    public List<String> getCommunityEmailsByCity(String city) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();

	            
	            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
	                    new File("./src/main/resources/data.json"),
	                    new TypeReference<Map<String, List<Map<String, Object>>>>() {});

	            List<Person> people = extractPeople(data);
	            List<String> emails = new ArrayList<>();

	            for (Person person : people) {
	                if (person.getCity().equals(city)) {
	                    emails.add(person.getEmail());
	                }
	            }

	            return emails;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	
}
