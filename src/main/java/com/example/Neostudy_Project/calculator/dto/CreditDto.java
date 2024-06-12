package com.example.Neostudy_Project.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Сущность кредита")
public class CreditDto {
    @Schema(description = "Сумма кредита")
    private BigDecimal amount;
    @Schema(description = "Длительность взятия кредита")
    private Integer term;
    @Schema(description = "Ежемесячный платёж")
    private BigDecimal monthlyPayment;
    @Schema(description = "Ставка по кредиту")
    private BigDecimal rate;
    @Schema(description = "Полная стоимость кредита")
    private BigDecimal psk;
    @Schema(description = "Включена ли страховка в стоимость")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Работает ли клиент официально")
    private Boolean isSalaryClient;
    @Schema(description = "График выплат по кредиту")
    private List<PaymentScheduleElementDto> paymentSchedule;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public BigDecimal getPsk() {
        return psk;
    }

    public void setPsk(BigDecimal psk) {
        this.psk = psk;
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

    public List<PaymentScheduleElementDto> getPaymentSchedule() {
        return paymentSchedule;
    }

    public void setPaymentSchedule(List<PaymentScheduleElementDto> paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }
}
