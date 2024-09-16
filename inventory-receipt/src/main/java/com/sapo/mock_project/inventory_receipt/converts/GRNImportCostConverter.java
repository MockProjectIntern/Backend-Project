package com.sapo.mock_project.inventory_receipt.converts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Converter(autoApply = true)
public class GRNImportCostConverter implements AttributeConverter<List<GRNImportCost>, String> {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String convertToDatabaseColumn(List<GRNImportCost> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert GRNImportCost into JSON");
            return null;
        }
    }

    @Override
    public List<GRNImportCost> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<GRNImportCost>>() {});
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into GRNImportCost");
            return null;
        }
    }
}
