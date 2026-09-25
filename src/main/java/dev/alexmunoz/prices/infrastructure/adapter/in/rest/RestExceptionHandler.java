package dev.alexmunoz.prices.infrastructure.adapter.in.rest;

import dev.alexmunoz.prices.application.exception.PriceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Translates exceptions into RFC 9457 problem details. Request binding and validation
 * errors (400) are handled by the inherited Spring MVC handlers.
 */
@RestControllerAdvice
class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PriceNotFoundException.class)
    ProblemDetail handlePriceNotFound(PriceNotFoundException exception) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Price not found");
        return problem;
    }
}
