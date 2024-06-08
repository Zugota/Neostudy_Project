package com.example.Neostudy_Project.converters;

import com.example.Neostudy_Project.models.Passport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

public class PassportAttributeConverter implements AttributeConverter<Passport, PGobject> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PGobject convertToDatabaseColumn(Passport passportData) {
        if (passportData == null) return null;
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(objectMapper.writeValueAsString(passportData));
            return pgObject;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException("Ошибка конвертора", e);
        }
    }

    @Override
    public Passport convertToEntityAttribute(PGobject obj) {
        try {
            return objectMapper.readValue(obj.getValue(), Passport.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка конвертора", e);
        }
    }
}
