package com.example.Neostudy_Project.statement.controllers;

import com.example.Neostudy_Project.calculator.dto.LoanOfferDto;
import com.example.Neostudy_Project.calculator.dto.LoanStatementRequestDto;
import com.example.Neostudy_Project.statement.service.StatementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@Tag(name = "Statement Controller")
@RequestMapping("/statement")
public class StatementController {
    @Autowired
    StatementService statementService;

    @PostMapping
    public List<LoanOfferDto> getLoanOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return statementService.generateLoanOffers(loanStatementRequestDto);
    }

    @PostMapping("/offer")
    public void selectLoanOffer(@RequestBody LoanOfferDto loanOfferDto) {
        statementService.selectLoanOffer(loanOfferDto);
    }
}