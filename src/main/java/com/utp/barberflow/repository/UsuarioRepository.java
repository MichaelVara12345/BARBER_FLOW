package com.utp.barberflow.repository;

import com.utp.barberflow.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Boot crea la consulta SQL automáticamente solo con leer el nombre del método
    Optional<Usuario> findByEmail(String email);
}