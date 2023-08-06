package com.openclassrooms.SafetyNetAlerts;

import com.openclassrooms.SafetyNetAlerts.controller.SafetyController;
import com.openclassrooms.SafetyNetAlerts.model.dto.Address;
import com.openclassrooms.SafetyNetAlerts.model.dto.Child;
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverage;
import com.openclassrooms.SafetyNetAlerts.model.dto.PersonCaserne;
import com.openclassrooms.SafetyNetAlerts.model.dto.Resident;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentInfo;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentStation;
import com.openclassrooms.SafetyNetAlerts.service.SafetyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SafetyController.class)
public class SafetyControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SafetyService safetyService;

	@Test
	public void testGetPersonsByFirestation() throws Exception {
	    String stationNumber = "4";
	    FirestationCoverage expectedResponse = new FirestationCoverage();

	    expectedResponse.setPeople(Arrays.asList(
	            new PersonCaserne("Lily", "Cooper", "489 Manchester St", "841-874-9845"),
	            new PersonCaserne("Tony", "Cooper", "112 Steppes Pl", "841-874-6874"),
	            new PersonCaserne("Ron", "Peters", "112 Steppes Pl", "841-874-8888"),
	            new PersonCaserne("Allison", "Boyd", "112 Steppes Pl", "841-874-9888")));

	    when(safetyService.retrievePersonsByFirestation(stationNumber)).thenReturn(expectedResponse);

	    mockMvc.perform(get("/firestation").param("stationNumber", stationNumber))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.adultsCount").value(expectedResponse.getAdultsCount()))
	            .andExpect(jsonPath("$.childrenCount").value(expectedResponse.getChildrenCount()))
	            .andExpect(jsonPath("$.people[*].firstName", hasItem("Lily")))
	            .andExpect(jsonPath("$.people[*].firstName", hasItem("Tony")))
	            .andExpect(jsonPath("$.people[*].firstName", hasItem("Ron")))
	            .andExpect(jsonPath("$.people[*].firstName", hasItem("Allison")));

	    verify(safetyService, times(1)).retrievePersonsByFirestation(stationNumber);
	}
	
	@Test
	public void testGetChildrenByAddress() throws Exception {

		String address = "947 E. Rose Dr";

		List<Child> expectedChildren = Arrays.asList(new Child("Kendrik", "Stelzer", 9, Arrays.asList("Brian Stelzer", "Shawna Stelzer")));

		when(safetyService.retrieveChildrenByAddress(address)).thenReturn(expectedChildren);

		mockMvc.perform(get("/childAlert").param("address", address)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value(expectedChildren.get(0).getFirstName()))
				.andExpect(jsonPath("$[0].lastName").value(expectedChildren.get(0).getLastName()));

		verify(safetyService, times(1)).retrieveChildrenByAddress(address);
	}

	@Test
	public void testGetPhoneNumbersByFirestation() throws Exception {

		String stationNumber = "1";

		List<String> expectedPhoneNumbers = Arrays.asList("841-874-6512", "841-874-8547", "841-874-7462",
				"841-874-7784", "841-874-7784", "841-874-7784");

		when(safetyService.retrievePhoneNumbersByFirestation(stationNumber)).thenReturn(expectedPhoneNumbers);

		mockMvc.perform(get("/phoneAlert").param("firestation", stationNumber)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value(expectedPhoneNumbers.get(0)))
				.andExpect(jsonPath("$[1]").value(expectedPhoneNumbers.get(1)));

		verify(safetyService, times(1)).retrievePhoneNumbersByFirestation(stationNumber);
	}

	@Test
	public void testGetResidentsByAddress() throws Exception {

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

		when(safetyService.retrieveResidentsByAddress(address)).thenReturn(expectedResidents);

		mockMvc.perform(get("/fire").param("address", address)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value(expectedResidents.get(0).getFirstName()))
				.andExpect(jsonPath("$[0].lastName").value(expectedResidents.get(0).getLastName()));

		verify(safetyService, times(1)).retrieveResidentsByAddress(address);
	}

	@Test
    public void testGetHouseholdsByStations() throws Exception {

        String stationNumber1 = "4";
        List<String> medications1 = new ArrayList<>();
        medications1.add("");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("");
        List<String> medications2 = new ArrayList<>();
        medications2.add("hydrapermazol:300mg");
        medications2.add("dodoxadin:30mg");

        List<String> allergies2 = new ArrayList<>();
        allergies2.add("shellfish");

        List<String> medications3 = new ArrayList<>();
        medications3.add("aznol:200mg");

        List<String> allergies3 = new ArrayList<>();
        allergies3.add("nillacilan");

        List<Address> expectedHouseholds1 = new ArrayList<>();
        expectedHouseholds1.add(new Address("489 Manchester St",
                Arrays.asList(new ResidentStation("Lily", "Cooper", "841-874-9845", 29, medications1, allergies1))));
        expectedHouseholds1.add(new Address("112 Steppes Pl",
                Arrays.asList(new ResidentStation("Tony", "Cooper", "841-874-6874", 29, medications2, allergies2))));
        expectedHouseholds1.add(new Address("112 Steppes Pl",
                Arrays.asList(new ResidentStation("Ron", "Peters", "841-874-8888", 58, medications1, allergies1))));
        expectedHouseholds1.add(new Address("112 Steppes Pl",
                Arrays.asList(new ResidentStation("Allison", "Boyd", "841-874-9888", 58, medications3, allergies3))));

        List<String> stationNumbers = new ArrayList<>();
        stationNumbers.add(stationNumber1);
        when(safetyService.getAllResidentsByFirestations(stationNumbers)).thenReturn(expectedHouseholds1);

        mockMvc.perform(get("/flood/stations").param("stations", stationNumber1)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("489 Manchester St"))
                .andExpect(jsonPath("$[0].residentStation[0].firstName").value("Lily"))
                .andExpect(jsonPath("$[1].address").value("112 Steppes Pl"))
                .andExpect(jsonPath("$[1].residentStation[0].firstName").value("Tony"))
                .andExpect(jsonPath("$[2].address").value("112 Steppes Pl"))
                .andExpect(jsonPath("$[2].residentStation[0].firstName").value("Ron"))
                .andExpect(jsonPath("$[3].address").value("112 Steppes Pl"))
                .andExpect(jsonPath("$[3].residentStation[0].firstName").value("Allison"));

        String stationNumber2 = "2";
        List<String> medications4 = new ArrayList<>();
        medications4.add("aznol:60mg");
        medications4.add("hydrapermazol:900mg");
        medications4.add("pharmacol:5000mg");
        medications4.add("terazine:500mg");

        List<String> allergies4 = new ArrayList<>();
        allergies4.add("peanut");
        allergies4.add("shellfish");
        allergies4.add("aznol");

        List<Address> expectedHouseholds2 = new ArrayList<>();
        expectedHouseholds2.add(new Address("892 Downing Ct",
                Arrays.asList(new ResidentStation("Sophia", "Zemicks", "841-874-7878", 35, medications4, allergies4))));
        expectedHouseholds2.add(new Address("892 Downing Ct",
                Arrays.asList(new ResidentStation("Warren", "Zemicks", "841-874-7512", 38, medications1, allergies1))));
        expectedHouseholds2.add(new Address("892 Downing Ct",
                Arrays.asList(new ResidentStation("Zach", "Zemicks", "841-874-7512", 6, medications1, allergies1))));

        stationNumbers.add(stationNumber2);
        when(safetyService.getAllResidentsByFirestations(stationNumbers)).thenReturn(expectedHouseholds2);

        mockMvc.perform(get("/flood/stations").param("stations", stationNumber1 + "," + stationNumber2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("892 Downing Ct"))
                .andExpect(jsonPath("$[0].residentStation[0].firstName").value("Sophia"))
                .andExpect(jsonPath("$[1].address").value("892 Downing Ct"))
                .andExpect(jsonPath("$[1].residentStation[0].firstName").value("Warren"))
                .andExpect(jsonPath("$[2].address").value("892 Downing Ct"))
                .andExpect(jsonPath("$[2].residentStation[0].firstName").value("Zach"));


        verify(safetyService, times(1)).getAllResidentsByFirestations(stationNumbers);
	}

	@Test
	public void testGetPersonInfoByName() throws Exception {
		String firstName = "John";
		String lastName = "Boyd";

		List<ResidentInfo> expectedPersons = new ArrayList<>();
		List<String> medications1 = new ArrayList<>();
		medications1.add("aznol:350mg");
		medications1.add("hydrapermazol:100mg");
		List<String> allergies1 = new ArrayList<>();
		allergies1.add("nillacilan");
		expectedPersons.add(
				new ResidentInfo("John", "Boyd", "1509 Culver St", "jaboyd@email.com", 39, medications1, allergies1));
		when(safetyService.retrievePersonInfoByName(firstName, lastName)).thenReturn(expectedPersons);

		mockMvc.perform(get("/personInfo").param("firstName", firstName).param("lastName", lastName))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value(expectedPersons.get(0).getFirstName()))
				.andExpect(jsonPath("$[0].lastName").value(expectedPersons.get(0).getLastName()));

		verify(safetyService, times(1)).retrievePersonInfoByName(firstName, lastName);
	}

	@Test
	public void testGetCommunityEmailsByCity() throws Exception {
		String city = "Culver";
		List<String> expectedEmails = Arrays.asList("jaboyd@email.com", "drk@email.com", "tenz@email.com",
				"jaboyd@email.com", "jaboyd@email.com", "drk@email.com", "tenz@email.com", "jaboyd@email.com",
				"jaboyd@email.com", "tcoop@ymail.com", "lily@email.com", "soph@email.com", "ward@email.com",
				"zarc@email.com", "reg@email.com", "jpeter@email.com", "jpeter@email.com", "aly@imail.com",
				"bstel@email.com", "ssanw@email.com", "bstel@email.com", "clivfd@ymail.com", "gramps@email.com");

		when(safetyService.retrieveCommunityEmailsByCity(city)).thenReturn(expectedEmails);

		mockMvc.perform(get("/communityEmail").param("city", city)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value(expectedEmails.get(0)))
				.andExpect(jsonPath("$[1]").value(expectedEmails.get(1)));

		verify(safetyService, times(1)).retrieveCommunityEmailsByCity(city);
	}
}
