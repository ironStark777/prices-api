package dev.alexmunoz.prices.infrastructure.adapter.in.rest;

import dev.alexmunoz.prices.domain.model.Price;

/**
 * Maps the domain {@link Price} to the REST representation, keeping the DTO free of domain types.
 */
final class PriceResponseMapper {

    private PriceResponseMapper() {
    }

    static PriceResponse toResponse(Price price) {
        return new PriceResponse(
                price.productId().value(),
                price.brandId().value(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.finalPrice().amount(),
                price.finalPrice().currency().getCurrencyCode());
    }
}
