package com.marketplace.ms_pagos.repository;
import com.marketplace.ms_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PagoRepository extends JpaRepository<Pago,Long> {
    Optional<Pago> findByPedidoId(Long pedidoId);
}
