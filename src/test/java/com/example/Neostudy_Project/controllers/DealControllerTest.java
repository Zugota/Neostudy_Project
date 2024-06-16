package com.example.Neostudy_Project.controllers;

import com.example.Neostudy_Project.calculator.dto.*;
import com.example.Neostudy_Project.deal.controllers.DealController;
import com.example.Neostudy_Project.deal.dto.FinishRegistrationRequestDto;
import com.example.Neostudy_Project.deal.services.LoanService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DealControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private DealController dealController;
    private LoanStatementRequestDto loanStatementRequestDto;
    private LoanOfferDto loanOfferDto;
    private FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeEach
    void setUp() {
        loanStatementRequestDto = new LoanStatementRequestDto();
        loanStatementRequestDto.setAmount(new BigDecimal("150000"));
        loanStatementRequestDto.setTerm(18);
        loanStatementRequestDto.setFirstName("Иван");
        loanStatementRequestDto.setLastName("Минкин");
        loanStatementRequestDto.setMiddleName("Максимович");
        loanStatementRequestDto.setEmail("ivan@mail.ru");
        loanStatementRequestDto.setBirthdate(LocalDate.of(1992, 3, 3));
        loanStatementRequestDto.setPassportSeries("2002");
        loanStatementRequestDto.setPassportNumber("77853");

        loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(UUID.randomUUID());
        loanOfferDto.setRequestedAmount(new BigDecimal("50000"));
        loanOfferDto.setTotalAmount(new BigDecimal("52000"));
        loanOfferDto.setTerm(12);
        loanOfferDto.setMonthlyPayment(new BigDecimal("6789.12"));
        loanOfferDto.setRate(new BigDecimal("10"));
        loanOfferDto.setInsuranceEnabled(true);
        loanOfferDto.setSalaryClient(false);

        finishRegistrationRequestDto = new FinishRegistrationRequestDto();
        finishRegistrationRequestDto.setGender(Gender.MALE);
        finishRegistrationRequestDto.setMaritalStatus(MaritalStatus.SINGLE);
        finishRegistrationRequestDto.setDependentAmount(0);
        finishRegistrationRequestDto.setPassportIssueDate(LocalDate.of(2012, 1, 30));
        finishRegistrationRequestDto.setPassportIssueBranch("ГУ МВД по Воронежской области");
        finishRegistrationRequestDto.setEmployment(new EmploymentDto());
        finishRegistrationRequestDto.setAccountNumber("1");


        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dealController).build();
    }

    @Test
    void testGenerateLoanOffers() throws Exception {
        List<LoanOfferDto> loanOffers = Arrays.asList(loanOfferDto);

        when(loanService.generateLoanOffers(any(LoanStatementRequestDto.class)))
                .thenReturn(loanOffers);

        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanStatementRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loanOffers)));
    }

    @Test
    void testSelectLoanOffer() throws Exception {
        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanOfferDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCalculateLoan() throws Exception {
        mockMvc.perform(post("/deal/calculate/{statementId}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishRegistrationRequestDto)))
                .andExpect(status().isOk());
    }

}
