package com.utp.barberflow.repository;

import com.utp.barberflow.entity.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberoRepository extends JpaRepository<Barbero, Long> {
}