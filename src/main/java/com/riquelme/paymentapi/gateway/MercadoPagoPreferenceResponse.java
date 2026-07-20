package com.riquelme.paymentapi.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MercadoPagoPreferenceResponse(

        String id,

        @JsonProperty("init_point")
        String initPoint,

        @JsonProperty("sandbox_init_point")
        String sandboxInitPoint
) {
}
