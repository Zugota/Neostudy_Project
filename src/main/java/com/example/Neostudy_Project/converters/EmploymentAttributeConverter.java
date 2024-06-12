package com.example.Neostudy_Project.converters;

import com.example.Neostudy_Project.deal.models.Employment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class EmploymentAttributeConverter implements AttributeConverter<Employment, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Employment employment) {
        try {
            return objectMapper.writeValueAsString(employment);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting Employment to JSON", e);
        }
    }

    @Override
    public Employment convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, Employment.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка конвертора", e);
        }
    }
}
