package com.example.Neostudy_Project.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Сущность работника")
public class EmploymentDto {
    @Schema(description = "Статус работника")
    private EmploymentStatus employmentStatus; // Enum
    @Schema(description = "ИНН работника")
    private String employerINN;
    @Schema(description = "Заработная плата работника")
    private BigDecimal salary;
    @Schema(description = "Должность")
    private Position position; // Enum
    @Schema(description = "Полный опыт работы")
    private Integer workExperienceTotal;
    @Schema(description = "Опыт работы в текущей организации")
    private Integer workExperienceCurrent;

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getEmployerINN() {
        return employerINN;
    }

    public void setEmployerINN(String employerINN) {
        this.employerINN = employerINN;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Integer getWorkExperienceTotal() {
        return workExperienceTotal;
    }

    public void setWorkExperienceTotal(Integer workExperienceTotal) {
        this.workExperienceTotal = workExperienceTotal;
    }

    public Integer getWorkExperienceCurrent() {
        return workExperienceCurrent;
    }

    public void setWorkExperienceCurrent(Integer workExperienceCurrent) {
        this.workExperienceCurrent = workExperienceCurrent;
    }
}