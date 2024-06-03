package com.example.Neostudy_Project.controllers;

import com.example.Neostudy_Project.dto.CreditDto;
import com.example.Neostudy_Project.dto.LoanOfferDto;
import com.example.Neostudy_Project.dto.LoanStatementRequestDto;
import com.example.Neostudy_Project.dto.ScoringDataDto;
import com.example.Neostudy_Project.services.LoanCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/calculator")
@RestController
@Tag(name="Calculator Controller", description = "Контроллер отвечает за расчёт условий и параметров кредита")
public class LoanCalculatorController {
    @Autowired
    private LoanCalculatorService loanCalculatorService;

    @Operation(
            summary = "Расчёт возможных условий кредита"
    )
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> calculateOffers(@RequestBody @Validated LoanStatementRequestDto request) {
        List<LoanOfferDto> offers = loanCalculatorService.calculatePossibleOffers(request);
        return ResponseEntity.ok(offers);
    }

    @Operation(
            summary = "Валидация присланных данных + полный расчет параметров кредита"
    )
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(@RequestBody @Validated ScoringDataDto scoringData) {
        CreditDto creditDto = loanCalculatorService.calculateCredit(scoringData);
        return ResponseEntity.ok(creditDto);
    }
}
