package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.Firestation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.dto.Address;
import com.openclassrooms.SafetyNetAlerts.model.dto.Child;
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverage;
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
import java.util.Optional;

@Service
public class SafetyService {

	private SafetyRepository safetyRepository;
	private PersonRepository personRepository;
	private FirestationRepository firestationRepository;
	private MedicalRecordRepository medicalRecordRepository;

	public SafetyService(SafetyRepository safetyRepository, PersonRepository personRepository,
			FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository) {
		this.safetyRepository = safetyRepository;
		this.personRepository = personRepository;
		this.firestationRepository = firestationRepository;
		this.medicalRecordRepository = medicalRecordRepository;
	}

	// retourner une liste des personnes couvertes par la caserne de pompiers correspondante
	
	public FirestationCoverage retrievePersonsByFirestation(String stationNumber) {
		if (stationNumber.isEmpty()) {
			return new FirestationCoverage();
		}
		return getPeopleByAddresses(getAddressesByStationNumber(stationNumber));
	}
	
	// retourner une liste d'enfants habitant à cette adresse

	public List<Child> retrieveChildrenByAddress(String address) {
		if (address.isEmpty()) {
			return new ArrayList<Child>();
		}
		return getChildrenByAddress(address);
	}

	// retourner une liste des numéros de téléphone des résidents desservis par la
	// caserne de pompiers

	public List<String> retrievePhoneNumbersByFirestation(String firestationNumber) {
		if (firestationNumber.isEmpty()) {
			return new ArrayList<String>();
		}
		return getPhoneNumbersByStationNumber(firestationNumber);
	}

	// retourner la liste des habitants vivant à l’adresse donnée ainsi que le
	// numéro de la caserne de pompiers la desservant

	public List<Resident> retrieveResidentsByAddress(String address) {
		if (address.isEmpty()) {
			return new ArrayList<Resident>();
		}
		return getResidentsByAddress(address);
	}


	// retourner le nom, l'adresse, l'âge, l'adresse mail
	// et les antécédents médicaux (médicaments, posologie, allergies) de chaque
	// habitant

	public List<ResidentInfo> retrievePersonInfoByName(String firstName, String lastName) {
		if ((firstName == null || firstName.isEmpty()) && lastName.isEmpty()) {
			return new ArrayList<ResidentInfo>();
		}
		if (firstName == null || firstName.isEmpty()) {
			return getPersonInfo(lastName);
		}

		return getPersonInfo(firstName, lastName);
	}

	// retourner les adresses mail de tous les habitants de la ville

	public List<String> retrieveCommunityEmailsByCity(String city) {
		if (city.isEmpty()) {
			return new ArrayList<String>();
		}
		return getCommunityEmailsByCity(city);
	}

	// Récupérer une liste d'adresses associées à une caserne de pompiers spécifique

