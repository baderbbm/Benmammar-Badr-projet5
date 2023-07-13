package com.openclassrooms.SafetyNetAlerts;

import com.openclassrooms.SafetyNetAlerts.model.dto.Child;
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverageResponse;
import com.openclassrooms.SafetyNetAlerts.model.dto.PersonCaserne;
import com.openclassrooms.SafetyNetAlerts.model.dto.Resident;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentInfo;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentStation;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.SafetyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SafetyServiceTest {
    private SafetyService safetyService;
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        personRepository = mock(PersonRepository.class);
        safetyService = new SafetyService(personRepository);
    }
    
    @Test
    public void testRetrievePersonsByFirestation() {
        String stationNumber = "4";
        List<String> expectedAddresses = Arrays.asList("489 Manchester St", "112 Steppes Pl");
        FirestationCoverageResponse expectedResponse = new FirestationCoverageResponse();
        List<PersonCaserne> expectedResidents = Arrays.asList(
                new PersonCaserne("Lily", "Cooper","489 Manchester St", "841-874-9845"),
                new PersonCaserne("Tony", "Cooper", "112 Steppes Pl", "841-874-6874"),
                new PersonCaserne("Ron", "Peters", "112 Steppes Pl","841-874-8888"),
                new PersonCaserne("Allison", "Boyd", "112 Steppes Pl","841-874-9888"));
        expectedResponse.setPeople(expectedResidents);
        
        when(personRepository.getAddressesByStationNumber(stationNumber)).thenReturn(expectedAddresses);
        when(personRepository.getPeopleByAddresses(expectedAddresses)).thenReturn(expectedResponse);
        
      
        FirestationCoverageResponse actualResponse = safetyService.retrievePersonsByFirestation(stationNumber);
        
        verify(personRepository, times(1)).getAddressesByStationNumber(stationNumber);
        verify(personRepository, times(1)).getPeopleByAddresses(expectedAddresses);
  
        assertEquals(expectedResponse, actualResponse);
    }

    
    @Test
    public void testRetrievePersonsByFirestation_StationNumberEmpty() {
        FirestationCoverageResponse response = safetyService.retrievePersonsByFirestation("");     
        assertEquals( new FirestationCoverageResponse().getAdultsCount(), response.getAdultsCount());
        assertEquals( new FirestationCoverageResponse().getChildrenCount(), response.getChildrenCount());    
    }

    
    @Test
    public void testRetrieveChildrenByAddress() {
        List<Child> expectedChildren = Arrays
				.asList(new Child("Kendrik", "Stelzer", 9, Arrays.asList("Brian Stelzer", "Shawna Stelzer")));
        when(personRepository.getChildrenByAddress("947 E. Rose Dr")).thenReturn(expectedChildren);
        List<Child> actualChildren = safetyService.retrieveChildrenByAddress("947 E. Rose Dr");
        assertEquals(expectedChildren, actualChildren);
        verify(personRepository, times(1)).getChildrenByAddress("947 E. Rose Dr");
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
        expectedPhoneNumbers.add("841-874-6512");
        expectedPhoneNumbers.add("841-874-8547");
        expectedPhoneNumbers.add("841-874-7462");
        expectedPhoneNumbers.add("841-874-7784");
        expectedPhoneNumbers.add("841-874-7784");
        expectedPhoneNumbers.add("841-874-7784");
        
        when(personRepository.getPhoneNumbersByStationNumber("1")).thenReturn(expectedPhoneNumbers);

        List<String> actualPhoneNumbers = safetyService.retrievePhoneNumbersByFirestation("1");

        assertEquals(expectedPhoneNumbers, actualPhoneNumbers);
        verify(personRepository, times(1)).getPhoneNumbersByStationNumber("1");
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
        
        
        when(personRepository.getResidentsByAddress("947 E. Rose Dr")).thenReturn(expectedResidents);

        List<Resident> actualResidents = safetyService.retrieveResidentsByAddress("947 E. Rose Dr");

        assertEquals(expectedResidents, actualResidents);
        verify(personRepository, times(1)).getResidentsByAddress("947 E. Rose Dr");
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
        
        when(personRepository.getAllResidentsByFirestation("4")).thenReturn(expectedResidents);

        List<ResidentStation> actualResidents = safetyService.retrieveHouseholdsByStations("4");

        assertEquals(expectedResidents, actualResidents);
        verify(personRepository, times(1)).getAllResidentsByFirestation("4");
    }
    
    
    @Test
    public void testRetrieveHouseholdsByStations_emptyStationNumbers() {
        String emptyStationNumbers = "";
        List<ResidentStation> actualResidents = safetyService.retrieveHouseholdsByStations(emptyStationNumbers);
        assertEquals(new ArrayList<ResidentStation>(), actualResidents);
        verify(personRepository, times(0)).getAllResidentsByFirestation(emptyStationNumbers);
    }

    @Test
    public void testRetrievePersonInfoByName() {
        List<ResidentInfo> expectedPersons = new ArrayList<>();
        List<String> medications1 = new ArrayList<>();
		medications1.add("aznol:350mg");
		medications1.add("hydrapermazol:100mg");
		List<String> allergies1 = new ArrayList<>();
		allergies1.add("nillacilan");
        expectedPersons.add(new ResidentInfo("John", "Boyd", "1509 Culver St", "jaboyd@email.com", 39, medications1, allergies1));
        when(personRepository.getPersonInfo("John", "Boyd")).thenReturn(expectedPersons);

        List<ResidentInfo> actualPersons = safetyService.retrievePersonInfoByName("John", "Boyd");

        assertEquals(expectedPersons.get(0).getFirstName(), actualPersons.get(0).getFirstName());
        assertEquals(expectedPersons.get(0).getLastName(), actualPersons.get(0).getLastName());
        assertEquals(expectedPersons.get(0).getAdress(), actualPersons.get(0).getAdress());
        assertEquals(expectedPersons.get(0).getEmail(), actualPersons.get(0).getEmail());
        assertEquals(expectedPersons.get(0).getAge(), actualPersons.get(0).getAge());
        assertEquals(expectedPersons.get(0).getMedications().get(0), actualPersons.get(0).getMedications().get(0));
		assertEquals(expectedPersons.get(0).getMedications().get(1), actualPersons.get(0).getMedications().get(1));
		assertEquals(expectedPersons.get(0).getAllergies().get(0), actualPersons.get(0).getAllergies().get(0));

        verify(personRepository, times(1)).getPersonInfo("John", "Boyd");
    }
    
    @Test
    public void testRetrievePersonInfoByName_emptyName() throws Exception {
        String emptyFirstName = "";
        String emptyLastName = "";
        List<ResidentInfo> actualPersons = safetyService.retrievePersonInfoByName(emptyFirstName, emptyLastName);
        assertEquals(new ArrayList<ResidentInfo>(), actualPersons);
        verify(personRepository, times(0)).getPersonInfo(emptyFirstName, emptyLastName);
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
        when(personRepository.getCommunityEmailsByCity("Culver")).thenReturn(expectedEmails);

        List<String> actualEmails = safetyService.retrieveCommunityEmailsByCity("Culver");

        assertEquals(expectedEmails, actualEmails);
        verify(personRepository, times(1)).getCommunityEmailsByCity("Culver");
    }
    
    @Test
    public void testRetrieveCommunityEmailsByCity_emptyCity() throws Exception {
        String emptyCity = "";
        List<String> actualEmails = safetyService.retrieveCommunityEmailsByCity(emptyCity);
        assertEquals(new ArrayList<String>(), actualEmails);
        verify(personRepository, times(0)).getCommunityEmailsByCity(emptyCity);
    }
    
}
