package dev.alexmunoz.prices.application.port.in;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Input of the {@link GetApplicablePriceUseCase}.
 */
public record GetApplicablePriceQuery(long brandId, long productId, LocalDateTime applicationDate) {

    public GetApplicablePriceQuery {
        Objects.requireNonNull(applicationDate, "applicationDate must not be null");
    }
}
