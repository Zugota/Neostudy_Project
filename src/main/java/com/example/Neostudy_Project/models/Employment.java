package com.example.Neostudy_Project.models;

import com.example.Neostudy_Project.dto.EmploymentStatus;
import com.example.Neostudy_Project.dto.Position;
import jakarta.persistence.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.UUID;

@Entity
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID employment_uuid;
    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;
    private String employment_inn;
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    private Position position;
    private Integer work_experience_total;
    private Integer work_experience_current;

}
