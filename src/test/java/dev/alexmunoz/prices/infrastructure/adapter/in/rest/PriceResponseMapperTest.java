package dev.alexmunoz.prices.infrastructure.adapter.in.rest;

import dev.alexmunoz.prices.domain.PriceMother;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PriceResponseMapperTest {

    @Test
    void mapsEveryFieldOfTheDomainPrice() {
        var response = PriceResponseMapper.toResponse(PriceMother.afternoonPromotion());

        assertThat(response).isEqualTo(new PriceResponse(
                35455L,
                1L,
                2,
                LocalDateTime.parse("2020-06-14T15:00:00"),
                LocalDateTime.parse("2020-06-14T18:30:00"),
                new BigDecimal("25.45"),
                "EUR"));
    }
}
