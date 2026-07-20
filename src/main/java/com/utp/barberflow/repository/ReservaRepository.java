package com.utp.barberflow.repository;

import com.utp.barberflow.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioId(Long usuarioId);
    List<Reserva> findByBarberoId(Long barberoId);

    List<Reserva> findByBarberoIdAndFecha(Long barberoId, java.time.LocalDate fecha);
}