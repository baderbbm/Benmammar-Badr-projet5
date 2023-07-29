package com.openclassrooms.SafetyNetAlerts;

import com.openclassrooms.SafetyNetAlerts.controller.PersonController;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonControllerTest {

    @Mock
    private PersonService personServiceMock;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPerson_shouldReturnAddedPerson() {
        Person newPerson = new Person("John", "Doe", "123 Main St", "City", "12345", "555-555-5555", "john@example.com");
        when(personServiceMock.addPerson(newPerson)).thenReturn(newPerson);
        ResponseEntity<Person> response = personController.addPerson(newPerson);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newPerson, response.getBody());
    }

    @Test
    void addPerson_shouldReturnInternalServerError() {
        Person newPerson = new Person("John", "Doe", "123 Main St", "City", "12345", "555-555-5555", "john@example.com");
        when(personServiceMock.addPerson(newPerson)).thenReturn(null);
        ResponseEntity<Person> response = personController.addPerson(newPerson);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updatePerson_shouldReturnUpdatedPerson() {
        String firstName = "John";
        String lastName = "Doe";
        Person updatedPerson = new Person("Updated", "Person", "New Address", "New City", "12345", "555-555-5555", "updated@example.com");
        when(personServiceMock.updatePerson(firstName, lastName, updatedPerson)).thenReturn(updatedPerson);
        ResponseEntity<Person> response = personController.updatePerson(firstName, lastName, updatedPerson);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPerson, response.getBody());
    }

    @Test
    void updatePerson_shouldReturnNotFound() {
        String firstName = "NonExistent";
        String lastName = "Person";
        Person updatedPerson = new Person("Updated", "Person", "New Address", "New City", "12345", "555-555-5555", "updated@example.com");
        when(personServiceMock.updatePerson(firstName, lastName, updatedPerson)).thenReturn(null);
        ResponseEntity<Person> response = personController.updatePerson(firstName, lastName, updatedPerson);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deletePerson_shouldReturnSuccess() {
        String firstName = "John";
        String lastName = "Doe";
        when(personServiceMock.deletePerson(firstName, lastName)).thenReturn(true);
        ResponseEntity<Void> response = personController.deletePerson(firstName, lastName);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deletePerson_shouldReturnNotFound() {
        String firstName = "NonExistent";
        String lastName = "Person";
        when(personServiceMock.deletePerson(firstName, lastName)).thenReturn(false);
        ResponseEntity<Void> response = personController.deletePerson(firstName, lastName);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}