	public List<String> getAddressesByStationNumber(String stationNumber) {
		List<Firestation> firestations = firestationRepository.getFirestations();

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

	public FirestationCoverage getPeopleByAddresses(List<String> addresses) {

		List<Person> people = personRepository.getPeople();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecord();

		List<PersonCaserne> peopleByAddresses = new ArrayList<>();
		int adultsCount = 0;
		int childrenCount = 0;

		for (String address : addresses) {
			for (Person person : people) {
				if (address.equals(person.getAddress())) {
					PersonCaserne personCaserne = new PersonCaserne();

					personCaserne.setFirstName(person.getFirstName());
					personCaserne.setLastName(person.getLastName());
					personCaserne.setAddress(person.getAddress());
					personCaserne.setPhone(person.getPhone());

					Optional<MedicalRecord> medicalRecordOptional = medicalRecords.stream()
							.filter(record -> record.getFirstName().equals(person.getFirstName())
									&& record.getLastName().equals(person.getLastName()))
							.findFirst();

					if (medicalRecordOptional.isPresent()) {
						MedicalRecord medicalRecord = medicalRecordOptional.get();

						LocalDate birthDate = medicalRecord.getBirthdate();
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
		}

		FirestationCoverage response = new FirestationCoverage();
		response.setPeople(peopleByAddresses);
		response.setAdultsCount(adultsCount);
		response.setChildrenCount(childrenCount);

		return response;
	}

	// Récupère une liste de personnes en fonction d'une adresse donnée

	public List<Person> getPeopleByAddress(String address) {

		List<Person> people = personRepository.getPeople();

		List<Person> peopleByAddresses = new ArrayList<>();

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
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecord();

		List<Child> childrenByAddress = new ArrayList<>();

		for (Person person : people) {
			Optional<MedicalRecord> medicalRecordOptional = medicalRecords.stream()
					.filter(record -> record.getFirstName().equals(person.getFirstName())
							&& record.getLastName().equals(person.getLastName()))
					.findFirst();

			if (medicalRecordOptional.isPresent()) {
				MedicalRecord medicalRecord = medicalRecordOptional.get();
				LocalDate birthDate = medicalRecord.getBirthdate();
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

		List<Person> people = personRepository.getPeople();
		List<Firestation> firestations = firestationRepository.getFirestations();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecord();

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
				LocalDate birthDate = medicalRecord.getBirthdate();
				
				LocalDate currentDate = LocalDate.now();
				Period period = Period.between(birthDate, currentDate);
				return period.getYears();
			}
		}
		return -1; 
	}

	// récupérer des informations détaillées sur tous les résidents résidant
	// à des adresses desservies par une caserne de pompiers spécifiée

	public List<Address> getAllResidentsByFirestation(String stationNumber) {
	 
	    List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecord();

	    List<Address> residentss = new ArrayList<>(); 
	    List<String> addresses = getAddressesByStationNumber(stationNumber);

	    for (String address : addresses) {

	        List<Person> residentsByAddress = getPeopleByAddress(address);
	        List<ResidentStation> residents = new ArrayList<>(); 

	        for (Person person : residentsByAddress) {
	            String personFirstName = person.getFirstName();
	            String personLastName = person.getLastName();
	            String personPhone = person.getPhone();
	            int personAge = getAgeFromMedicalRecords(person, medicalRecords);

	            ResidentStation resident = new ResidentStation();
	            resident.setFirstName(personFirstName);
	            resident.setLastName(personLastName);
	            resident.setPhone(personPhone);
	            resident.setAge(personAge);

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

	        Address residentp = new Address(); 
	        residentp.setAddress(address);
	        residentp.setResidentStation(residents);
	        residentss.add(residentp);
	    }

	    return residentss;
	}

	
	// récupérer des informations détaillées sur tous les résidents résidant
	// à des adresses desservies par des casernes de pompiers spécifiées
	
	public List<Address> getAllResidentsByFirestations(List<String> stationNumbers) {
        List<Address> allResidents = new ArrayList<>();

        for (String stationNumber : stationNumbers) {
            List<Address> residents = getAllResidentsByFirestation(stationNumber);
            allResidents.addAll(residents);
        }

        return allResidents;
    }
	

	// prend un prénom et un nom de famille en entrée, récupère les informations des
	// résidents correspondants
	// à partir de différentes sources de données, les combine dans des objets
	// ResidentInfo, puis renvoie
	// une liste contenant ces informations pour tous les résidents correspondants

	public List<ResidentInfo> getPersonInfo(String firstName, String lastName) {
		List<ResidentInfo> residents = new ArrayList<>();

		// Récupérer les résidents en fonction du prénom et du nom

		List<Person> people = getPeopleByFirstNameAndLastName(personRepository.getPeople(), firstName, lastName);

		// Récupérer les informations complémentaires nécessaires (firestations,
		// medicalrecords, etc.)
		List<Firestation> firestations = firestationRepository.getFirestations();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecord();

		// Parcourir les résidents et récupérer leurs informations
		for (Person person : people) {
			ResidentInfo resident = new ResidentInfo();
			resident.setFirstName(person.getFirstName());
			resident.setLastName(person.getLastName());
			resident.setAdress(person.getAddress());
			resident.setEmail(person.getEmail());
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

	// prend une liste de personnes, un prénom et un nom de famille en entrée,
	// filtre la liste pour ne conserver que
	// les personnes ayant le prénom et le nom de famille spécifiés, puis retourne
	// une nouvelle liste contenant
	// ces personnes filtrées

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

		List<ResidentInfo> residents = new ArrayList<>();

		// Récupérer les résidents en fonction du prénom et du nom

		List<Person> people = getPeopleByLastName(personRepository.getPeople(), lastName);

		// Récupérer les informations complémentaires nécessaires (firestations,
		// medicalrecords, etc.)
		List<Firestation> firestations = firestationRepository.getFirestations();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecord();

		// Parcourir les résidents et récupérer leurs informations
		for (Person person : people) {
			ResidentInfo resident = new ResidentInfo();
			resident.setFirstName(person.getFirstName());
			resident.setLastName(person.getLastName());
			resident.setAdress(person.getAddress());
			resident.setEmail(person.getEmail());
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

	// prend une liste de personnes et un nom de famille en entrée, filtre la liste
	// pour ne conserver
	// que les personnes ayant le nom de famille spécifié, puis retourne une
	// nouvelle liste contenant ces personnes filtrées

	public List<Person> getPeopleByLastName(List<Person> people, String lastName) {
		List<Person> matchingPeople = new ArrayList<>();
		for (Person person : people) {
			if (person.getLastName().equals(lastName)) {
				matchingPeople.add(person);
			}
		}

		return matchingPeople;
	}

	// récupère une liste de personnes, filtre celles qui résident
	// dans la ville spécifiée et renvoie une liste d'adresses e-mail
	// correspondantes

	public List<String> getCommunityEmailsByCity(String city) {
		List<Person> people = personRepository.getPeople();
		List<String> emails = new ArrayList<>();
		for (Person person : people) {
			if (person.getCity().equals(city)) {
				emails.add(person.getEmail());
			}
		}
		return emails;
	}
}
