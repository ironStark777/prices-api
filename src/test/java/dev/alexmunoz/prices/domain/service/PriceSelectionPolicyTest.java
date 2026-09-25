package dev.alexmunoz.prices.domain.service;

import dev.alexmunoz.prices.domain.PriceMother;
import dev.alexmunoz.prices.domain.model.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static dev.alexmunoz.prices.domain.PriceMother.price;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriceSelectionPolicyTest {

    private static final List<Price> SAMPLE_PRICES = List.of(
            PriceMother.basePrice(),
            PriceMother.afternoonPromotion(),
            PriceMother.morningPromotion(),
            PriceMother.seasonPrice());

    private final PriceSelectionPolicy policy = new PriceSelectionPolicy();

    @ParameterizedTest(name = "at {0} -> price list {1}")
    @CsvSource({
            "2020-06-14T10:00:00, 1",
            "2020-06-14T16:00:00, 2",
            "2020-06-14T21:00:00, 1",
            "2020-06-15T10:00:00, 3",
            "2020-06-16T21:00:00, 4"
    })
    @DisplayName("selects the applicable price with the highest priority")
    void selectsHighestPriorityAmongApplicablePrices(LocalDateTime applicationDate, int expectedPriceList) {
        assertThat(policy.select(SAMPLE_PRICES, applicationDate))
                .get()
                .extracting(Price::priceList)
                .isEqualTo(expectedPriceList);
    }

    @Test
    void resultDoesNotDependOnCandidateOrder() {
        var reversed = new ArrayList<>(SAMPLE_PRICES);
        Collections.reverse(reversed);
        var at = LocalDateTime.parse("2020-06-14T16:00:00");

        assertThat(policy.select(reversed, at)).isEqualTo(policy.select(SAMPLE_PRICES, at));
    }

    @Test
    void ignoresPricesOutsideTheirValidityPeriod() {
        var at = LocalDateTime.parse("2020-06-14T16:00:00");
        var expiredHighPriority = price(9, 99, "2020-01-01T00:00:00", "2020-01-31T23:59:59", "1.00");

        assertThat(policy.select(List.of(PriceMother.basePrice(), expiredHighPriority), at))
                .contains(PriceMother.basePrice());
    }

    @Test
    void breaksPriorityTiesWithTheMostRecentStartDate() {
        var at = LocalDateTime.parse("2020-06-20T12:00:00");
        var older = price(5, 1, "2020-06-01T00:00:00", "2020-06-30T23:59:59", "10.00");
        var newer = price(6, 1, "2020-06-10T00:00:00", "2020-06-30T23:59:59", "12.00");

        assertThat(policy.select(List.of(newer, older), at)).contains(newer);
        assertThat(policy.select(List.of(older, newer), at)).contains(newer);
    }

    @Test
    void returnsEmptyWhenNothingApplies() {
        var at = LocalDateTime.parse("2021-01-01T00:00:00");

        assertThat(policy.select(SAMPLE_PRICES, at)).isEmpty();
        assertThat(policy.select(List.of(), at)).isEmpty();
    }

    @Test
    void skipsNullCandidates() {
        var at = LocalDateTime.parse("2020-06-14T10:00:00");
        var candidates = Arrays.asList(null, PriceMother.basePrice(), null);

        assertThat(policy.select(candidates, at)).contains(PriceMother.basePrice());
    }

    @Test
    void rejectsNullArguments() {
        var at = LocalDateTime.parse("2020-06-14T10:00:00");

        assertThatNullPointerException().isThrownBy(() -> policy.select(null, at));
        assertThatNullPointerException().isThrownBy(() -> policy.select(SAMPLE_PRICES, null));
    }
}
