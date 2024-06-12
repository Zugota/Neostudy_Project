package com.example.Neostudy_Project.deal.services;

import com.example.Neostudy_Project.calculator.dto.LoanOfferDto;
import com.example.Neostudy_Project.calculator.dto.LoanStatementRequestDto;
import com.example.Neostudy_Project.deal.dto.FinishRegistrationRequestDto;
import com.example.Neostudy_Project.deal.models.ApplicationStatus;
import com.example.Neostudy_Project.deal.models.Client;
import com.example.Neostudy_Project.deal.models.Statement;
import com.example.Neostudy_Project.deal.repo.ClientRepository;
import com.example.Neostudy_Project.deal.repo.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    public void chooseLoanOffer(LoanOfferDto loanOfferDto) {

    }

    public void finalizeCreditCalculation(String statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
    }
}

