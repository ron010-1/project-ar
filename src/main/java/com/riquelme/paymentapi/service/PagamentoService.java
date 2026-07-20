package com.riquelme.paymentapi.service;

import com.riquelme.paymentapi.dto.PagamentoResponseDTO;
import com.riquelme.paymentapi.entity.Pagamento;
import com.riquelme.paymentapi.entity.Pedido;
import com.riquelme.paymentapi.entity.StatusPagamento;
import com.riquelme.paymentapi.exception.PagamentoException;
import com.riquelme.paymentapi.exception.ResourceNotFoundException;
import com.riquelme.paymentapi.gateway.MercadoPagoClient;
import com.riquelme.paymentapi.gateway.MercadoPagoPaymentResult;
import com.riquelme.paymentapi.gateway.MercadoPagoPreferenceResponse;
import com.riquelme.paymentapi.repository.PagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoService pedidoService;
    private final MercadoPagoClient mercadoPagoClient;

    public PagamentoService(PagamentoRepository pagamentoRepository,
                             PedidoService pedidoService,
                             MercadoPagoClient mercadoPagoClient) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoService = pedidoService;
        this.mercadoPagoClient = mercadoPagoClient;
    }

    @Transactional
    public PagamentoResponseDTO gerarPagamento(Long pedidoId) {
        Pedido pedido = pedidoService.buscarEntidadePorId(pedidoId);

        boolean jaAprovado = pagamentoRepository.findByPedidoIdOrderByDataCriacaoDesc(pedidoId).stream()
                .anyMatch(p -> p.getStatus() == StatusPagamento.APROVADO);
        if (jaAprovado) {
            throw new PagamentoException("Pedido " + pedidoId + " ja possui pagamento aprovado");
        }

        MercadoPagoPreferenceResponse preferencia = mercadoPagoClient.criarPreferencia(
                pedido.getDescricao(), pedido.getValor(), pedidoId.toString());

        String checkoutUrl = preferencia.initPoint() != null ? preferencia.initPoint() : preferencia.sandboxInitPoint();

        Pagamento pagamento = Pagamento.builder()
                .pedido(pedido)
                .valor(pedido.getValor())
                .status(StatusPagamento.PENDENTE)
                .gatewayPreferenceId(preferencia.id())
                .checkoutUrl(checkoutUrl)
                .build();

        return PagamentoResponseDTO.from(pagamentoRepository.save(pagamento));
    }

    @Transactional
    public PagamentoResponseDTO consultarStatus(Long pagamentoId) {
        Pagamento pagamento = buscarEntidadePorId(pagamentoId);

        List<MercadoPagoPaymentResult> resultados = mercadoPagoClient
                .buscarPagamentosPorReferencia(pagamento.getPedido().getId().toString())
                .resultsOrEmpty();

        Optional<MercadoPagoPaymentResult> maisRecente = resultados.stream()
                .max(Comparator.comparing(r -> parseDataOuMinimo(r.dateCreated())));

        maisRecente.ifPresent(resultado -> {
            pagamento.setStatus(mapearStatus(resultado.status()));
            pagamento.setGatewayPaymentId(String.valueOf(resultado.id()));
            pagamento.setDataAtualizacao(Instant.now());
        });

        return PagamentoResponseDTO.from(pagamentoRepository.save(pagamento));
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarHistorico() {
        return pagamentoRepository.findAllByOrderByDataCriacaoDesc().stream()
                .map(PagamentoResponseDTO::from)
                .toList();
    }

    private Pagamento buscarEntidadePorId(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento nao encontrado: " + id));
    }

    private static Instant parseDataOuMinimo(String dateCreated) {
        if (dateCreated == null) {
            return Instant.MIN;
        }
        try {
            return OffsetDateTime.parse(dateCreated).toInstant();
        } catch (Exception ex) {
            return Instant.MIN;
        }
    }

    private static StatusPagamento mapearStatus(String statusMercadoPago) {
        if (statusMercadoPago == null) {
            return StatusPagamento.PENDENTE;
        }
        return switch (statusMercadoPago) {
            case "approved" -> StatusPagamento.APROVADO;
            case "rejected" -> StatusPagamento.RECUSADO;
            case "cancelled", "refunded", "charged_back" -> StatusPagamento.CANCELADO;
            default -> StatusPagamento.PENDENTE;
        };
    }
}
