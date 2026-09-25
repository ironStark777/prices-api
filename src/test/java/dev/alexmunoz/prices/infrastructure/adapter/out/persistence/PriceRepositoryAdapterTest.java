package dev.alexmunoz.prices.infrastructure.adapter.out.persistence;

import dev.alexmunoz.prices.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PriceRepositoryAdapter.class)
class PriceRepositoryAdapterTest {

    @Autowired
    private PriceRepositoryAdapter adapter;

    @Test
    void returnsEveryPriceWhoseValidityPeriodContainsTheDate() {
        var prices = adapter.findApplicablePrices(1L, 35455L, LocalDateTime.parse("2020-06-14T16:00:00"));

        assertThat(prices)
                .extracting(Price::priceList)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void mapsEveryColumnToTheDomainModel() {
        var prices = adapter.findApplicablePrices(1L, 35455L, LocalDateTime.parse("2020-06-15T10:00:00"));

        assertThat(prices).filteredOn(price -> price.priceList() == 3)
                .singleElement()
                .satisfies(price -> {
                    assertThat(price.brandId()).isEqualTo(1L);
                    assertThat(price.productId()).isEqualTo(35455L);
                    assertThat(price.priority()).isEqualTo(1);
                    assertThat(price.startDate()).isEqualTo(LocalDateTime.parse("2020-06-15T00:00:00"));
                    assertThat(price.endDate()).isEqualTo(LocalDateTime.parse("2020-06-15T11:00:00"));
                    assertThat(price.finalPrice().amount()).isEqualByComparingTo(new BigDecimal("30.50"));
                    assertThat(price.finalPrice().currency().getCurrencyCode()).isEqualTo("EUR");
                });
    }

    @Test
    void includesPricesStartingOrEndingExactlyAtTheDate() {
        assertThat(adapter.findApplicablePrices(1L, 35455L, LocalDateTime.parse("2020-06-14T18:30:00")))
                .extracting(Price::priceList)
                .contains(2);
    }

    @Test
    void returnsNothingForUnknownProductOrBrand() {
        var at = LocalDateTime.parse("2020-06-14T10:00:00");

        assertThat(adapter.findApplicablePrices(1L, 99999L, at)).isEmpty();
        assertThat(adapter.findApplicablePrices(2L, 35455L, at)).isEmpty();
    }
}
