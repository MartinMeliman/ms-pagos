package com.marketplace.ms_pagos.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Entity @Table(name="pagos")
public class Pago {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotNull @Column(name="pedido_id", nullable=false, unique=true) private Long pedidoId;
    @NotNull @Column(name="usuario_id", nullable=false) private Long usuarioId;
    @NotNull @DecimalMin("0.01") @Column(nullable=false, precision=10, scale=2) private BigDecimal monto;
    @NotBlank @Column(name="metodo_pago", nullable=false, length=50) private String metodoPago;
    // Estados: PENDIENTE, APROBADO, RECHAZADO
    @Column(nullable=false, length=30) private String estado="PENDIENTE";
    @Column(name="creado_en", updatable=false) private LocalDateTime creadoEn;
    @PrePersist public void pre(){ creadoEn=LocalDateTime.now(); }
}
