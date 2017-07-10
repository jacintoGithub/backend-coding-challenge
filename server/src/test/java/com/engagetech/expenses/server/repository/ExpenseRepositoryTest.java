package com.engagetech.expenses.server.repository;

import com.engagetech.expenses.server.domain.Expense;
import com.engagetech.expenses.server.service.PopulatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Calendar;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test")
public class ExpenseRepositoryTest {

    @Autowired
    private PopulatorService populatorService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    public void testCRUD() {
        Expense expense = populatorService.expense();
        expenseRepository.deleteAll();
        assertThat(expenseRepository.count(), equalTo(0L));
        expense = expenseRepository.save(expense);
        assertThat(expense.getVat(), equalTo(6.9F));
        assertThat(expenseRepository.count(), equalTo(1L));
        expenseRepository.delete(expense.getId());
        assertThat(expenseRepository.count(), equalTo(0L));
    }
}
