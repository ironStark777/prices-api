package dev.alexmunoz.prices.application.port.out;

import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.Price;
import dev.alexmunoz.prices.domain.model.ProductId;

import java.util.List;

/**
 * Outbound port: source of prices, implemented by the infrastructure layer.
 */
public interface PriceRepository {

    /**
     * Returns every price list defined for a product of a brand, whatever its validity period.
     * Choosing the one that applies at a given date is a domain decision.
     */
    List<Price> findByBrandAndProduct(BrandId brandId, ProductId productId);
}
