package dev.alexmunoz.prices.infrastructure.adapter.in.rest;

import dev.alexmunoz.prices.application.port.in.GetApplicablePriceQuery;
import dev.alexmunoz.prices.application.port.in.GetApplicablePriceUseCase;
import dev.alexmunoz.prices.domain.PriceMother;
import dev.alexmunoz.prices.domain.exception.PriceNotFoundException;
import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
class PriceControllerTest {

    private static final String URL = "/api/v1/prices";
    private static final LocalDateTime AT = LocalDateTime.parse("2020-06-14T16:00:00");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetApplicablePriceUseCase useCase;

    @Test
    void mapsTheUseCaseResultToTheResponse() throws Exception {
        given(useCase.getApplicablePrice(new GetApplicablePriceQuery(new BrandId(1L), new ProductId(35455L), AT)))
                .willReturn(PriceMother.afternoonPromotion());

        mockMvc.perform(get(URL)
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"))
                .andExpect(jsonPath("$.price").value(25.45))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void returns404ProblemDetailWhenNoPriceApplies() throws Exception {
        given(useCase.getApplicablePrice(any()))
                .willThrow(new PriceNotFoundException(new BrandId(1L), new ProductId(35455L), AT));

        mockMvc.perform(get(URL)
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Price not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void returns400WhenAParameterIsMissing() throws Exception {
        mockMvc.perform(get(URL)
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"));

        then(useCase).should(never()).getApplicablePrice(any());
    }

    @Test
    void returns400WhenTheDateIsMalformed() throws Exception {
        mockMvc.perform(get(URL)
                        .param("applicationDate", "14/06/2020 16:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());

        then(useCase).should(never()).getApplicablePrice(any());
    }

    @Test
    void returns400WhenAnIdentifierIsNotPositive() throws Exception {
        mockMvc.perform(get(URL)
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "-1")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());

        then(useCase).should(never()).getApplicablePrice(any());
    }
}
