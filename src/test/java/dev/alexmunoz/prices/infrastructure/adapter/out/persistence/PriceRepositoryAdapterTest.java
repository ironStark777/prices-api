package dev.alexmunoz.prices.infrastructure.adapter.out.persistence;

import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.Price;
import dev.alexmunoz.prices.domain.model.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PriceRepositoryAdapter.class)
class PriceRepositoryAdapterTest {

    private static final BrandId BRAND = new BrandId(1L);
    private static final ProductId PRODUCT = new ProductId(35455L);
    private static final LocalDateTime DURING_SAMPLE = LocalDateTime.parse("2020-06-15T10:00:00");

    @Autowired
    private PriceRepositoryAdapter adapter;

    @ParameterizedTest(name = "at {0} -> price lists {1}")
    @CsvSource(delimiter = '|', value = {
            "2020-06-13T23:59:59 | ",
            "2020-06-14T00:00:00 | 1",
            "2020-06-14T14:59:59 | 1",
            "2020-06-14T15:00:00 | 1 2",
            "2020-06-14T16:00:00 | 1 2",
            "2020-06-14T18:30:00 | 1 2",
            "2020-06-14T18:30:01 | 1",
            "2020-06-15T11:00:00 | 1 3",
            "2020-06-15T11:00:01 | 1",
            "2020-06-16T21:00:00 | 1 4",
            "2020-12-31T23:59:59 | 1 4",
            "2021-01-01T00:00:00 | "
    })
    @DisplayName("returns only the price lists valid at the date, with inclusive bounds")
    void returnsOnlyThePriceListsValidAtTheDate(LocalDateTime applicationDate, String expectedPriceLists) {
        var expected = expectedPriceLists == null
                ? new Integer[0]
                : Arrays.stream(expectedPriceLists.trim().split(" ")).map(Integer::valueOf).toArray(Integer[]::new);

        assertThat(adapter.findApplicable(BRAND, PRODUCT, applicationDate))
                .extracting(Price::priceList)
                .containsExactlyInAnyOrder(expected);
    }

    @Test
    void mapsEveryColumnToTheDomainModel() {
        assertThat(adapter.findApplicable(BRAND, PRODUCT, LocalDateTime.parse("2020-06-15T10:00:00")))
                .filteredOn(price -> price.priceList() == 3)
                .singleElement()
                .satisfies(price -> {
                    assertThat(price.brandId()).isEqualTo(BRAND);
                    assertThat(price.productId()).isEqualTo(PRODUCT);
                    assertThat(price.priority()).isEqualTo(1);
                    assertThat(price.startDate()).isEqualTo(LocalDateTime.parse("2020-06-15T00:00:00"));
                    assertThat(price.endDate()).isEqualTo(LocalDateTime.parse("2020-06-15T11:00:00"));
                    assertThat(price.finalPrice().amount()).isEqualByComparingTo(new BigDecimal("30.50"));
                    assertThat(price.finalPrice().currency().getCurrencyCode()).isEqualTo("EUR");
                });
    }

    @Test
    void returnsNothingForUnknownProductOrBrand() {
        assertThat(adapter.findApplicable(BRAND, new ProductId(99999L), DURING_SAMPLE)).isEmpty();
        assertThat(adapter.findApplicable(new BrandId(2L), PRODUCT, DURING_SAMPLE)).isEmpty();
    }
}
