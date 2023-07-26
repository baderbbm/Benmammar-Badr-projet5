package com.openclassrooms.SafetyNetAlerts;


import com.openclassrooms.SafetyNetAlerts.controller.MedicalRecordController;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private MockMvc mockMvc;

    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        medicalRecord = new MedicalRecord("John", "Boyd", LocalDate.of(1984, 3, 6),
                List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));
    }

    @Test
    void addMedicalRecord_shouldReturnAddedMedicalRecord() throws Exception {
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);

        mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"birthdate\":\"1984-03-06\", \"medications\":[\"aznol:350mg\", \"hydrapermazol:100mg\"], \"allergies\":[\"nillacilan\"] }"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Boyd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthdate").value("1984-03-06"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medications[0]").value("aznol:350mg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medications[1]").value("hydrapermazol:100mg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allergies[0]").value("nillacilan"));
    }

    @Test
    void updateMedicalRecord_shouldReturnUpdatedMedicalRecord() throws Exception {
        when(medicalRecordService.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class))).thenReturn(medicalRecord);

        mockMvc.perform(MockMvcRequestBuilders.put("/medicalRecord/{firstName}/{lastName}", "John", "Boyd")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"birthdate\":\"1984-03-06\", \"medications\":[\"aznol:350mg\", \"hydrapermazol:100mg\"], \"allergies\":[\"nillacilan\"] }"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Boyd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthdate").value("1984-03-06"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medications[0]").value("aznol:350mg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medications[1]").value("hydrapermazol:100mg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allergies[0]").value("nillacilan"));
    }

    @Test
    void deleteMedicalRecord_shouldReturnNoContent() throws Exception {
        when(medicalRecordService.deleteMedicalRecord(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord/{firstName}/{lastName}", "John", "Boyd"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMedicalRecord_shouldReturnNotFound() throws Exception {
        when(medicalRecordService.deleteMedicalRecord(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord/{firstName}/{lastName}", "NonExistent", "Person"))
                .andExpect(status().isNotFound());
    }
}
