package dev.alexmunoz.prices.domain.model;

import dev.alexmunoz.prices.domain.PriceMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriceTest {

    private final Price promotion = PriceMother.afternoonPromotion(); // 2020-06-14 15:00 -> 18:30

    @ParameterizedTest(name = "{0} -> applicable={1}")
    @CsvSource({
            "2020-06-14T14:59:59, false",
            "2020-06-14T15:00:00, true",
            "2020-06-14T16:45:00, true",
            "2020-06-14T18:30:00, true",
            "2020-06-14T18:30:01, false"
    })
    @DisplayName("validity period bounds are inclusive")
    void isApplicableAt(LocalDateTime applicationDate, boolean expected) {
        assertThat(promotion.isApplicableAt(applicationDate)).isEqualTo(expected);
    }

    @Test
    void rejectsEndDateBeforeStartDate() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> PriceMother.price(1, 0, "2020-06-15T00:00:00", "2020-06-14T00:00:00", "10.00"));
    }

    @Test
    void rejectsMissingFinalPrice() {
        var start = LocalDateTime.parse("2020-06-14T00:00:00");
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(1L, 1L, 1, 0, start, start, null));
    }
}
