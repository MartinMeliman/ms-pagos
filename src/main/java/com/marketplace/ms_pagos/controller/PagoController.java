package com.marketplace.ms_pagos.controller;
import com.marketplace.ms_pagos.model.Pago;
import com.marketplace.ms_pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/pagos") @RequiredArgsConstructor
public class PagoController {
    private final PagoService pagoService;
    @GetMapping public List<Pago> obtenerTodos(){ return pagoService.obtenerTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id){ return pagoService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @GetMapping("/pedido/{pid}") public ResponseEntity<Pago> porPedido(@PathVariable Long pid){ return pagoService.obtenerPorPedido(pid).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @PostMapping public ResponseEntity<Pago> crear(@Valid @RequestBody Pago p){ return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.guardar(p)); }
    @PutMapping("/{id}/estado") public ResponseEntity<Pago> actualizarEstado(@PathVariable Long id, @RequestParam String estado){ return pagoService.actualizarEstado(id,estado).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
}
