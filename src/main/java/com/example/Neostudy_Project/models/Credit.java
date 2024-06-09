package com.example.Neostudy_Project.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID credit_id;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthly_payment;
    private BigDecimal rate;
    private BigDecimal psk;
    @Column(columnDefinition = "jsonb")
    private String payment_schedule;
    private Boolean insurance_enabled;
    private Boolean salary_client;
    @Enumerated(EnumType.STRING)
    private CreditStatus credit_status;
}
