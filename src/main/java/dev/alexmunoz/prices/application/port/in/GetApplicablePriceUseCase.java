package dev.alexmunoz.prices.application.port.in;

import dev.alexmunoz.prices.application.exception.PriceNotFoundException;
import dev.alexmunoz.prices.domain.model.Price;

/**
 * Inbound port: resolves the final price of a product for a brand at a given date.
 */
public interface GetApplicablePriceUseCase {

    /**
     * @throws PriceNotFoundException when no price applies
     */
    Price getApplicablePrice(GetApplicablePriceQuery query);
}
