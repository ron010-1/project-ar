package com.rampex.paymentapi.controller;

import com.rampex.paymentapi.dto.PagamentoRequestDTO;
import com.rampex.paymentapi.dto.PagamentoResponseDTO;
import com.rampex.paymentapi.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@Tag(name = "Pagamentos", description = "Geracao e consulta de pagamentos via Mercado Pago")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    @Operation(summary = "Gerar cobranca no gateway para um pedido")
    public ResponseEntity<PagamentoResponseDTO> gerar(@Valid @RequestBody PagamentoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.gerarPagamento(request.pedidoId()));
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Consultar status atual de um pagamento no gateway")
    public ResponseEntity<PagamentoResponseDTO> consultarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.consultarStatus(id));
    }

    @GetMapping
    @Operation(summary = "Listar historico de pagamentos")
    public ResponseEntity<List<PagamentoResponseDTO>> listarHistorico() {
        return ResponseEntity.ok(pagamentoService.listarHistorico());
    }
}
