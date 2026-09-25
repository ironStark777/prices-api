package dev.alexmunoz.prices.infrastructure.adapter.out.persistence;

import dev.alexmunoz.prices.domain.model.BrandId;
import dev.alexmunoz.prices.domain.model.Price;
import dev.alexmunoz.prices.domain.model.ProductId;
import dev.alexmunoz.prices.application.port.out.PriceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA implementation of the {@link PriceRepository} outbound port.
 */
@Component
class PriceRepositoryAdapter implements PriceRepository {

    private final SpringDataPriceRepository repository;

    PriceRepositoryAdapter(SpringDataPriceRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Price> findApplicablePrices(BrandId brandId, ProductId productId, LocalDateTime applicationDate) {
        return repository.findApplicable(brandId.value(), productId.value(), applicationDate).stream()
                .map(PriceEntityMapper::toDomain)
                .toList();
    }
}
