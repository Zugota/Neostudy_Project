package com.example.Neostudy_Project.deal.controllers;

import com.example.Neostudy_Project.calculator.dto.LoanOfferDto;
import com.example.Neostudy_Project.calculator.dto.LoanStatementRequestDto;
import com.example.Neostudy_Project.deal.dto.FinishRegistrationRequestDto;
import com.example.Neostudy_Project.deal.services.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/deal")
@Tag(name = "Deal Controller")
public class DealController {
    @Autowired
    private LoanService loanService;

    @PostMapping("/statement")
    @Operation(summary = "Calculate loan offers", description = "Calculates possible loan offers")
    public ResponseEntity<List<LoanOfferDto>> generateLoanOffers(@Validated @RequestBody LoanStatementRequestDto requestDto) {
        List<LoanOfferDto> offers = loanService.generateLoanOffers(requestDto);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/offer/select")
    @Operation(summary = "Select a loan offer", description = "Selects one of the loan offers")
    public ResponseEntity<Void> chooseLoanOffer(@Validated @RequestBody LoanOfferDto loanOfferDto) {
        loanService.chooseLoanOffer(loanOfferDto); //TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/calculate/{statementId}")
    @Operation(summary = "Finalize registration and calculate credit",
            description = "Finalizes the registration and calculates full credit parameters")
    public ResponseEntity<Void> finalizeCreditCalculation(@PathVariable String statementId,
                                                          @Validated @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {
        loanService.finalizeCreditCalculation(statementId, finishRegistrationRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
