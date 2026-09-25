package dev.alexmunoz.prices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end tests of the REST endpoint against the H2 sample dataset,
 * covering the five scenarios required by the specification.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PricesApiAcceptanceTest {

    private static final long BRAND_ID = 1L;
    private static final long PRODUCT_ID = 35455L;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "{0}: request at {1} -> price list {2}, {5} EUR")
    @CsvSource({
            "Test 1, 2020-06-14T10:00:00, 1, 2020-06-14T00:00:00, 2020-12-31T23:59:59, 35.50",
            "Test 2, 2020-06-14T16:00:00, 2, 2020-06-14T15:00:00, 2020-06-14T18:30:00, 25.45",
            "Test 3, 2020-06-14T21:00:00, 1, 2020-06-14T00:00:00, 2020-12-31T23:59:59, 35.50",
            "Test 4, 2020-06-15T10:00:00, 3, 2020-06-15T00:00:00, 2020-06-15T11:00:00, 30.50",
            "Test 5, 2020-06-16T21:00:00, 4, 2020-06-15T16:00:00, 2020-12-31T23:59:59, 38.95"
    })
    @DisplayName("returns the applicable price for the required scenarios")
    void returnsTheApplicablePrice(String scenario, String applicationDate, int priceList,
                                   String startDate, String endDate, double price) throws Exception {
        requestPrice(applicationDate, PRODUCT_ID, BRAND_ID)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.brandId").value(BRAND_ID))
                .andExpect(jsonPath("$.priceList").value(priceList))
                .andExpect(jsonPath("$.startDate").value(startDate))
                .andExpect(jsonPath("$.endDate").value(endDate))
                .andExpect(jsonPath("$.price").value(price))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void returns404BeforeAnyPriceIsValid() throws Exception {
        requestPrice("2020-06-13T23:59:59", PRODUCT_ID, BRAND_ID)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void returns404ForAnUnknownProduct() throws Exception {
        requestPrice("2020-06-14T10:00:00", 1L, BRAND_ID)
                .andExpect(status().isNotFound());
    }

    private ResultActions requestPrice(String applicationDate, long productId, long brandId) throws Exception {
        return mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", applicationDate)
                .param("productId", String.valueOf(productId))
                .param("brandId", String.valueOf(brandId)));
    }
}
