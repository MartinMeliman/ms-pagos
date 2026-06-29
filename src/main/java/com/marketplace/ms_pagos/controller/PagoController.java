package com.marketplace.ms_pagos.controller;

import com.marketplace.ms_pagos.model.Pago;
import com.marketplace.ms_pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Pagos", description = "Procesamiento de pagos de pedidos del marketplace EcoTrade")
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @Operation(summary = "Listar todos los pagos",
               description = "Retorna la lista completa de pagos registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public List<Pago> obtenerTodos() {
        return pagoService.obtenerTodos();
    }

    @Operation(summary = "Obtener pago por ID",
               description = "Busca un pago por su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(
            @Parameter(description = "ID del pago") @PathVariable Long id) {
        return pagoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener pago por pedido",
               description = "Busca el pago asociado a un pedido específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago del pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe pago para ese pedido")
    })
    @GetMapping("/pedido/{pid}")
    public ResponseEntity<Pago> porPedido(
            @Parameter(description = "ID del pedido") @PathVariable Long pid) {
        return pagoService.obtenerPorPedido(pid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar nuevo pago",
               description = "Registra un pago para un pedido. Solo se permite un pago por pedido.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pago registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Ya existe un pago para ese pedido")
    })
    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody Pago p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.guardar(p));
    }

    @Operation(summary = "Actualizar estado del pago",
               description = "Actualiza el estado de un pago. Un pago APROBADO no puede modificarse.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "El pago ya está APROBADO y no puede modificarse"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(
            @Parameter(description = "ID del pago") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del pago") @RequestParam String estado) {
        return pagoService.actualizarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}