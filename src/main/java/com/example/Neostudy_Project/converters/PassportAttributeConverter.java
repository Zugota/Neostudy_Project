package com.example.Neostudy_Project.converters;

import com.example.Neostudy_Project.models.Passport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.SQLException;

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
