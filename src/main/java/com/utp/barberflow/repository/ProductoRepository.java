package com.utp.barberflow.repository;

import com.utp.barberflow.entity.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Filtra los productos que pertenecen a una barbería específica
    List<Producto> findByBarberiaId(Long barberiaId);
}