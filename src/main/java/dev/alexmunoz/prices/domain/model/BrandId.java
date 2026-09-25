package dev.alexmunoz.prices.domain.model;

/**
 * Identifier of a brand (chain) of the group.
 */
public record BrandId(long value) {

    public BrandId {
        if (value <= 0) {
            throw new IllegalArgumentException("brandId must be positive");
        }
    }
}
