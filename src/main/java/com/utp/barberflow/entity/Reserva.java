package com.utp.barberflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false, length = 20)
    private String estado; // "PENDIENTE", "CONFIRMADA", "CANCELADA"

    // Relación: Muchas reservas pertenecen a un Usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación: Muchas reservas se hacen en una Barbería
    @ManyToOne
    @JoinColumn(name = "barberia_id", nullable = false)
    private Barberia barberia;
}