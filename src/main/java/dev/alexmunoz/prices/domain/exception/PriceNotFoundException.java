package dev.alexmunoz.prices.domain.exception;

import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.ProductId;

import java.time.LocalDateTime;

/**
 * Raised when no price applies to a product of a brand at the requested date.
 */
public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(BrandId brandId, ProductId productId, LocalDateTime applicationDate) {
        super("No applicable price found for brandId=%d, productId=%d at %s"
                .formatted(brandId.value(), productId.value(), applicationDate));
    }
}
