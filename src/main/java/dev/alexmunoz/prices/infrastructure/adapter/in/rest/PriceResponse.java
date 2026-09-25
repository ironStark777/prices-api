package dev.alexmunoz.prices.infrastructure.adapter.in.rest;

import dev.alexmunoz.prices.domain.model.Price;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "PriceResponse", description = "Final price applicable to a product of a brand")
public record PriceResponse(
        @Schema(example = "35455") long productId,
        @Schema(example = "1") long brandId,
        @Schema(description = "Price list (tariff) applied", example = "2") int priceList,
        @Schema(example = "2020-06-14T15:00:00") LocalDateTime startDate,
        @Schema(example = "2020-06-14T18:30:00") LocalDateTime endDate,
        @Schema(description = "Final selling price", example = "25.45") BigDecimal price,
        @Schema(description = "ISO-4217 currency code", example = "EUR") String currency) {

    static PriceResponse from(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.finalPrice().amount(),
                price.finalPrice().currency().getCurrencyCode());
    }
}
