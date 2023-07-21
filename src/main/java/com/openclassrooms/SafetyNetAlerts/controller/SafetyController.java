package com.openclassrooms.SafetyNetAlerts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.SafetyNetAlerts.model.dto.Child; 
import com.openclassrooms.SafetyNetAlerts.model.dto.FirestationCoverageResponse;
import com.openclassrooms.SafetyNetAlerts.model.dto.Resident;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentInfo;
import com.openclassrooms.SafetyNetAlerts.model.dto.ResidentStation;
import com.openclassrooms.SafetyNetAlerts.service.SafetyService;

@RestController
public class SafetyController {
    private SafetyService safetyService;

    public SafetyController(SafetyService safetyService) {
        this.safetyService = safetyService;
    }

    // Retourner une liste des personnes couvertes par la caserne de pompiers correspondante. 
    // Si le numéro de station = 1, elle doit renvoyer les habitants couverts par la station numéro 1. 
    // La liste doit inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro de téléphone. 
    // De plus, elle doit fournir un décompte du nombre d'adultes et du nombre d'enfants dans la zone desservie 
    
    
    @GetMapping("/firestation")
    public FirestationCoverageResponse getPersonsByFirestation(@RequestParam("stationNumber") String stationNumber) {
        return safetyService.retrievePersonsByFirestation(stationNumber);
    }
    
    // Retourner une liste d'enfants habitant à cette adresse. La liste doit comprendre le prénom et le nom de famille 
    // de chaque enfant, son âge et une liste des autres membres du foyer 

    @GetMapping("/childAlert")
    public List<Child>   getChildrenByAddress(@RequestParam("address") String address) {
        return safetyService.retrieveChildrenByAddress(address);
    }
     
    // Retourner une liste des numéros de téléphone des résidents desservis par la caserne de pompiers. 
    // Nous l'utiliserons pour envoyer des messages texte d'urgence à des foyers spécifiques 
     
    
    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersByFirestation(@RequestParam("firestation") String firestationNumber) {
        return safetyService.retrievePhoneNumbersByFirestation(firestationNumber);
    }

    // Retourner la liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne de pompiers la desservant. 
    // La liste doit inclure le nom, le numéro de téléphone, l'âge et les antécédents médicaux 
    // (médicaments, posologie et allergies) de chaque personne. 
    
    @GetMapping("/fire")
    public List<Resident> getResidentsByAddress(@RequestParam("address") String address) {
        return safetyService.retrieveResidentsByAddress(address); 
    }
    
   
    // Retourner une liste de tous les foyers desservis par la caserne. Cette liste doit regrouper les personnes par adresse. 
    // Elle doit aussi inclure le nom, le numéro de téléphone et l'âge des habitants, 
    // et faire figurer leurs antécédents médicaux (médicaments, posologie et allergies) à côté de chaque nom. 
    
    @GetMapping("/flood/stations")
    public  List<ResidentStation> getHouseholdsByStations(@RequestParam("stations") String stationNumbers) {
        return safetyService.retrieveHouseholdsByStations(stationNumbers);
    }
    
    // Retourner le nom, l'adresse, l'âge, l'adresse mail et les antécédents médicaux (médicaments, posologie, allergies) 
    // de chaque habitant. Si plusieurs personnes portent le même nom, elles doivent toutes apparaître. 
    
    @GetMapping("/personInfo")
    public List<ResidentInfo> getPersonInfoByName(@RequestParam(name="firstName", required=false) String firstName,
                                            					@RequestParam("lastName") String lastName) {
        return safetyService.retrievePersonInfoByName(firstName, lastName); 
    }
    
    // Retourner les adresses mail de tous les habitants de la ville. 
    
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmailsByCity(@RequestParam("city") String city) {
        return safetyService.retrieveCommunityEmailsByCity(city);
    }
   
}

