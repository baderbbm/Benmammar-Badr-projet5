package com.openclassrooms.SafetyNetAlerts.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;

import java.util.List;

@Service
public class PersonService {
	private PersonRepository personRepository;

	public PersonService(PersonRepository personRepository) {
		this.personRepository=personRepository;
	}

	public List<Person> getPersons() {
		return personRepository.getPeople();
	}

	public Person addPerson(Person person) {
		getPersons().add(person);
		return person;
	}

	public Person updatePerson(String firstName, String lastName, Person updatedPerson) {
		for (Person existingPerson : getPersons()) {
			if (existingPerson.getFirstName().equals(firstName) && existingPerson.getLastName().equals(lastName)) {
				existingPerson.setAddress(updatedPerson.getAddress());
				existingPerson.setCity(updatedPerson.getCity());
				existingPerson.setZip(updatedPerson.getZip());
				existingPerson.setPhone(updatedPerson.getPhone());
				existingPerson.setEmail(updatedPerson.getEmail());
				return existingPerson;
			}
		}
		return null;
	}

	public boolean deletePerson(String firstName, String lastName) {
		Person foundPerson = null;
		for (Person person : getPersons()) {
			if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
				foundPerson = person;
				break;
			}
		}
		if (foundPerson != null) {
			getPersons().remove(foundPerson);
			return true;
		} else {
			return false;
		}
	}
}
