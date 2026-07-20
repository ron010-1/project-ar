package com.riquelme.paymentapi.dto;

import com.riquelme.paymentapi.entity.Pedido;

import java.math.BigDecimal;
import java.time.Instant;

public record PedidoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        Instant dataCriacao
) {
    public static PedidoResponseDTO from(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDescricao(),
                pedido.getValor(),
                pedido.getDataCriacao()
        );
    }
}
