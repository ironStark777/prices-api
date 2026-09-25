package dev.alexmunoz.prices.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class IdentifierTest {

    @Test
    void identifiersWithTheSameValueAreEqual() {
        assertThat(new BrandId(1L)).isEqualTo(new BrandId(1L));
        assertThat(new ProductId(35455L)).isEqualTo(new ProductId(35455L));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void brandIdMustBePositive(long value) {
        assertThatIllegalArgumentException().isThrownBy(() -> new BrandId(value));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void productIdMustBePositive(long value) {
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductId(value));
    }
}
