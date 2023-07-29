package com.openclassrooms.SafetyNetAlerts.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.util.List;
import java.util.Map;

@Repository
public class SafetyRepository {
	
    public  Map<String, List<Map<String, Object>>> loadData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();  
            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
                    new File("./src/main/resources/data.json"),
                    new TypeReference<Map<String, List<Map<String, Object>>>>() {});
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        return null;
        }
       
    }
}
