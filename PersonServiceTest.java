package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PersonServiceTest {

    private PersonRepository personRepositoryMock;
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personRepositoryMock = mock(PersonRepository.class);
        List<Person> persons = new ArrayList<>();

        persons.add(new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"));
        persons.add(new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com"));
   
        when(personRepositoryMock.getPeople()).thenReturn(persons);

        personService = new PersonService(personRepositoryMock);
    }

    @Test
    void getPersons_shouldReturnListOfPersons() {
        List<Person> persons = personService.getPersons();
        assertEquals(2, persons.size());       
    }

    @Test
    void addPerson_shouldAddPersonToList() {
        int initialSize = personService.getPersons().size();
        Person newPerson = new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person addedPerson = personService.addPerson(newPerson);
        assertNotNull(addedPerson);
        assertEquals(initialSize + 1, personService.getPersons().size());
        assertTrue(personService.getPersons().contains(newPerson));
    }

    @Test
    void updatePerson_shouldUpdateExistingPerson() {
        String firstName = "John";
        String lastName = "Boyd";
        Person updatedPerson = new Person("John", "Boyd", "New Address", "New City", "12345", "555-555-5555", "updated@email.com");
        Person result = personService.updatePerson(firstName, lastName, updatedPerson);
        assertNotNull(result);
        assertEquals(updatedPerson.getFirstName(), result.getFirstName());
        assertEquals(updatedPerson.getLastName(), result.getLastName());
        assertEquals(updatedPerson.getAddress(), result.getAddress());
        assertEquals(updatedPerson.getCity(), result.getCity());
        assertEquals(updatedPerson.getZip(), result.getZip());
        assertEquals(updatedPerson.getPhone(), result.getPhone());
        assertEquals(updatedPerson.getEmail(), result.getEmail());
    }

    @Test
    void deletePerson_shouldRemovePersonFromList() {
        String firstName = "John";
        String lastName = "Boyd";
        boolean isDeleted = personService.deletePerson(firstName, lastName);
        assertTrue(isDeleted);
        assertFalse(personService.getPersons().stream().anyMatch(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)));
    }

    @Test
    void deletePerson_shouldNotRemoveNonExistentPerson() {
        String firstName = "NonExistent";
        String lastName = "Person";
        boolean isDeleted = personService.deletePerson(firstName, lastName);
        assertFalse(isDeleted);
    }
}

