package com.openclassrooms.SafetyNetAlerts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.dto.Child;
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverageResponse;
import com.openclassrooms.SafetyNetAlerts.model.dto.PersonCaserne;
import com.openclassrooms.SafetyNetAlerts.model.dto.Resident;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentInfo;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentStation;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PersonRepositoryTest {

	private PersonRepository personRepository;

	@BeforeEach
	public void setUp() {
		personRepository = new PersonRepository(Arrays.asList(new Person()));
	}

	@Test
	public void testgetAddressesByStationNumber() {
		// Données de test
		String stationNumber = "4";

		// Résultat attendu
		List<String> expected = new ArrayList<>();
		expected.add("489 Manchester St");
		expected.add("112 Steppes Pl");

		// Exécution de la méthode à tester
		List<String> actual = personRepository.getAddressesByStationNumber(stationNumber);

		// Vérification du résultat
		assertEquals(expected.get(0), actual.get(0));
		assertEquals(expected.get(1), actual.get(1));
	}

	@Test
	public void testGetPeopleByAddresses() {
		// Données de test
		List<String> addresses = Arrays.asList("489 Manchester St");

		// Résultat attendu
		FirestationCoverageResponse expected = new FirestationCoverageResponse();

		expected.setPeople(Arrays.asList(new PersonCaserne("Lily", "Cooper", "489 Manchester St", "841-874-9845")));

		expected.setAdultsCount(1);
		expected.setChildrenCount(0);

		// Exécution de la méthode à tester
		FirestationCoverageResponse actual = personRepository.getPeopleByAddresses(addresses);

		// Vérification du résultat
		assertEquals(expected.getChildrenCount(), actual.getChildrenCount());
		assertEquals(expected.getPeople().get(0).getFirstName(), actual.getPeople().get(0).getFirstName());
		assertEquals(expected.getPeople().get(0).getLastName(), actual.getPeople().get(0).getLastName());
		assertEquals(expected.getPeople().get(0).getAddress(), actual.getPeople().get(0).getAddress());
		assertEquals(expected.getPeople().get(0).getPhone(), actual.getPeople().get(0).getPhone());
	}

	@Test
	public void testGetPeopleByAddress_NonExistentAddress() {
		String address = "1509";
		List<Person> expected = Arrays.asList();
		List<Person> actual = personRepository.getPeopleByAddress(address);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetChildrenByAddress() {
		// Données de test
		String address = "947 E. Rose Dr";

		// Résultat attendu
		List<Child> expected = Arrays
				.asList(new Child("Kendrik", "Stelzer", 9, Arrays.asList("Brian Stelzer", "Shawna Stelzer")));

		// Exécution de la méthode à tester
		List<Child> actual = personRepository.getChildrenByAddress(address);

		// Vérification du résultat
		assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
		assertEquals(expected.get(0).getLastName(), actual.get(0).getLastName());
		assertEquals(expected.get(0).getAge(), actual.get(0).getAge());
		assertEquals(expected.get(0).getHouseholdMembers(), actual.get(0).getHouseholdMembers());
	}

	@Test
	public void testGetResidentsByAddress() {
		// Données de test
		String address = "947 E. Rose Dr";

		// Résultat attendu
		List<String> medications1 = new ArrayList<>(2);
		medications1.add("ibupurin:200mg");
		medications1.add("hydrapermazol:400mg");
		List<String> allergies1 = new ArrayList<>();
		allergies1.add("nillacilan");

		List<String> medications2 = new ArrayList<>();
		medications2.add("");
		List<String> allergies2 = new ArrayList<>();
		allergies2.add("");

		List<String> medications3 = new ArrayList<>();
		medications3.add("noxidian:100mg");
		medications3.add("pharmacol:2500mg");
		List<String> allergies3 = new ArrayList<>();
		allergies3.add("");

		List<Resident> expected = Arrays.asList(
				new Resident("Brian", "Stelzer", "841-874-7784", "1", 47, medications1, allergies1),
				new Resident("Shawna", "Stelzer", "841-874-7784", "1", 43, medications2, allergies2),
				new Resident("Kendrik", "Stelzer", "841-874-7784", "1", 9, medications3, allergies3));

		// Exécution de la méthode à tester
		List<Resident> actual = personRepository.getResidentsByAddress(address);

		// Vérification du résultat
		assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
		assertEquals(expected.get(1).getFirstName(), actual.get(1).getFirstName());
		assertEquals(expected.get(2).getFirstName(), actual.get(2).getFirstName());
		assertEquals(expected.get(0).getAge(), actual.get(0).getAge());
		assertEquals(expected.get(1).getAge(), actual.get(1).getAge());
		assertEquals(expected.get(2).getAge(), actual.get(2).getAge());
		assertEquals(expected.get(0).getMedications().get(0), actual.get(0).getMedications().get(0));
		assertEquals(expected.get(0).getMedications().get(1), actual.get(0).getMedications().get(1));
		assertEquals(expected.get(0).getAllergies().get(0), actual.get(0).getAllergies().get(0));
	}

	@Test
	public void testGetPhoneNumbersByStationNumber() {
		// Données de test
		String stationNumber = "1";

		// Résultat attendu
		List<String> expected = Arrays.asList("841-874-6512", "841-874-8547", "841-874-7462", "841-874-7784",
				"841-874-7784", "841-874-7784");

		// Exécution de la méthode à tester
		List<String> actual = personRepository.getPhoneNumbersByStationNumber(stationNumber);

		// Vérification du résultat
		assertEquals(expected, actual);
	}

	@Test
	public void testGetAllResidentsByFirestation() {
		// Données de test
		String stationNumber = "4";

		// Résultat attendu
		List<ResidentStation> expected = Arrays.asList(new ResidentStation("Lily", "Cooper", "841-874-9845"),
				new ResidentStation("Tony", "Cooper", "841-874-6874"),
				new ResidentStation("Ron", "Peters", "841-874-8888"),
				new ResidentStation("Allison", "Boyd", "841-874-9888"));

		// Exécution de la méthode à tester
		List<ResidentStation> actual = personRepository.getAllResidentsByFirestation(stationNumber);

		// Vérification du résultat
		assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
		assertEquals(expected.get(1).getFirstName(), actual.get(1).getFirstName());
		assertEquals(expected.get(2).getFirstName(), actual.get(2).getFirstName());
		assertEquals(expected.get(3).getFirstName(), actual.get(3).getFirstName());
	}

	@Test
	public void testGetPersonInfo() {

		// Données de test
		String firstName = "John";
		String lastName = "Boyd";

		List<String> medications1 = new ArrayList<>();
		medications1.add("aznol:350mg");
		medications1.add("hydrapermazol:100mg");
		List<String> allergies1 = new ArrayList<>();
		allergies1.add("nillacilan");

		// Résultat attendu
		
		List<ResidentInfo> expected = Arrays
		.asList(new ResidentInfo("John", "Boyd", "1509 Culver St", "jaboyd@email.com", 39, medications1, allergies1));
				
		// Exécution de la méthode à tester
		List<ResidentInfo> actual = personRepository.getPersonInfo(firstName, lastName);

		// Vérification du résultat
		assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
		assertEquals(expected.get(0).getLastName(), actual.get(0).getLastName());
		assertEquals(expected.get(0).getEmail(), actual.get(0).getEmail());
		assertEquals(expected.get(0).getAge(), actual.get(0).getAge());
		assertEquals(expected.get(0).getMedications().get(0), actual.get(0).getMedications().get(0));
		assertEquals(expected.get(0).getMedications().get(1), actual.get(0).getMedications().get(1));
		assertEquals(expected.get(0).getAllergies().get(0), actual.get(0).getAllergies().get(0));
	}

	@Test
	public void testGetCommunityEmailsByCity() {
		String city = "Culver";
		List<String> expected = Arrays.asList("jaboyd@email.com", "drk@email.com", "tenz@email.com", "jaboyd@email.com",
				"jaboyd@email.com", "drk@email.com", "tenz@email.com", "jaboyd@email.com", "jaboyd@email.com",
				"tcoop@ymail.com", "lily@email.com", "soph@email.com", "ward@email.com", "zarc@email.com",
				"reg@email.com", "jpeter@email.com", "jpeter@email.com", "aly@imail.com", "bstel@email.com",
				"ssanw@email.com", "bstel@email.com", "clivfd@ymail.com", "gramps@email.com");

		List<String> actual = personRepository.getCommunityEmailsByCity(city);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCommunityEmailsByCity_NonExistentCity() {
		String city = "Paris";
		List<String> expected = Arrays.asList();
		List<String> actual = personRepository.getCommunityEmailsByCity(city);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetHouseholdMembers() {
		// Données de test

		Person person = new Person("Kendrik", "Stelzer", "947 E. Rose Dr", "Culver", "97451", "841-874-7784",
				"bstel@email.com");

		String address = "947 E. Rose Dr";

		// Résultat attendu
		List<String> expected = Arrays.asList("Brian Stelzer", "Shawna Stelzer");

		// Exécution de la méthode à tester
		List<String> actual = personRepository.getHouseholdMembers(person, address);

		// Vérification du résultat
		assertEquals(expected, actual);
	}

}
