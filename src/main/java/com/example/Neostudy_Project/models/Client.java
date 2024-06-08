package com.example.Neostudy_Project.models;

import com.example.Neostudy_Project.converters.PassportAttributeConverter;
import com.example.Neostudy_Project.dto.Gender;
import com.example.Neostudy_Project.dto.MaritalStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.UUID;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID client_id;
    private String last_name;
    private String first_name;
    private String middle_name;
    private Date birth_date;
    private String email;
    @Column(columnDefinition = "character varying")
    private Gender gender;
    @Column(columnDefinition = "character varying")
    private MaritalStatus marital_status;
    private Integer dependent_amount;
    @Convert(converter = PassportAttributeConverter.class)
    private Passport passport;

}
