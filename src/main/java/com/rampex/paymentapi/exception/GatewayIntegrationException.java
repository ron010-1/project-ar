package com.rampex.paymentapi.exception;

public class GatewayIntegrationException extends RuntimeException {

    public GatewayIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
