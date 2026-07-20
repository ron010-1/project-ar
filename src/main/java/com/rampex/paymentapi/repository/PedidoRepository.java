package com.rampex.paymentapi.repository;

import com.rampex.paymentapi.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
