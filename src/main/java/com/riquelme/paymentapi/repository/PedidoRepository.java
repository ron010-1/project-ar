package com.riquelme.paymentapi.repository;

import com.riquelme.paymentapi.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
