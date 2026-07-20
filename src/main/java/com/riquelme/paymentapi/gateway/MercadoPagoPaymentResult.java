package com.riquelme.paymentapi.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoPagoPaymentResult(

        Long id,

        String status,

        @JsonProperty("transaction_amount")
        BigDecimal transactionAmount,

        @JsonProperty("date_created")
        String dateCreated,

        @JsonProperty("external_reference")
        String externalReference
) {
}
