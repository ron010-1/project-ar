package com.rampex.paymentapi.service;

import com.rampex.paymentapi.dto.PedidoRequestDTO;
import com.rampex.paymentapi.dto.PedidoResponseDTO;
import com.rampex.paymentapi.entity.Pedido;
import com.rampex.paymentapi.exception.ResourceNotFoundException;
import com.rampex.paymentapi.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public PedidoResponseDTO criar(PedidoRequestDTO request) {
        Pedido pedido = Pedido.builder()
                .descricao(request.descricao())
                .valor(request.valor())
                .build();
        return PedidoResponseDTO.from(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listar() {
        return pedidoRepository.findAll().stream()
                .map(PedidoResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        return PedidoResponseDTO.from(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    Pedido buscarEntidadePorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido nao encontrado: " + id));
    }
}
