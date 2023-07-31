package com.openclassrooms.SafetyNetAlerts.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.SafetyNetAlerts.model.dto.Address;
import com.openclassrooms.SafetyNetAlerts.model.dto.Child;
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverage;
import com.openclassrooms.SafetyNetAlerts.model.dto.Resident;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentInfo;
import com.openclassrooms.SafetyNetAlerts.service.SafetyService;

@RestController
public class SafetyController {
    private static final Logger logger = LoggerFactory.getLogger(SafetyController.class);

    private SafetyService safetyService;

    public SafetyController(SafetyService safetyService) {
        this.safetyService = safetyService;
    }

    @GetMapping("/firestation")
    public FirestationCoverage getPersonsByFirestation(@RequestParam("stationNumber") String stationNumber) {
        logger.info("/firestation - GET - Retrieving persons by firestation - Station Number: {}", stationNumber);
        return safetyService.retrievePersonsByFirestation(stationNumber);
    }

    @GetMapping("/childAlert")
    public List<Child> getChildrenByAddress(@RequestParam("address") String address) {
        logger.info("/childAlert - GET - Retrieving children by address - Address: {}", address);
        return safetyService.retrieveChildrenByAddress(address);
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersByFirestation(@RequestParam("firestation") String firestationNumber) {
        logger.info("/phoneAlert - GET - Retrieving phone numbers by firestation - Firestation Number: {}", firestationNumber);
        return safetyService.retrievePhoneNumbersByFirestation(firestationNumber);
    }

    @GetMapping("/fire")
    public List<Resident> getResidentsByAddress(@RequestParam("address") String address) {
        logger.info("/fire - GET - Retrieving residents by address - Address: {}", address);
        return safetyService.retrieveResidentsByAddress(address);
    }

    @GetMapping("/flood/stations")
    public List<Address> getHouseholdsByStations(@RequestParam("stations") String stationNumbers) {
        logger.info("/flood/stations - GET - Retrieving households by stations - Station Numbers: {}", stationNumbers);
        return safetyService.getAllResidentsByFirestations(Arrays.asList(stationNumbers.split(",")));
    }

    @GetMapping("/personInfo")
    public List<ResidentInfo> getPersonInfoByName(@RequestParam(name = "firstName", required = false) String firstName,
                                                  @RequestParam("lastName") String lastName) {
        logger.info("/personInfo - GET - Retrieving person info by name - First Name: {}, Last Name: {}", firstName, lastName);
        return safetyService.retrievePersonInfoByName(firstName, lastName);
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmailsByCity(@RequestParam("city") String city) {
        logger.info("/communityEmail - GET - Retrieving community emails by city - City: {}", city);
        return safetyService.retrieveCommunityEmailsByCity(city);
    }
}
