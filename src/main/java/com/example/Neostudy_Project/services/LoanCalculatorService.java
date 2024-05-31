package com.example.Neostudy_Project.services;

import com.example.Neostudy_Project.dto.LoanOfferDto;
import com.example.Neostudy_Project.dto.LoanStatementRequestDto;
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
