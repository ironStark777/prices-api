package dev.alexmunoz.prices.domain.service;

import dev.alexmunoz.prices.domain.PriceMother;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static dev.alexmunoz.prices.domain.PriceMother.price;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriceSelectionPolicyTest {

    private final PriceSelectionPolicy policy = new PriceSelectionPolicy();

    @Test
    void selectsTheHighestPriorityAmongOverlappingPrices() {
        var overlapping = List.of(PriceMother.basePrice(), PriceMother.afternoonPromotion());

        assertThat(policy.select(overlapping)).contains(PriceMother.afternoonPromotion());
    }

    @Test
    void selectsTheOnlyCandidate() {
        assertThat(policy.select(List.of(PriceMother.basePrice()))).contains(PriceMother.basePrice());
    }

    @Test
    void resultDoesNotDependOnCandidateOrder() {
        var candidates = List.of(PriceMother.basePrice(), PriceMother.afternoonPromotion());
        var reversed = new ArrayList<>(candidates);
        Collections.reverse(reversed);

        assertThat(policy.select(reversed)).isEqualTo(policy.select(candidates));
    }

    @Test
    void breaksPriorityTiesWithTheMostRecentStartDate() {
        var older = price(5, 1, "2020-06-01T00:00:00", "2020-06-30T23:59:59", "10.00");
        var newer = price(6, 1, "2020-06-10T00:00:00", "2020-06-30T23:59:59", "12.00");

        assertThat(policy.select(List.of(newer, older))).contains(newer);
        assertThat(policy.select(List.of(older, newer))).contains(newer);
    }

    @Test
    void breaksRemainingTiesWithTheHighestPriceList() {
        var lower = price(5, 1, "2020-06-10T00:00:00", "2020-06-30T23:59:59", "10.00");
        var higher = price(6, 1, "2020-06-10T00:00:00", "2020-06-30T23:59:59", "12.00");

        assertThat(policy.select(List.of(higher, lower))).contains(higher);
        assertThat(policy.select(List.of(lower, higher))).contains(higher);
    }

    @Test
    void returnsEmptyWhenThereAreNoCandidates() {
        assertThat(policy.select(List.of())).isEmpty();
    }

    @Test
    void skipsNullCandidates() {
        var candidates = Arrays.asList(null, PriceMother.basePrice(), null);

        assertThat(policy.select(candidates)).contains(PriceMother.basePrice());
    }

    @Test
    void rejectsNullCandidates() {
        assertThatNullPointerException().isThrownBy(() -> policy.select(null));
    }
}
