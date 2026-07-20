package com.rampex.paymentapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MercadoPagoProperties.class)
@OpenAPIDefinition(
        info = @Info(
                title = "Payment API",
                version = "1.0.0",
                description = "API de pedidos e pagamentos integrada ao Mercado Pago"
        )
)
public class OpenApiConfig {
}
