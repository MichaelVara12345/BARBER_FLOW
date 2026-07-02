package com.utp.barberflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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

    @Column(nullable = false, length = 50)
    private String hora; // <-- Cambiado a String para aceptar "Lunes - 9:00 am"

    @Column(nullable = false, length = 20)
    private String estado; 

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "barberia_id", nullable = false)
    private Barberia barberia;

    // --- NUEVO: Añadimos al barbero ---
    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;
}