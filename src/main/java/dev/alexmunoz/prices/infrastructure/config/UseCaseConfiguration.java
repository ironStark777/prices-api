package dev.alexmunoz.prices.infrastructure.config;

import dev.alexmunoz.prices.application.port.in.GetApplicablePriceUseCase;
import dev.alexmunoz.prices.application.service.GetApplicablePriceService;
import dev.alexmunoz.prices.domain.port.PriceRepository;
import dev.alexmunoz.prices.domain.service.PriceSelectionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the framework-agnostic domain and application classes as Spring beans.
 */
@Configuration
class UseCaseConfiguration {

    @Bean
    PriceSelectionPolicy priceSelectionPolicy() {
        return new PriceSelectionPolicy();
    }

    @Bean
    GetApplicablePriceUseCase getApplicablePriceUseCase(PriceRepository priceRepository,
                                                        PriceSelectionPolicy priceSelectionPolicy) {
        return new GetApplicablePriceService(priceRepository, priceSelectionPolicy);
    }
}
