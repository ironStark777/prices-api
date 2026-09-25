package dev.alexmunoz.prices.domain.exception;

import java.time.LocalDateTime;

/**
 * Raised when no price applies to a product of a brand at the requested date.
 */
public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(long brandId, long productId, LocalDateTime applicationDate) {
        super("No applicable price found for brandId=%d, productId=%d at %s"
                .formatted(brandId, productId, applicationDate));
    }
}
