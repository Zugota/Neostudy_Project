package com.example.Neostudy_Project.controllers;

import com.example.Neostudy_Project.dto.LoanOfferDto;
import com.example.Neostudy_Project.dto.LoanStatementRequestDto;
import com.example.Neostudy_Project.services.LoanCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/calculator")
public class LoanCalculatorController {
    @Autowired
    private LoanCalculatorService loanCalculatorService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> calculateOffers(@RequestBody @Validated LoanStatementRequestDto request) {
        List<LoanOfferDto> offers = loanCalculatorService.calculatePossibleOffers(request);
        return ResponseEntity.ok(offers);
    }


}
