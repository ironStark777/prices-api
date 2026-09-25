package dev.alexmunoz.prices.application.service;

import dev.alexmunoz.prices.application.port.in.GetApplicablePriceQuery;
import dev.alexmunoz.prices.domain.PriceMother;
import dev.alexmunoz.prices.domain.exception.PriceNotFoundException;
import dev.alexmunoz.prices.application.port.out.PriceRepository;
import dev.alexmunoz.prices.domain.service.PriceSelectionPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static dev.alexmunoz.prices.domain.PriceMother.BRAND_ID;
import static dev.alexmunoz.prices.domain.PriceMother.PRODUCT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GetApplicablePriceServiceTest {

    private static final LocalDateTime AT = LocalDateTime.parse("2020-06-14T16:00:00");

    @Mock
    private PriceRepository priceRepository;

    private GetApplicablePriceService service;

    @BeforeEach
    void setUp() {
        service = new GetApplicablePriceService(priceRepository, new PriceSelectionPolicy());
    }

    @Test
    void returnsTheHighestPriorityPriceAmongCandidates() {
        given(priceRepository.findApplicablePrices(BRAND_ID, PRODUCT_ID, AT))
                .willReturn(List.of(PriceMother.basePrice(), PriceMother.afternoonPromotion()));

        var price = service.getApplicablePrice(new GetApplicablePriceQuery(BRAND_ID, PRODUCT_ID, AT));

        assertThat(price).isEqualTo(PriceMother.afternoonPromotion());
        then(priceRepository).should().findApplicablePrices(BRAND_ID, PRODUCT_ID, AT);
    }

    @Test
    void throwsPriceNotFoundWhenThereAreNoCandidates() {
        given(priceRepository.findApplicablePrices(BRAND_ID, PRODUCT_ID, AT)).willReturn(List.of());
        var query = new GetApplicablePriceQuery(BRAND_ID, PRODUCT_ID, AT);

        assertThatThrownBy(() -> service.getApplicablePrice(query))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining("brandId=1")
                .hasMessageContaining("productId=35455");
    }

    @Test
    void rejectsNullQuery() {
        assertThatThrownBy(() -> service.getApplicablePrice(null))
                .isInstanceOf(NullPointerException.class);
    }
}
