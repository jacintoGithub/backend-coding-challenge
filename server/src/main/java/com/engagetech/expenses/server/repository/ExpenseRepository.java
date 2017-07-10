package com.engagetech.expenses.server.repository;

import com.engagetech.expenses.server.domain.Expense;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, UUID> {
}
