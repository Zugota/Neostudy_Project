package com.example.Neostudy_Project.converters;

import com.example.Neostudy_Project.models.Passport;
import com.example.Neostudy_Project.models.StatusHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;

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
