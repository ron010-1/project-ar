package com.riquelme.paymentapi.dto;

import jakarta.validation.constraints.NotNull;

public record PagamentoRequestDTO(

        @NotNull(message = "pedidoId e obrigatorio")
        Long pedidoId
) {
}
