package com.example.Neostudy_Project.deal.models;

import com.example.Neostudy_Project.deal.converters.StatusHistoryAttributeConverter;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statement_id;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Client client_id;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Credit credit_id;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private Timestamp creation_date;
    @Column(columnDefinition = "jsonb")
    private String applied_offer;
    private Timestamp sign_date;
    private String ses_code;
    @Convert(converter = StatusHistoryAttributeConverter.class)
    @Column(columnDefinition = "jsonb")
    private StatusHistory status_history;

}