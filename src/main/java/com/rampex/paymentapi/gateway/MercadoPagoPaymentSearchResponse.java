package com.rampex.paymentapi.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoPagoPaymentSearchResponse(List<MercadoPagoPaymentResult> results) {

    public List<MercadoPagoPaymentResult> resultsOrEmpty() {
        return results == null ? Collections.emptyList() : results;
    }
}
