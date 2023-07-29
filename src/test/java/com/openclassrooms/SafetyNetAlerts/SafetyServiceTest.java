package com.openclassrooms.SafetyNetAlerts;

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
import com.openclassrooms.SafetyNetAlerts.service.SafetyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SafetyServiceTest {

	private SafetyService safetyService;

	private SafetyRepository safetyRepository;
	private PersonRepository personRepository;
	private FirestationRepository firestationRepository;
	private MedicalRecordRepository medicalRecordRepository;
	
	@BeforeEach
	public void setUp() {
		personRepository = new PersonRepository();
		safetyRepository = new SafetyRepository();
		firestationRepository = new FirestationRepository();
		medicalRecordRepository = new MedicalRecordRepository();
		safetyService = new SafetyService(safetyRepository, personRepository, firestationRepository,medicalRecordRepository);
	}
	

	@Test
	public void testRetrievePersonsByFirestation() {
		String stationNumber = "4";
		List<String> expectedAddresses = Arrays.asList("489 Manchester St", "112 Steppes Pl");
		FirestationCoverage expectedResponse = new FirestationCoverage();
		List<PersonCaserne> expectedResidents = Arrays.asList(
				new PersonCaserne("Lily", "Cooper", "489 Manchester St", "841-874-9845"),
				new PersonCaserne("Tony", "Cooper", "112 Steppes Pl", "841-874-6874"),
				new PersonCaserne("Ron", "Peters", "112 Steppes Pl", "841-874-8888"),
				new PersonCaserne("Allison", "Boyd", "112 Steppes Pl", "841-874-9888"));
		expectedResponse.setPeople(expectedResidents);

		FirestationCoverage actualResponse = safetyService.retrievePersonsByFirestation(stationNumber);

		assertEquals(expectedResponse.getPeople().get(0).getFirstName(),actualResponse.getPeople().get(0).getFirstName());
		assertEquals(expectedResponse.getPeople().get(0).getLastName(),actualResponse.getPeople().get(0).getLastName());
		assertEquals(expectedResponse.getPeople().get(0).getAddress(), actualResponse.getPeople().get(0).getAddress());
		assertEquals(expectedResponse.getPeople().get(0).getPhone(), actualResponse.getPeople().get(0).getPhone());
	}

	@Test
	public void testRetrievePersonsByFirestation_StationNumberEmpty() {
		FirestationCoverage response = safetyService.retrievePersonsByFirestation("");
		assertEquals(new FirestationCoverage().getAdultsCount(), response.getAdultsCount());
		assertEquals(new FirestationCoverage().getChildrenCount(), response.getChildrenCount());
	}

	@Test
	public void testRetrieveChildrenByAddress() {
		List<Child> expectedChildren = Arrays.asList(new Child("Kendrik", "Stelzer", 9, Arrays.asList("Brian Stelzer", "Shawna Stelzer")));
	
		List<Child> actualChildren = safetyService.retrieveChildrenByAddress("947 E. Rose Dr");
		assertEquals(expectedChildren.get(0).getFirstName(), actualChildren.get(0).getFirstName());
		assertEquals(expectedChildren.get(0).getLastName(), actualChildren.get(0).getLastName());
	}

	@Test
	public void testRetrieveChildrenByAddress_AddressEmpty() {
		List<Child> children = safetyService.retrieveChildrenByAddress("");
		assertNotNull(children);
		assertEquals(0, children.size());
	}

	@Test
	public void testRetrievePhoneNumbersByFirestation() {
		List<String> expectedPhoneNumbers = new ArrayList<>();
		expectedPhoneNumbers.add("841-874-9845");
		expectedPhoneNumbers.add("841-874-6874");
		expectedPhoneNumbers.add("841-874-8888");
		expectedPhoneNumbers.add("841-874-9888");
		List<String> actualPhoneNumbers = safetyService.getPhoneNumbersByStationNumber("4");
		assertEquals(expectedPhoneNumbers, actualPhoneNumbers);
		assertEquals(expectedPhoneNumbers.get(0), actualPhoneNumbers.get(0));
		assertEquals(expectedPhoneNumbers.get(1), actualPhoneNumbers.get(1));
		assertEquals(expectedPhoneNumbers.get(2), actualPhoneNumbers.get(2));
		assertEquals(expectedPhoneNumbers.get(3), actualPhoneNumbers.get(3));
	}

	@Test
	public void testRetrievePhoneNumbersByFirestation_FirestationNumberEmpty() {
		List<String> phoneNumbers = safetyService.retrievePhoneNumbersByFirestation("");
		assertNotNull(phoneNumbers);
		assertEquals(0, phoneNumbers.size());
	}

	@Test
	public void testRetrieveResidentsByAddress() {
		String address = "947 E. Rose Dr";
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

		List<Resident> expectedResidents = Arrays.asList(
				new Resident("Brian", "Stelzer", "841-874-7784", "1", 47, medications1, allergies1),
				new Resident("Shawna", "Stelzer", "841-874-7784", "1", 43, medications2, allergies2),
				new Resident("Kendrik", "Stelzer", "841-874-7784", "1", 9, medications3, allergies3));

		List<Resident> actualResidents = safetyService.retrieveResidentsByAddress("947 E. Rose Dr");

		assertEquals(expectedResidents.get(0).getFirstName(), actualResidents.get(0).getFirstName());
		assertEquals(expectedResidents.get(0).getLastName(), actualResidents.get(0).getLastName());
		assertEquals(expectedResidents.get(0).getAge(), actualResidents.get(0).getAge());
		assertEquals(expectedResidents.get(0).getPhone(), actualResidents.get(0).getPhone());
	}

	@Test
	public void testRetrieveResidentsByAddress_AddressEmpty() {
		List<Resident> residents = safetyService.retrieveResidentsByAddress("");
		assertNotNull(residents);
		assertEquals(0, residents.size());
	}

	@Test
	public void testRetrieveHouseholdsByStations() {
		List<ResidentStation> expectedResidents = Arrays.asList(new ResidentStation("Lily", "Cooper", "841-874-9845"),
				new ResidentStation("Tony", "Cooper", "841-874-6874"),
				new ResidentStation("Ron", "Peters", "841-874-8888"),
				new ResidentStation("Allison", "Boyd", "841-874-9888"));

		List<Address> actualResidents = safetyService.getAllResidentsByFirestation("4");

		assertEquals(expectedResidents.get(0).getFirstName(), actualResidents.get(0).getResidentStation().get(0).getFirstName());

	}

	@Test
	public void testRetrieveHouseholdsByStations_emptyStationNumbers() {
		String emptyStationNumbers = "";
		List<Address> actualResidents = safetyService.getAllResidentsByFirestation(emptyStationNumbers);
		assertEquals(new ArrayList<ResidentStation>(), actualResidents);
		
	}

	@Test
	public void testRetrievePersonInfoByName() {
		List<ResidentInfo> expectedPersons = new ArrayList<>();
		List<String> medications1 = new ArrayList<>();
		medications1.add("aznol:350mg");
		medications1.add("hydrapermazol:100mg");
		List<String> allergies1 = new ArrayList<>();
		allergies1.add("nillacilan");
		expectedPersons.add(
				new ResidentInfo("John", "Boyd", "1509 Culver St", "jaboyd@email.com", 39, medications1, allergies1));
		

		List<ResidentInfo> actualPersons = safetyService.retrievePersonInfoByName("John", "Boyd");

		assertEquals(expectedPersons.get(0).getFirstName(), actualPersons.get(0).getFirstName());
		assertEquals(expectedPersons.get(0).getLastName(), actualPersons.get(0).getLastName());
		assertEquals(expectedPersons.get(0).getAdress(), actualPersons.get(0).getAdress());
		assertEquals(expectedPersons.get(0).getEmail(), actualPersons.get(0).getEmail());
		assertEquals(expectedPersons.get(0).getAge(), actualPersons.get(0).getAge());
		assertEquals(expectedPersons.get(0).getMedications().get(0), actualPersons.get(0).getMedications().get(0));
		assertEquals(expectedPersons.get(0).getMedications().get(1), actualPersons.get(0).getMedications().get(1));
		assertEquals(expectedPersons.get(0).getAllergies().get(0), actualPersons.get(0).getAllergies().get(0));

	}

	@Test
	public void testRetrievePersonInfoByName_emptyName() throws Exception {
		String emptyFirstName = "";
		String emptyLastName = "";
		List<ResidentInfo> actualPersons = safetyService.retrievePersonInfoByName(emptyFirstName, emptyLastName);
		assertEquals(new ArrayList<ResidentInfo>(), actualPersons);
	}

	@Test
	public void testRetrieveCommunityEmailsByCity() {
		List<String> expectedEmails = new ArrayList<>();
		expectedEmails.add("jaboyd@email.com");
		expectedEmails.add("drk@email.com");
		expectedEmails.add("tenz@email.com");
		expectedEmails.add("tcoop@ymail.com");
		expectedEmails.add("lily@email.com");
		expectedEmails.add("soph@email.com");
		expectedEmails.add("ward@email.com");
		expectedEmails.add("zarc@email.com");
		expectedEmails.add("reg@email.com");
		expectedEmails.add("jpeter@email.com");
		expectedEmails.add("aly@imail.com");
		expectedEmails.add("bstel@email.com");
		expectedEmails.add("ssanw@email.com");
		expectedEmails.add("clivfd@ymail.com");
		expectedEmails.add("gramps@email.com");

		List<String> actualEmails = safetyService.retrieveCommunityEmailsByCity("Culver");

		assertEquals(expectedEmails.get(0), actualEmails.get(0));
		assertEquals(expectedEmails.get(1), actualEmails.get(1));
		assertEquals(expectedEmails.get(2), actualEmails.get(2));
	}

	@Test
	public void testRetrieveCommunityEmailsByCity_emptyCity() throws Exception {
		String emptyCity = "";
		List<String> actualEmails = safetyService.retrieveCommunityEmailsByCity(emptyCity);
		assertEquals(new ArrayList<String>(), actualEmails);
	}

	@Test
	public void testgetAddressesByStationNumber() {
		String stationNumber = "4";
		List<String> expected = new ArrayList<>();
		expected.add("489 Manchester St");
		expected.add("112 Steppes Pl");
		List<String> actual = safetyService.getAddressesByStationNumber(stationNumber);
		assertEquals(expected.get(0), actual.get(0));
		assertEquals(expected.get(1), actual.get(1));
	}

	@Test
	public void testGetPeopleByAddresses() {
		List<String> addresses = Arrays.asList("489 Manchester St");
		FirestationCoverage expected = new FirestationCoverage();
		expected.setPeople(Arrays.asList(new PersonCaserne("Lily", "Cooper", "489 Manchester St", "841-874-9845")));
		expected.setAdultsCount(1);
		expected.setChildrenCount(0);
		FirestationCoverage actual = safetyService.getPeopleByAddresses(addresses);
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
		List<Person> actual = safetyService.getPeopleByAddress(address);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetChildrenByAddress() {
		String address = "947 E. Rose Dr";
		List<Child> expected = Arrays.asList(new Child("Kendrik", "Stelzer", 9, Arrays.asList("Brian Stelzer", "Shawna Stelzer")));
		List<Child> actual = safetyService.getChildrenByAddress(address);
		assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
		assertEquals(expected.get(0).getLastName(), actual.get(0).getLastName());
		assertEquals(expected.get(0).getAge(), actual.get(0).getAge());
		assertEquals(expected.get(0).getHouseholdMembers(), actual.get(0).getHouseholdMembers());
	}

	@Test
	public void testGetResidentsByAddress() {
		String address = "947 E. Rose Dr";
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
		List<Resident> actual = safetyService.getResidentsByAddress(address);
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
	public void testGetAllResidentsByFirestation() {
		String stationNumber = "4";
		List<ResidentStation> expected = Arrays.asList(new ResidentStation("Lily", "Cooper", "841-874-9845"),
				new ResidentStation("Tony", "Cooper", "841-874-6874"),
				new ResidentStation("Ron", "Peters", "841-874-8888"),
				new ResidentStation("Allison", "Boyd", "841-874-9888"));
		Address expectedaddress= new Address();
		expectedaddress.setAddress("489 Manchester St");
		expectedaddress.setResidentStation(expected);
		List<Address> actual = safetyService.getAllResidentsByFirestation(stationNumber);
		assertEquals(expectedaddress.getAddress(), actual.get(0).getAddress());
		assertEquals(expectedaddress.getResidentStation().get(0).getFirstName(), actual.get(0).getResidentStation().get(0).getFirstName());
		
	}

	@Test
	public void testGetPersonInfo() {
		String firstName = "John";
		String lastName = "Boyd";

		List<String> medications1 = new ArrayList<>();
		medications1.add("aznol:350mg");
		medications1.add("hydrapermazol:100mg");
		List<String> allergies1 = new ArrayList<>();
		allergies1.add("nillacilan");

		List<ResidentInfo> expected = Arrays.asList(
				new ResidentInfo("John", "Boyd", "1509 Culver St", "jaboyd@email.com", 39, medications1, allergies1));

		List<ResidentInfo> actual = safetyService.getPersonInfo(firstName, lastName);

		assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
		assertEquals(expected.get(0).getLastName(), actual.get(0).getLastName());
		assertEquals(expected.get(0).getEmail(), actual.get(0).getEmail());
		assertEquals(expected.get(0).getAge(), actual.get(0).getAge());
		assertEquals(expected.get(0).getMedications().get(0), actual.get(0).getMedications().get(0));
		assertEquals(expected.get(0).getMedications().get(1), actual.get(0).getMedications().get(1));
		assertEquals(expected.get(0).getAllergies().get(0), actual.get(0).getAllergies().get(0));
	}

	@Test
	public void testGetAllPersonInfo() {
		String firstName = "";
		String lastName = "Boyd";

		List<String> medications1 = new ArrayList<>();
		medications1.add("aznol:350mg");
		medications1.add("hydrapermazol:100mg");
		List<String> allergies1 = new ArrayList<>();
		allergies1.add("nillacilan");

		List<ResidentInfo> expected = Arrays.asList(
				new ResidentInfo("John", "Boyd", "1509 Culver St", "jaboyd@email.com", 39, medications1, allergies1));

		List<ResidentInfo> actual = safetyService.getPersonInfo(lastName);

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

		List<String> actual = safetyService.getCommunityEmailsByCity(city);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCommunityEmailsByCity_NonExistentCity() {
		String city = "Paris";
		List<String> expected = Arrays.asList();
		List<String> actual = safetyService.getCommunityEmailsByCity(city);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetHouseholdMembers() {
		Person person = new Person("Kendrik", "Stelzer", "947 E. Rose Dr", "Culver", "97451", "841-874-7784",
				"bstel@email.com");
		String address = "947 E. Rose Dr";
		List<String> expected = Arrays.asList("Brian Stelzer", "Shawna Stelzer");
		List<String> actual = safetyService.getHouseholdMembers(person, address);
		assertEquals(expected, actual);
	}
}
