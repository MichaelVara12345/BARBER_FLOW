package com.utp.barberflow.service;

import com.utp.barberflow.entity.Usuario;
import com.utp.barberflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Herramienta para encriptar contraseñas
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Método para REGISTRAR
    public Usuario registrarUsuario(Usuario usuario) {
        // Verificamos si el correo ya existe
        Optional<Usuario> existe = usuarioRepository.findByEmail(usuario.getEmail());
        if (existe.isPresent()) {
            throw new RuntimeException("El correo ya está en uso");
        }
        
        // Encriptamos la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Asignamos rol por defecto si no trae uno
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("CLIENTE");
        }
        
        return usuarioRepository.save(usuario);
    }

    // Método para LOGIN
    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Comparamos la contraseña encriptada
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario; // Si todo está bien, devolvemos el usuario
    }
}