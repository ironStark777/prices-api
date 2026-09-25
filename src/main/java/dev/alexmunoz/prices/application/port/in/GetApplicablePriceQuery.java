package dev.alexmunoz.prices.application.port.in;

import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.ProductId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Input of the {@link GetApplicablePriceUseCase}.
 */
public record GetApplicablePriceQuery(BrandId brandId, ProductId productId, LocalDateTime applicationDate) {

    public GetApplicablePriceQuery {
        Objects.requireNonNull(brandId, "brandId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(applicationDate, "applicationDate must not be null");
    }
}
