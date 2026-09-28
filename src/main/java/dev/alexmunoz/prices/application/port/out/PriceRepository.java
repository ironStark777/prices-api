package dev.alexmunoz.prices.application.port.out;

import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.Price;
import dev.alexmunoz.prices.domain.model.ProductId;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Outbound port: source of prices, implemented by the infrastructure layer.
 */
public interface PriceRepository {

    /**
     * Returns the price lists of a product of a brand whose validity period contains the given
     * date (both bounds inclusive). Several may overlap; choosing among them is a domain decision.
     */
    List<Price> findApplicable(BrandId brandId, ProductId productId, LocalDateTime applicationDate);
}
