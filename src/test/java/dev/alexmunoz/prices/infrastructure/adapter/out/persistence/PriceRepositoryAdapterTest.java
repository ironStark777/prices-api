package dev.alexmunoz.prices.infrastructure.adapter.out.persistence;

import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.Price;
import dev.alexmunoz.prices.domain.model.ProductId;
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

    private static final BrandId BRAND = new BrandId(1L);
    private static final ProductId PRODUCT = new ProductId(35455L);

    @Autowired
    private PriceRepositoryAdapter adapter;

    @Test
    void returnsEveryPriceListOfTheProductForTheBrand() {
        assertThat(adapter.findByBrandAndProduct(BRAND, PRODUCT))
                .extracting(Price::priceList)
                .containsExactlyInAnyOrder(1, 2, 3, 4);
    }

    @Test
    void mapsEveryColumnToTheDomainModel() {
        assertThat(adapter.findByBrandAndProduct(BRAND, PRODUCT))
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
        assertThat(adapter.findByBrandAndProduct(BRAND, new ProductId(99999L))).isEmpty();
        assertThat(adapter.findByBrandAndProduct(new BrandId(2L), PRODUCT)).isEmpty();
    }
}
