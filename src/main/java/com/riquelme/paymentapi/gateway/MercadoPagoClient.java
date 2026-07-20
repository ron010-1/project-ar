package com.riquelme.paymentapi.gateway;

import com.riquelme.paymentapi.config.MercadoPagoProperties;
import com.riquelme.paymentapi.exception.GatewayIntegrationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class MercadoPagoClient {

    private final RestClient restClient;

    public MercadoPagoClient(MercadoPagoProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.accessToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public MercadoPagoPreferenceResponse criarPreferencia(String titulo, BigDecimal valor, String externalReference) {
        Map<String, Object> item = Map.of(
                "title", titulo,
                "quantity", 1,
                "unit_price", valor,
                "currency_id", "BRL"
        );
        Map<String, Object> body = Map.of(
                "items", List.of(item),
                "external_reference", externalReference
        );
        try {
            return restClient.post()
                    .uri("/checkout/preferences")
                    .body(body)
                    .retrieve()
                    .body(MercadoPagoPreferenceResponse.class);
        } catch (RestClientException ex) {
            throw new GatewayIntegrationException("Falha ao criar preferencia de pagamento no Mercado Pago", ex);
        }
    }

    public MercadoPagoPaymentSearchResponse buscarPagamentosPorReferencia(String externalReference) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/v1/payments/search")
                            .queryParam("external_reference", externalReference)
                            .build())
                    .retrieve()
                    .body(MercadoPagoPaymentSearchResponse.class);
        } catch (RestClientException ex) {
            throw new GatewayIntegrationException("Falha ao consultar pagamentos no Mercado Pago", ex);
        }
    }
}
