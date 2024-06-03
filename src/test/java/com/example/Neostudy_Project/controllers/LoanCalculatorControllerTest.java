package com.example.Neostudy_Project.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.example.Neostudy_Project.dto.*;
import com.example.Neostudy_Project.services.LoanCalculatorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LoanCalculatorControllerTest {

    @MockBean
    private LoanCalculatorService loanCalculationService;

    @InjectMocks
    private LoanCalculatorController loanCalculationController;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(loanCalculationController).build();
    }
    
    @Test
    void calculateOffers() throws Exception {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        requestDto.setAmount(new BigDecimal("150000"));
        requestDto.setTerm(18);
        requestDto.setFirstName("Минкин");
        requestDto.setLastName("Иван");
        requestDto.setMiddleName("Максимович");
        requestDto.setEmail("ivan@mail.ru");
        requestDto.setBirthdate(LocalDate.of(1992, 3, 3));
        requestDto.setPassportSeries("2002");
        requestDto.setPassportNumber("77853");

        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());
        offerDto.setRequestedAmount(new BigDecimal("50000"));
        offerDto.setTotalAmount(new BigDecimal("52000"));
        offerDto.setTerm(12);
        offerDto.setMonthlyPayment(new BigDecimal("6789.12"));
        offerDto.setRate(new BigDecimal("10"));
        offerDto.setInsuranceEnabled(true);
        offerDto.setSalaryClient(false);

        List<LoanOfferDto> offers = List.of(offerDto);

        when(loanCalculationService.calculatePossibleOffers(any(LoanStatementRequestDto.class))).thenReturn(offers);

        mockMvc.perform((RequestBuilder) post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit() throws Exception {
        ScoringDataDto scoringDataDto = new ScoringDataDto();
        scoringDataDto.setAmount(new BigDecimal("150000"));
        scoringDataDto.setTerm(12);
        scoringDataDto.setFirstName("Минкин");
        scoringDataDto.setLastName("Иван");
        scoringDataDto.setMiddleName("Максимович");
        scoringDataDto.setGender(Gender.MALE);
        scoringDataDto.setBirthdate(LocalDate.of(1992, 3,  3));
        scoringDataDto.setPassportSeries("2002");
        scoringDataDto.setPassportNumber("77853");
        scoringDataDto.setPassportIssueDate(LocalDate.of(2012, 1, 30));
        scoringDataDto.setPassportIssueBranch("ГУ МВД по Воронежской области");
        scoringDataDto.setMaritalStatus(MaritalStatus.SINGLE);
        scoringDataDto.setDependentAmount(0);
        scoringDataDto.setEmployment(new EmploymentDto());
        scoringDataDto.setAccountNumber("1");
        scoringDataDto.setInsuranceEnabled(true);
        scoringDataDto.setSalaryClient(true);

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(new BigDecimal("100000"));
        creditDto.setTerm(12);
        creditDto.setMonthlyPayment(new BigDecimal("6789.12"));
        creditDto.setRate(new BigDecimal("10"));
        creditDto.setPsk(new BigDecimal("52000"));
        creditDto.setInsuranceEnabled(true);
        creditDto.setSalaryClient(true);
        creditDto.setPaymentSchedule(List.of(new PaymentScheduleElementDto()));

        when(loanCalculationService.calculateCredit(any(ScoringDataDto.class))).thenReturn(creditDto);

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scoringDataDto)))
                .andExpect(status().isOk());
    }
}
