package dev.alexmunoz.prices.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MoneyTest {

    @Test
    void createsMoneyFromAmountAndIsoCode() {
        var money = Money.of("35.50", "EUR");

        assertThat(money.amount()).isEqualByComparingTo(new BigDecimal("35.50"));
        assertThat(money.currency()).isEqualTo(Currency.getInstance("EUR"));
    }

    @Test
    void rejectsNegativeAmounts() {
        assertThatIllegalArgumentException().isThrownBy(() -> Money.of("-1.00", "EUR"));
    }

    @Test
    void rejectsUnknownCurrencies() {
        assertThatIllegalArgumentException().isThrownBy(() -> Money.of("1.00", "XXXX"));
    }
}
