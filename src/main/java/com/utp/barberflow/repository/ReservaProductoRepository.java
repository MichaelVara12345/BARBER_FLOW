package com.utp.barberflow.repository;

import com.utp.barberflow.entity.ReservaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaProductoRepository extends JpaRepository<ReservaProducto, Long> {
    // Busca todos los productos que pertenezcan a una cita específica
    List<ReservaProducto> findByReservaId(Long reservaId);
}