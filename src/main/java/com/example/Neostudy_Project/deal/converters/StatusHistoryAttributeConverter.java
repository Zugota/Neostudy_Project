package com.example.Neostudy_Project.deal.converters;

import com.example.Neostudy_Project.deal.models.StatusHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class StatusHistoryAttributeConverter implements AttributeConverter<StatusHistory, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(StatusHistory statusHistory) {
        try {
            return objectMapper.writeValueAsString(statusHistory);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting StatusHistory to JSON", e);
        }
    }

    @Override
    public StatusHistory convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, StatusHistory.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка конвертора", e);
        }
    }
}