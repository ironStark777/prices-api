package dev.alexmunoz.prices.application.service;

import dev.alexmunoz.prices.application.port.in.GetApplicablePriceQuery;
import dev.alexmunoz.prices.application.port.in.GetApplicablePriceUseCase;
import dev.alexmunoz.prices.domain.exception.PriceNotFoundException;
import dev.alexmunoz.prices.domain.model.Price;
import dev.alexmunoz.prices.application.port.out.PriceRepository;
import dev.alexmunoz.prices.domain.service.PriceSelectionPolicy;

import java.util.Objects;

/**
 * Application service orchestrating the price lookup. Framework-agnostic: it is wired
 * as a bean by the infrastructure layer.
 */
public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepository priceRepository;
    private final PriceSelectionPolicy selectionPolicy;

    public GetApplicablePriceService(PriceRepository priceRepository, PriceSelectionPolicy selectionPolicy) {
        this.priceRepository = Objects.requireNonNull(priceRepository);
        this.selectionPolicy = Objects.requireNonNull(selectionPolicy);
    }

    @Override
    public Price getApplicablePrice(GetApplicablePriceQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        var candidates = priceRepository.findByBrandAndProduct(query.brandId(), query.productId());
        return selectionPolicy.select(candidates, query.applicationDate())
                .orElseThrow(() -> new PriceNotFoundException(
                        query.brandId(), query.productId(), query.applicationDate()));
    }
}
