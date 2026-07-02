package com.utp.barberflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List; 

@Entity
@Table(name = "barberos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barbero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String especialidad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "barberos_horarios", joinColumns = @JoinColumn(name = "barbero_id"))
    @Column(name = "horario")
    private List<String> horarios;

    // --- NUEVA RELACIÓN AÑADIDA ---
    // Muchos barberos pueden pertenecer a una sola barbería
    @ManyToOne
    @JoinColumn(name = "barberia_id")
    private Barberia barberia;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;
 
    
}