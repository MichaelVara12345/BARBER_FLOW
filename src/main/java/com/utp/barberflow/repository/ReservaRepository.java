package com.utp.barberflow.repository;

import com.utp.barberflow.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Este método nos devolverá la lista de citas de un usuario específico
    List<Reserva> findByUsuarioId(Long usuarioId);
}