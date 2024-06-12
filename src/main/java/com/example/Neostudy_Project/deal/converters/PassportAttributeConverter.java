package com.example.Neostudy_Project.deal.converters;

import com.example.Neostudy_Project.deal.models.Passport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class PassportAttributeConverter implements AttributeConverter<Passport, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Passport passportData) {
        try {
            return objectMapper.writeValueAsString(passportData);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting Passport to JSON", e);
        }
    }

    @Override
    public Passport convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, Passport.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка конвертора", e);
        }
    }
}
