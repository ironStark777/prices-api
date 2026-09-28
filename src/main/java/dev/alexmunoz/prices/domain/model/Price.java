package dev.alexmunoz.prices.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A price list (tariff) applied to a product of a brand within a validity period.
 *
 * <p>When several prices overlap in time, the one with the highest {@code priority} wins.
 */
public record Price(
        BrandId brandId,
        ProductId productId,
        int priceList,
        int priority,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Money finalPrice) {

    public Price {
        Objects.requireNonNull(brandId, "brandId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(finalPrice, "finalPrice must not be null");
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
    }
}
