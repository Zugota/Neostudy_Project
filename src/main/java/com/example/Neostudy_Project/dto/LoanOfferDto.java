package com.example.Neostudy_Project.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Предложение по кредиту")
public class LoanOfferDto {
    @Schema(description = "Идентификатор")
    private UUID statementId;
    @Schema(description = "Запрашиваемая сумма")
    private BigDecimal requestedAmount;
    @Schema(description = "Итоговая сумма")
    private BigDecimal totalAmount;
    @Schema(description = "Срок взятия кредита")
    private Integer term;
    @Schema(description = "Ежемесячный платёж")
    private BigDecimal monthlyPayment;
    @Schema(description = "Ставка по кредиту")
    private BigDecimal rate;
    @Schema(description = "Включена ли страховка в стоимость")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Работает ли клиент официально")
    private Boolean isSalaryClient;

    public UUID getStatementId() {
        return statementId;
    }

    public void setStatementId(UUID statementId) {
        this.statementId = statementId;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Boolean getInsuranceEnabled() {
        return isInsuranceEnabled;
    }

    public void setInsuranceEnabled(Boolean insuranceEnabled) {
        isInsuranceEnabled = insuranceEnabled;
    }

    public Boolean getSalaryClient() {
        return isSalaryClient;
    }

    public void setSalaryClient(Boolean salaryClient) {
        isSalaryClient = salaryClient;
    }
}
