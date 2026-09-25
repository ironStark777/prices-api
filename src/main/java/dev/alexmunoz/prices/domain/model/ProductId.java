package dev.alexmunoz.prices.domain.model;

/**
 * Identifier of a product.
 */
public record ProductId(long value) {

    public ProductId {
        if (value <= 0) {
            throw new IllegalArgumentException("productId must be positive");
        }
    }
}
