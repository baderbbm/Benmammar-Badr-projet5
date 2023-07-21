package com.openclassrooms.SafetyNetAlerts.repository;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PersonRepository {

	private static List<Person> people;

	public List<Person> getPeople() {
		if (people == null)
			people = extractPeople(new SafetyRepository().loadData());
		return people;

	}

	private List<Person> extractPeople(Map<String, List<Map<String, Object>>> data) {
		List<Map<String, Object>> personDataList = data.get("persons");
		List<Person> people = new ArrayList<>();

		for (Map<String, Object> personData : personDataList) {
			Person person = new Person();
			person.setFirstName((String) personData.get("firstName"));
			person.setLastName((String) personData.get("lastName"));
			person.setAddress((String) personData.get("address"));
			person.setCity((String) personData.get("city"));
			person.setZip((String) personData.get("zip"));
			person.setPhone((String) personData.get("phone"));
			person.setEmail((String) personData.get("email"));
			people.add(person);
		}
		return people;
	}

}
