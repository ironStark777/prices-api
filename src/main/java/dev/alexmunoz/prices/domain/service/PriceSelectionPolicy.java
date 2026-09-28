package dev.alexmunoz.prices.domain.service;

import dev.alexmunoz.prices.domain.model.Price;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * Domain rule that decides which price applies when several of them overlap. The candidates are
 * already valid at the application date: the outbound port filters by validity period.
 *
 * <ol>
 *     <li>The highest {@code priority} wins.</li>
 *     <li>On a priority tie case, the most recently started price wins, then the highest price list,
 *     so the result is always deterministic.</li>
 * </ol>
 */
public final class PriceSelectionPolicy {

    private static final Comparator<Price> PRECEDENCE = Comparator
            .comparingInt(Price::priority)
            .thenComparing(Price::startDate)
            .thenComparingInt(Price::priceList);

    public Optional<Price> select(Collection<Price> candidates) {
        Objects.requireNonNull(candidates, "candidates must not be null");
        return candidates.stream()
                .filter(Objects::nonNull)
                .max(PRECEDENCE);
    }
}
