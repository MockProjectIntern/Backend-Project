package com.sapo.mock_project.inventory_receipt.converts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Converter(autoApply = true)
public class GRNImportCostConverter implements AttributeConverter<GRNImportCost, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(GRNImportCost attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert Inspection into JSON");
            return null;
        }
    }

    @Override
    public GRNImportCost convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, GRNImportCost.class);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into Inspection");
            return null;
        }
    }
}
