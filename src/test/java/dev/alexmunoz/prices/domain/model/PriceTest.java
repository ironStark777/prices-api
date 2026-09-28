package dev.alexmunoz.prices.domain.model;

import dev.alexmunoz.prices.domain.PriceMother;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriceTest {

    @Test
    void rejectsEndDateBeforeStartDate() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> PriceMother.price(1, 0, "2020-06-15T00:00:00", "2020-06-14T00:00:00", "10.00"));
    }

    @Test
    void rejectsMissingFinalPrice() {
        var start = LocalDateTime.parse("2020-06-14T00:00:00");
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(new BrandId(1L), new ProductId(1L), 1, 0, start, start, null));
    }
}
