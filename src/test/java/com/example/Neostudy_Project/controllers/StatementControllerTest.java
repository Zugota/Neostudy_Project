package com.example.Neostudy_Project.controllers;

import com.example.Neostudy_Project.calculator.dto.LoanOfferDto;
import com.example.Neostudy_Project.calculator.dto.LoanStatementRequestDto;
import com.example.Neostudy_Project.statement.controllers.StatementController;
import com.example.Neostudy_Project.statement.service.StatementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebMvcTest(StatementController.class)
public class StatementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatementService statementService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoanStatementRequestDto loanStatementRequestDto;
    private LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        loanStatementRequestDto = new LoanStatementRequestDto();
        loanStatementRequestDto.setAmount(new BigDecimal("10000"));
        loanStatementRequestDto.setTerm(12);
        loanStatementRequestDto.setFirstName("Иван");
        loanStatementRequestDto.setLastName("Минкин");
        loanStatementRequestDto.setEmail("john.doe@example.com");
        loanStatementRequestDto.setBirthdate(LocalDate.of(1992, 3, 3));
        loanStatementRequestDto.setPassportSeries("1234");
        loanStatementRequestDto.setPassportNumber("567890");

        loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(UUID.randomUUID());
        loanOfferDto.setRequestedAmount(new BigDecimal("10000"));
        loanOfferDto.setTotalAmount(new BigDecimal("11000"));
        loanOfferDto.setTerm(12);
        loanOfferDto.setMonthlyPayment(new BigDecimal("916.67"));
        loanOfferDto.setRate(new BigDecimal("10"));
        loanOfferDto.setInsuranceEnabled(true);
        loanOfferDto.setSalaryClient(false);
    }

    @Test
    void testGetLoanOffers() throws Exception {
        List<LoanOfferDto> loanOffers = Arrays.asList(loanOfferDto);

        Mockito.when(statementService.generateLoanOffers(Mockito.any(LoanStatementRequestDto.class)))
                .thenReturn(loanOffers);

        mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanStatementRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loanOffers)));
    }

    @Test
    void testSelectLoanOffer() throws Exception {
        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanOfferDto)))
                .andExpect(status().isOk());
    }
}