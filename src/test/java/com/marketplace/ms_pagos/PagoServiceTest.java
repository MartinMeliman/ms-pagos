package com.marketplace.ms_pagos;
import com.marketplace.ms_pagos.model.Pago;
import com.marketplace.ms_pagos.repository.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.marketplace.ms_pagos.service.PagoService;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para PagoService.
 * Patrón Given/When/Then con Mockito (sin BD real).
 */
@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock private PagoRepository pagoRepository;
    @InjectMocks private PagoService pagoService;

    private Pago pago;

    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setId(1L);
        pago.setPedidoId(10L);
        pago.setEstado("PENDIENTE");
    }

    @Test
    @DisplayName("obtenerTodos: debería retornar lista de pagos")
    void shouldReturnAllPagos() {
        // GIVEN
        when(pagoRepository.findAll()).thenReturn(List.of(pago));
        // WHEN
        List<Pago> resultado = pagoService.obtenerTodos();
        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("obtenerPorId: debería retornar el pago cuando existe")
    void shouldReturnPagoById() {
        // GIVEN
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        // WHEN
        Optional<Pago> resultado = pagoService.obtenerPorId(1L);
        // THEN
        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getPedidoId());
    }

    @Test
    @DisplayName("guardar: debería crear el pago cuando no existe pago para el pedido")
    void shouldSavePagoSuccessfully() {
        // GIVEN
        when(pagoRepository.findByPedidoId(10L)).thenReturn(Optional.empty());
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        // WHEN
        Pago resultado = pagoService.guardar(pago);
        // THEN
        assertNotNull(resultado);
        assertEquals(10L, resultado.getPedidoId());
        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    @DisplayName("guardar: debería lanzar excepción cuando ya existe pago para el pedido")
    void shouldThrowWhenPagoDuplicated() {
        // GIVEN — regla de negocio: un pago por pedido
        when(pagoRepository.findByPedidoId(10L)).thenReturn(Optional.of(pago));
        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> pagoService.guardar(pago));
        assertTrue(ex.getMessage().contains("Ya existe un pago"));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarEstado: debería actualizar el estado del pago")
    void shouldUpdateEstadoSuccessfully() {
        // GIVEN — pago en PENDIENTE se puede modificar
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        // WHEN
        Optional<Pago> resultado = pagoService.actualizarEstado(1L, "APROBADO");
        // THEN
        assertTrue(resultado.isPresent());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    @DisplayName("actualizarEstado: no debería modificar un pago ya APROBADO")
    void shouldThrowWhenModifyingApprovedPago() {
        // GIVEN — regla de negocio: pago APROBADO es inmutable
        Pago pagoAprobado = new Pago();
        pagoAprobado.setId(1L);
        pagoAprobado.setEstado("APROBADO");
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoAprobado));
        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> pagoService.actualizarEstado(1L, "RECHAZADO"));
        assertTrue(ex.getMessage().contains("APROBADO"));
        verify(pagoRepository, never()).save(any());
    }
}
