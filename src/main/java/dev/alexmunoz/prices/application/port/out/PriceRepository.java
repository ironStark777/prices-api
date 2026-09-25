package dev.alexmunoz.prices.application.port.out;

import dev.alexmunoz.prices.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Outbound port: source of prices, implemented by the infrastructure layer.
 */
public interface PriceRepository {

    /**
     * Returns the candidate prices of a product for a brand whose validity period
     * contains the given date. The result may contain several overlapping prices.
     */
    List<Price> findApplicablePrices(long brandId, long productId, LocalDateTime applicationDate);
}
