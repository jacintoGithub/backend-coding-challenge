package com.engagetech.expenses.server.controller;

import com.engagetech.expenses.server.domain.Expense;
import com.engagetech.expenses.server.error.HttpError;
import com.engagetech.expenses.server.service.PopulatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.constraints.DecimalMin;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test")
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PopulatorService populatorService;

    @Autowired
    private ExpenseController expenseController;

    @Test
    public void testEnglishValidationError() throws Exception {
        Expense expense = populatorService.expense();
        expense.setAmount(-5F);
        String response = mockMvc.perform(post(ExpenseController.PATH)
                                                  .contentType(MediaType.APPLICATION_JSON).locale(Locale.ENGLISH)
                                                  .content(objectMapper.writeValueAsString(expense)))
                                 .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        assertThat(objectMapper.readValue(response, HttpError.class).getMessage(),
                   equalTo(messageSource
                                   .getMessage(DecimalMin.class.getSimpleName(), new String[]{"amount", null, "0.01"},
                                               Locale.ENGLISH)));
    }

    @Test
    public void testSpanishValidationError() throws Exception {
        Expense expense = populatorService.expense();
        expense.setAmount(-5F);
        String response = mockMvc.perform(post(ExpenseController.PATH)
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .locale(Locale.forLanguageTag("es"))
                                                  .content(objectMapper.writeValueAsString(expense)))
                                 .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        assertThat(objectMapper.readValue(response, HttpError.class).getMessage(),
                   equalTo(messageSource
                                   .getMessage(DecimalMin.class.getSimpleName(), new String[]{"amount", null, "0.01"},
                                               Locale.forLanguageTag("es"))));
    }

    @Test
    public void testRest() throws Exception {
        mockMvc.perform(get(ExpenseController.PATH)).andExpect(status().isOk())
               .andExpect(jsonPath("$.totalElements", is(0)));
        mockMvc.perform(post(ExpenseController.PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(populatorService.expense())))
               .andExpect(status().isOk());
        mockMvc.perform(get(ExpenseController.PATH)).andExpect(status().isOk())
               .andExpect(jsonPath("$.totalElements", is(1)));
    }
}
