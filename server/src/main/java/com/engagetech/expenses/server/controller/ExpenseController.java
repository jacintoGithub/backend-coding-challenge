package com.engagetech.expenses.server.controller;

import com.engagetech.expenses.server.domain.Expense;
import com.engagetech.expenses.server.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@CrossOrigin
@RestController
public class ExpenseController {

    public static final String PATH = "/expenses";

    @Autowired
    private ExpenseRepository expenseRepository;

    @RequestMapping(method = RequestMethod.GET, value = PATH)
    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(expenseRepository.findAll(pageable));
    }

    @RequestMapping(method = RequestMethod.POST, value = PATH)
    public ResponseEntity<?> create(@Valid @RequestBody Expense expense, BindingResult bindingResult, Locale locale) {
        return ResponseEntity.ok(expenseRepository.save(expense));
    }
}