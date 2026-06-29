package com.marketplace.ms_pagos.service;
import com.marketplace.ms_pagos.model.Pago;
import com.marketplace.ms_pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
@Slf4j @Service @RequiredArgsConstructor
public class PagoService {
    private final PagoRepository pagoRepository;
    public List<Pago> obtenerTodos(){ return pagoRepository.findAll(); }
    public Optional<Pago> obtenerPorId(Long id){ return pagoRepository.findById(id); }
    public Optional<Pago> obtenerPorPedido(Long pid){ return pagoRepository.findByPedidoId(pid); }
    public Pago guardar(Pago p){
        if(pagoRepository.findByPedidoId(p.getPedidoId()).isPresent()) throw new RuntimeException("Ya existe un pago para el pedido ID: "+p.getPedidoId());
        log.info("Creando pago para pedido ID: {}", p.getPedidoId());
        return pagoRepository.save(p);
    }
    public Optional<Pago> actualizarEstado(Long id, String estado){
        return pagoRepository.findById(id).map(p->{
            if("APROBADO".equals(p.getEstado())) throw new RuntimeException("No se puede modificar un pago ya APROBADO");
            p.setEstado(estado); return pagoRepository.save(p);
        });
    }
}
