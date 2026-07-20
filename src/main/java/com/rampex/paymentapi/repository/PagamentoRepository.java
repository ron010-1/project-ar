package com.rampex.paymentapi.repository;

import com.rampex.paymentapi.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findAllByOrderByDataCriacaoDesc();

    List<Pagamento> findByPedidoIdOrderByDataCriacaoDesc(Long pedidoId);
}
