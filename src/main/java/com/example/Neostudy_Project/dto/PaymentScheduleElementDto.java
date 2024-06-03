package com.example.Neostudy_Project.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "График выплат по кредиту")
public class PaymentScheduleElementDto {
    @Schema(description = "Номер выплаты")
    private Integer number;
    @Schema(description = "Дата выплаты")
    private LocalDate date;
    @Schema(description = "Полная сумма платежа")
    private BigDecimal totalPayment;
    @Schema(description = "Выплата процентов")
    private BigDecimal interestPayment;
    @Schema(description = "Выплата кредита")
    private BigDecimal debtPayment;
    @Schema(description = "Оставшаяся сумма")
    private BigDecimal remainingDebt;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getInterestPayment() {
        return interestPayment;
    }

    public void setInterestPayment(BigDecimal interestPayment) {
        this.interestPayment = interestPayment;
    }

    public BigDecimal getDebtPayment() {
        return debtPayment;
    }

    public void setDebtPayment(BigDecimal debtPayment) {
        this.debtPayment = debtPayment;
    }

    public BigDecimal getRemainingDebt() {
        return remainingDebt;
    }

    public void setRemainingDebt(BigDecimal remainingDebt) {
        this.remainingDebt = remainingDebt;
    }
}
