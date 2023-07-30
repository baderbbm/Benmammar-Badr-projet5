package com.openclassrooms.SafetyNetAlerts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // Ajouter une nouvelle personne
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        logger.info("Adding a new person: {}", person);

        Person addedPerson = personService.addPerson(person);
        if (addedPerson != null) {
            logger.info("Person added successfully: {}", addedPerson);
            return ResponseEntity.ok(addedPerson);
        } else {
            logger.error("Failed to add person: {}", person);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mettre à jour une personne existante
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<Person> updatePerson(@PathVariable String firstName,
                                               @PathVariable String lastName,
                                               @RequestBody Person person) {
        logger.info("Updating person - First Name: {}, Last Name: {}, Person: {}", firstName, lastName, person);

        Person updatedPerson = personService.updatePerson(firstName, lastName, person);
        if (updatedPerson != null) {
            logger.info("Person updated successfully: {}", updatedPerson);
            return ResponseEntity.ok(updatedPerson);
        } else {
            logger.warn("Person not found - First Name: {}, Last Name: {}", firstName, lastName);
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une personne
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deletePerson(@PathVariable String firstName,
                                             @PathVariable String lastName) {
        logger.info("Deleting person - First Name: {}, Last Name: {}", firstName, lastName);

        boolean success = personService.deletePerson(firstName, lastName);
        if (success) {
            logger.info("Person deleted successfully - First Name: {}, Last Name: {}", firstName, lastName);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Person not found - First Name: {}, Last Name: {}", firstName, lastName);
            return ResponseEntity.notFound().build();
        }
    }
}
