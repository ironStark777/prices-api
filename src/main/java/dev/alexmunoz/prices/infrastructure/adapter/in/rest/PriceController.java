package dev.alexmunoz.prices.infrastructure.adapter.in.rest;

import dev.alexmunoz.prices.application.port.in.GetApplicablePriceQuery;
import dev.alexmunoz.prices.application.port.in.GetApplicablePriceUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "Prices", description = "Applicable price lookup")
@RestController
@RequestMapping("/api/v1/prices")
class PriceController {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    PriceController(GetApplicablePriceUseCase getApplicablePriceUseCase) {
        this.getApplicablePriceUseCase = getApplicablePriceUseCase;
    }

    @Operation(summary = "Get the price that applies to a product of a brand at a given date")
    @ApiResponse(responseCode = "200", description = "Applicable price found")
    @ApiResponse(responseCode = "400", description = "Missing or invalid parameters",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "No price applies at that date",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping
    public PriceResponse getApplicablePrice(
            @Parameter(description = "Application date (ISO-8601, local time)", example = "2020-06-14T10:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @Parameter(description = "Product identifier", example = "35455")
            @RequestParam @Positive Long productId,
            @Parameter(description = "Brand identifier", example = "1")
            @RequestParam @Positive Long brandId) {
        var query = new GetApplicablePriceQuery(brandId, productId, applicationDate);
        return PriceResponse.from(getApplicablePriceUseCase.getApplicablePrice(query));
    }
}
