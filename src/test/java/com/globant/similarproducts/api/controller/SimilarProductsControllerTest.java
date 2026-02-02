package com.globant.similarproducts.api.controller;

import com.globant.similarproducts.application.usecase.GetSimilarProductsUseCase;
import com.globant.similarproducts.domain.exception.ProductNotFoundException;
import com.globant.similarproducts.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimilarProductsController.class)
class SimilarProductsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GetSimilarProductsUseCase useCase;

    @Test
    void getSimilar_shouldReturn200() throws Exception {
        when(useCase.execute("1")).thenReturn(List.of(
                new Product("2","Dress", new BigDecimal("19.99"), true)
        ));

        mockMvc.perform(get("/product/1/similar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("2"));
    }

    @Test
    void getSimilar_shouldReturn404_whenNotFound() throws Exception {
        when(useCase.execute("999")).thenThrow(new ProductNotFoundException("999"));

        mockMvc.perform(get("/product/999/similar"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}