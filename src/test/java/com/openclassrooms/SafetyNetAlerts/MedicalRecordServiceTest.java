package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class MedicalRecordServiceTest {

	private MedicalRecordRepository medicalRecordRepositoryMock;
	private MedicalRecordService medicalRecordService;

	@BeforeEach
	void setUp() {
		medicalRecordRepositoryMock = mock(MedicalRecordRepository.class);
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		LocalDate johnBirthdate = LocalDate.of(1984, 3, 6);
		MedicalRecord johnRecord = new MedicalRecord("John", "Boyd", johnBirthdate,
				List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));
		LocalDate jacobBirthdate = LocalDate.of(1989, 3, 6);
		MedicalRecord jacobRecord = new MedicalRecord("Jacob", "Boyd", jacobBirthdate,
				List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), List.of());
		medicalRecords.add(johnRecord);
		medicalRecords.add(jacobRecord);
		when(medicalRecordRepositoryMock.getMedicalRecord()).thenReturn(medicalRecords);
		medicalRecordService = new MedicalRecordService(medicalRecordRepositoryMock);
	}

	@Test
	void getMedicalRecords_shouldReturnListOfMedicalRecords() {
		List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecords();
		assertEquals(2, medicalRecords.size());
	}

	@Test
	void addMedicalRecord_shouldAddMedicalRecordToList() {
		int initialSize = medicalRecordService.getMedicalRecords().size();
		LocalDate tenleyBirthdate = LocalDate.of(2012, 2, 18);
		MedicalRecord newMedicalRecord = new MedicalRecord("Tenley", "Boyd", tenleyBirthdate, List.of(),
				List.of("peanut"));
		MedicalRecord addedMedicalRecord = medicalRecordService.addMedicalRecord(newMedicalRecord);
		assertNotNull(addedMedicalRecord);
		assertEquals(initialSize + 1, medicalRecordService.getMedicalRecords().size());
		assertTrue(medicalRecordService.getMedicalRecords().contains(newMedicalRecord));
	}

	@Test
	void updateMedicalRecord_shouldUpdateExistingMedicalRecord() {
		String firstName = "John";
		String lastName = "Boyd";

		LocalDate johnBirthdate = LocalDate.of(1984, 3, 6);
		MedicalRecord updatedMedicalRecord = new MedicalRecord("John", "Boyd", johnBirthdate,
				List.of("updated_med:500mg"), List.of("updated_allergy"));
		MedicalRecord result = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
		assertNotNull(result);
		assertEquals(updatedMedicalRecord.getFirstName(), result.getFirstName());
		assertEquals(updatedMedicalRecord.getLastName(), result.getLastName());
		assertEquals(updatedMedicalRecord.getBirthdate(), result.getBirthdate());
		assertEquals(updatedMedicalRecord.getMedications(), result.getMedications());
		assertEquals(updatedMedicalRecord.getAllergies(), result.getAllergies());
	}

	@Test
	void deleteMedicalRecord_shouldRemoveMedicalRecordFromList() {
		String firstName = "John";
		String lastName = "Boyd";
		boolean isDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		assertTrue(isDeleted);
		assertFalse(medicalRecordService.getMedicalRecords().stream()
				.anyMatch(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)));
	}

	@Test
	void deleteMedicalRecord_shouldNotRemoveNonExistentMedicalRecord() {
		String firstName = "NonExistent";
		String lastName = "Person";
		boolean isDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		assertFalse(isDeleted);
	}
}
