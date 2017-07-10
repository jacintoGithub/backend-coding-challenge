package com.engagetech.expenses.server.service;

import com.engagetech.expenses.server.domain.Expense;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PopulatorService {

    public Expense expense(){
        Expense expense = new Expense();
        expense.setAmount(5.75F);
        expense.setVat(6.9F);
        expense.setReason("Reason test");
        expense.setDate(Calendar.getInstance().getTimeInMillis());
        return expense;
    }
}
