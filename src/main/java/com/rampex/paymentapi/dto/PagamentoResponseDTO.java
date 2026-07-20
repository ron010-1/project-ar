package com.rampex.paymentapi.dto;

import com.rampex.paymentapi.entity.Pagamento;
import com.rampex.paymentapi.entity.StatusPagamento;

import java.math.BigDecimal;
import java.time.Instant;

public record PagamentoResponseDTO(
        Long id,
        Long pedidoId,
        BigDecimal valor,
        StatusPagamento status,
        String gatewayPreferenceId,
        String gatewayPaymentId,
        String checkoutUrl,
        Instant dataCriacao,
        Instant dataAtualizacao
) {
    public static PagamentoResponseDTO from(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getPedido().getId(),
                pagamento.getValor(),
                pagamento.getStatus(),
                pagamento.getGatewayPreferenceId(),
                pagamento.getGatewayPaymentId(),
                pagamento.getCheckoutUrl(),
                pagamento.getDataCriacao(),
                pagamento.getDataAtualizacao()
        );
    }
}
