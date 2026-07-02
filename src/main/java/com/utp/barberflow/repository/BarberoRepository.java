package com.utp.barberflow.repository;

import com.utp.barberflow.entity.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; 

public interface BarberoRepository extends JpaRepository<Barbero, Long> {
    // Spring Boot creará la consulta SQL automáticamente para filtrar por barbería
    List<Barbero> findByBarberiaId(Long barberiaId);
 Barbero findByUsuarioId(Long usuarioId);
}