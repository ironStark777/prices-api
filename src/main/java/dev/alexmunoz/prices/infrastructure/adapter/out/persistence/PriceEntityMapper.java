package dev.alexmunoz.prices.infrastructure.adapter.out.persistence;

import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.Money;
import dev.alexmunoz.prices.domain.model.Price;
import dev.alexmunoz.prices.domain.model.ProductId;

import java.util.Currency;

final class PriceEntityMapper {

    private PriceEntityMapper() {
    }

    static Price toDomain(PriceEntity entity) {
        return new Price(
                new BrandId(entity.getBrandId()),
                new ProductId(entity.getProductId()),
                entity.getPriceList(),
                entity.getPriority(),
                entity.getStartDate(),
                entity.getEndDate(),
                new Money(entity.getPrice(), Currency.getInstance(entity.getCurrency().trim())));
    }
}
