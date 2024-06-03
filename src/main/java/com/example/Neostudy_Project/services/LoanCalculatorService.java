package com.example.Neostudy_Project.services;

import com.example.Neostudy_Project.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class LoanCalculatorService {

    private static final BigDecimal BASE_RATE = new BigDecimal("15.5");
    private static final BigDecimal INSURANCE_DISCOUNT = new BigDecimal("2.5");
    private static final BigDecimal CLIENT_HAS_A_JOB_DISCOUNT = new BigDecimal("1.0");
    private static final BigDecimal INSURANCE_COST = new BigDecimal("130000");

    public List<LoanOfferDto> calculatePossibleOffers(LoanStatementRequestDto requestDto) {
        validatePreScoring(requestDto);
        List<LoanOfferDto> loanOffers = new ArrayList<>();

        boolean[] vals = {false, true};

        for (boolean isInsuranceEnabled : vals) {
            for (boolean isSalaryClient : vals) {
                LoanOfferDto loanOffer = new LoanOfferDto();
                loanOffer.setStatementId(UUID.randomUUID());
                loanOffer.setRequestedAmount(requestDto.getAmount());
                loanOffer.setTerm(requestDto.getTerm());

                BigDecimal clientRate = BASE_RATE;
                BigDecimal totalAmount = requestDto.getAmount();

                if (isInsuranceEnabled) {
                    totalAmount = totalAmount.add(INSURANCE_DISCOUNT);
                    clientRate = clientRate.subtract(INSURANCE_COST);
                }

                if (isSalaryClient) {
                    clientRate = clientRate.subtract(CLIENT_HAS_A_JOB_DISCOUNT);
                }

                BigDecimal monthPayment = calculateMonthPayment(totalAmount, clientRate, loanOffer.getTerm());

                loanOffer.setTotalAmount(totalAmount);
                loanOffer.setMonthlyPayment(monthPayment);
                loanOffer.setRate(clientRate);
                loanOffer.setInsuranceEnabled(isInsuranceEnabled);
                loanOffer.setSalaryClient(isSalaryClient);
                loanOffers.add(loanOffer);
            }
        }
        loanOffers.sort(Comparator.comparing(LoanOfferDto::getStatementId).reversed());
        return loanOffers;
    }

    private BigDecimal calculateMonthPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthRate = rate.divide(new BigDecimal("12"), BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP);
        BigDecimal monthPayment = amount.multiply(monthRate).divide(BigDecimal.ONE.subtract((BigDecimal.ONE.add(monthRate)).pow(-1 * term)), BigDecimal.ROUND_HALF_UP);
        return monthPayment;
    }

    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        validateScoring(scoringDataDto);

        CreditDto creditDto = new CreditDto();

        BigDecimal rate = BASE_RATE;
        BigDecimal totalAmount = scoringDataDto.getAmount();

        if (scoringDataDto.getInsuranceEnabled()) {
            totalAmount = totalAmount.add(INSURANCE_DISCOUNT);
            rate = rate.subtract(INSURANCE_COST);
        }

        if (scoringDataDto.getSalaryClient()) {
            rate = rate.subtract(CLIENT_HAS_A_JOB_DISCOUNT);
        }

        rate = calculateClientRate(scoringDataDto, rate);

        BigDecimal monthPayment = calculateMonthPayment(totalAmount, rate, scoringDataDto.getTerm());

        //Расчёт полной стоимости кредита
        BigDecimal psk = totalAmount.multiply(rate).divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(scoringDataDto.getTerm())).divide(new BigDecimal("12"), BigDecimal.ROUND_HALF_UP);

        creditDto.setAmount(scoringDataDto.getAmount());
        creditDto.setTerm(scoringDataDto.getTerm());
        creditDto.setMonthlyPayment(monthPayment);
        creditDto.setRate(rate);
        creditDto.setPsk(psk);
        creditDto.setInsuranceEnabled(scoringDataDto.getInsuranceEnabled());
        creditDto.setSalaryClient(scoringDataDto.getSalaryClient());
        creditDto.setPaymentSchedule(createPaymentSchedule(totalAmount, rate, scoringDataDto.getTerm(), monthPayment));

        return creditDto;
    }

    private List<PaymentScheduleElementDto> createPaymentSchedule(BigDecimal totalAmount, BigDecimal rate, Integer term, BigDecimal monthPayment) {
        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        BigDecimal monthlyPayment = monthPayment;
        BigDecimal remainingDebt = totalAmount;

        for (int i = 1; i <= term; i++) {
            PaymentScheduleElementDto elem = new PaymentScheduleElementDto();
            elem.setNumber(i);
            elem.setDate(LocalDate.now().plusDays(i));
            BigDecimal interestPayment = remainingDebt.multiply(rate).divide(new BigDecimal("12"), BigDecimal.ROUND_HALF_UP)
                    .divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            elem.setTotalPayment(monthlyPayment);
            elem.setInterestPayment(interestPayment);
            elem.setDebtPayment(debtPayment);
            elem.setRemainingDebt(remainingDebt);

            paymentSchedule.add(elem);
        }

        return paymentSchedule;
    }

    private BigDecimal calculateClientRate(ScoringDataDto scoringDataDto, BigDecimal clientRate) {
        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case SELF_EMPLOYED -> clientRate = clientRate.add(new BigDecimal("1.0"));
            case BUSINESS_OWNER -> clientRate = clientRate.add(new BigDecimal("2.0"));
        }

        switch (scoringDataDto.getEmployment().getPosition()) {
            case MIDDLE_MANAGER -> clientRate = clientRate.subtract(new BigDecimal("2.0"));
            case TOP_MANAGER -> clientRate = clientRate.subtract(new BigDecimal("3.0"));
        }

        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED -> clientRate = clientRate.subtract(new BigDecimal("3.0"));
            case DIVORCED -> clientRate = clientRate.add(new BigDecimal("1.0"));
        }

        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        switch (scoringDataDto.getGender()) {
            case FEMALE -> {
                if (age >= 32 && age <= 60) clientRate = clientRate.subtract(new BigDecimal("3.0"));
            }
            case MALE -> {
                if (age >= 30 && age <= 55) clientRate = clientRate.subtract(new BigDecimal("3.0"));
            }
            case NON_BINARY -> clientRate = clientRate.add(new BigDecimal("7.0"));
        }
        return clientRate;
    }

    private void validateScoring(ScoringDataDto scoringDataDto) {
        if (scoringDataDto.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED)
            throw new IllegalArgumentException("Employment is unemployed");
        else if (scoringDataDto.getEmployment().getSalary().multiply(new BigDecimal("25")).compareTo(scoringDataDto.getAmount()) < 0)
            throw new IllegalArgumentException("Too large amount of loan");
        else if (Period.between(LocalDate.now(), scoringDataDto.getBirthdate()).getYears() < 20 ||
                Period.between(LocalDate.now(), scoringDataDto.getBirthdate()).getYears() > 65)
            throw new IllegalArgumentException("Not appropriate age");
        else if (scoringDataDto.getEmployment().getWorkExperienceTotal() < 18 || scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3)
            throw new IllegalArgumentException("Not enough work experience");
    }

    private void validatePreScoring(LoanStatementRequestDto requestDto) {
        if (requestDto.getFirstName().length() < 2 || requestDto.getFirstName().length() > 30)
            throw new IllegalArgumentException("First name must be between 2 and 30 characters");
        else if (requestDto.getLastName().length() < 2 || requestDto.getLastName().length() > 30)
            throw new IllegalArgumentException("Last name must be between 2 and 30 characters");
        else if (requestDto.getMiddleName() != null && requestDto.getMiddleName().length() < 2 || requestDto.getMiddleName().length() > 30)
            throw new IllegalArgumentException("Middle name must be between 2 and 30 characters");
        else if (requestDto.getAmount().compareTo(new BigDecimal("30000")) < 0)
            throw new IllegalArgumentException("Amount must be greater than or equal to 30000");
        else if (requestDto.getTerm() < 6)
            throw new IllegalArgumentException("Term must be greater than 6");
        else if (Period.between(requestDto.getBirthdate(), LocalDate.now()).getYears() < 18)
            throw new IllegalArgumentException("Total years must be greater than 18 years");
        else if (!requestDto.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))
            throw new IllegalArgumentException("Invalid email address");
        else if (!requestDto.getPassportSeries().matches("^\\d{4}$"))
            throw new IllegalArgumentException("Invalid passport series number (You should write 4 numbers)");
        else if (!requestDto.getPassportNumber().matches("^\\d{6}$"))
            throw new IllegalArgumentException("Invalid passport number (You should write 6 numbers)");
    }
}