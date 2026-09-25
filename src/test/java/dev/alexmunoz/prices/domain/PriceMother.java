package dev.alexmunoz.prices.domain;

import dev.alexmunoz.prices.domain.model.Money;
import dev.alexmunoz.prices.domain.model.Price;

import java.time.LocalDateTime;

/**
 * Test data factory reproducing the sample PRICES table of the exercise.
 */
public final class PriceMother {

    public static final long BRAND_ID = 1L;
    public static final long PRODUCT_ID = 35455L;

    private PriceMother() {
    }

    public static Price basePrice() {
        return price(1, 0, "2020-06-14T00:00:00", "2020-12-31T23:59:59", "35.50");
    }

    public static Price afternoonPromotion() {
        return price(2, 1, "2020-06-14T15:00:00", "2020-06-14T18:30:00", "25.45");
    }

    public static Price morningPromotion() {
        return price(3, 1, "2020-06-15T00:00:00", "2020-06-15T11:00:00", "30.50");
    }

    public static Price seasonPrice() {
        return price(4, 1, "2020-06-15T16:00:00", "2020-12-31T23:59:59", "38.95");
    }

    public static Price price(int priceList, int priority, String start, String end, String amount) {
        return new Price(
                BRAND_ID,
                PRODUCT_ID,
                priceList,
                priority,
                LocalDateTime.parse(start),
                LocalDateTime.parse(end),
                Money.of(amount, "EUR"));
    }
}
