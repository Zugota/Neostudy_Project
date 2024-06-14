package com.example.Neostudy_Project.deal.services;

import com.example.Neostudy_Project.calculator.dto.CreditDto;
import com.example.Neostudy_Project.calculator.dto.LoanOfferDto;
import com.example.Neostudy_Project.calculator.dto.LoanStatementRequestDto;
import com.example.Neostudy_Project.calculator.dto.ScoringDataDto;
import com.example.Neostudy_Project.deal.dto.FinishRegistrationRequestDto;
import com.example.Neostudy_Project.deal.models.*;
import com.example.Neostudy_Project.deal.repo.ClientRepository;
import com.example.Neostudy_Project.deal.repo.StatementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoanService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CALCULATOR_SERVICE_URL = "http://localhost:8083/calculator";

    public List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto requestDto) {
        Client client = new Client();
        client.setFirst_name(requestDto.getFirstName());
        client.setLast_name(requestDto.getLastName());
        client.setMiddle_name(requestDto.getMiddleName());
        client.setBirth_date((toDate(requestDto.getBirthdate())));
        client.setEmail(requestDto.getEmail());
        clientRepository.save(client);

        Statement statement = new Statement();
        statement.setClient_id(client);
        statement.setCreation_date(Timestamp.valueOf(LocalDateTime.now()));
        statement.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        statementRepository.save(statement);

        List<LoanOfferDto> loanOffers = restTemplate.postForObject(
                CALCULATOR_SERVICE_URL + "/offers", requestDto, List.class
        );

        if (loanOffers != null) {
            loanOffers = loanOffers.stream()
                    .map(offer -> {
                        offer.setStatementId(statement.getStatement_id());
                        return offer;
                    })
                    .sorted((o1, o2) -> o2.getRate().compareTo(o1.getRate()))
                    .collect(Collectors.toList());
        }
        return loanOffers;
    }

    public void chooseLoanOffer(LoanOfferDto loanOfferDto) {
        Statement statement = statementRepository.findById(loanOfferDto.getStatementId())
                .orElseThrow(() -> new IllegalArgumentException("No record with this Id"));
        statement.setStatus(ApplicationStatus.PREAPPROVAL);
        statement.setApplied_offer(loanOfferDto.toString());
        statementRepository.save(statement);
    }

    public void finalizeCreditCalculation(String statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        Statement statement = statementRepository.findById(UUID.fromString(statementId))
                .orElseThrow(() -> new IllegalArgumentException("No record with this Id"));

        Client client = statement.getClient_id();

        ScoringDataDto scoringData = new ScoringDataDto();
        scoringData.setGender(finishRegistrationRequestDto.getGender());
        scoringData.setMaritalStatus(finishRegistrationRequestDto.getMaritalStatus());
        scoringData.setDependentAmount(finishRegistrationRequestDto.getDependentAmount());
        scoringData.setPassportIssueDate(finishRegistrationRequestDto.getPassportIssueDate());
        scoringData.setPassportIssueBranch(finishRegistrationRequestDto.getPassportIssueBranch());
        scoringData.setEmployment(finishRegistrationRequestDto.getEmployment());
        scoringData.setAccountNumber(finishRegistrationRequestDto.getAccountNumber());

        scoringData.setFirstName(client.getFirst_name());
        scoringData.setLastName(client.getLast_name());
        scoringData.setMiddleName(client.getMiddle_name());
        scoringData.setBirthdate(toDate(client.getBirth_date()));
        scoringData.setPassportSeries(client.getPassport_id().getSeries());
        scoringData.setPassportNumber(client.getPassport_id().getNumber());

        CreditDto creditDto = restTemplate.postForObject(
                CALCULATOR_SERVICE_URL + "/calc", scoringData, CreditDto.class
        );

        Credit credit = new Credit();
        if (creditDto != null) {
            credit.setAmount(creditDto.getAmount());
            credit.setTerm(creditDto.getTerm());
            credit.setMonthly_payment(creditDto.getMonthlyPayment());
            credit.setRate(creditDto.getRate());
            credit.setPsk(creditDto.getPsk());

            ObjectMapper objectMapper = new ObjectMapper();
            try{
                String paymentSchedule = objectMapper.writeValueAsString(creditDto.getPaymentSchedule());
                credit.setPayment_schedule(paymentSchedule);
            } catch (JsonProcessingException e){
                throw new RuntimeException("Error converting to JSON", e);
            }

            credit.setInsurance_enabled(creditDto.getInsuranceEnabled());
            credit.setSalary_client(creditDto.getSalaryClient());
            credit.setCredit_status(CreditStatus.CALCULATED);
        }

        statement.setCredit_id(credit);
        statement.setStatus(ApplicationStatus.CREDIT_ISSUED);
        statementRepository.save(statement);
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate toDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}