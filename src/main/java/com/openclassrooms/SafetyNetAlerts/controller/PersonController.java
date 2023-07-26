package com.openclassrooms.SafetyNetAlerts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // Ajouter une nouvelle personne
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person addedPerson = personService.addPerson(person);
        if (addedPerson != null) {
            return ResponseEntity.ok(addedPerson);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mettre à jour une personne existante
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<Person> updatePerson(@PathVariable String firstName,
    										   @PathVariable String lastName,
                                               @RequestBody Person person) {
        Person updatedPerson = personService.updatePerson(firstName, lastName, person);
        if (updatedPerson != null) {
            return ResponseEntity.ok(updatedPerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une personne
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deletePerson(@PathVariable String firstName,
                                             @PathVariable String lastName) {
        boolean success = personService.deletePerson(firstName, lastName);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
