package com.utp.barberflow.service;

import com.utp.barberflow.dto.BarberoRegistroDTO;
import com.utp.barberflow.entity.Barberia;
import com.utp.barberflow.entity.Barbero;
import com.utp.barberflow.entity.Usuario;
import com.utp.barberflow.repository.BarberoRepository;
import com.utp.barberflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- NUEVA IMPORTACIÓN
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BarberoService {

    @Autowired
    private BarberoRepository barberoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // <-- INYECTAMOS LA "LICUADORA" DE CONTRASEÑAS

    public List<Barbero> obtenerTodos() {
        return barberoRepository.findAll();
    }

    public List<Barbero> obtenerPorBarberia(Long barberiaId) {
        return barberoRepository.findByBarberiaId(barberiaId);
    }

    public Barbero obtenerPorUsuario(Long usuarioId) {
        return barberoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public Barbero registrarBarberoConUsuario(BarberoRegistroDTO dto, Long barberiaId) {
        Barberia barberia = new Barberia();
        barberia.setId(barberiaId);

        // 1. Creamos la cuenta de acceso
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        
        // --- AQUÍ ESTÁ LA MAGIA: ENCRIPTAMOS LA CONTRASEÑA ---
        usuario.setPassword(passwordEncoder.encode(dto.getPassword())); 
        
        usuario.setRol("BARBERO");
        usuario.setBarberia(barberia);
        
        usuario = usuarioRepository.save(usuario);

        // 2. Creamos el perfil público
        Barbero barbero = new Barbero();
        barbero.setNombre(dto.getNombre());
        barbero.setEspecialidad(dto.getEspecialidad());
        barbero.setDescripcion(dto.getDescripcion());
        barbero.setHorarios(dto.getHorarios());
        barbero.setBarberia(barberia);
        barbero.setUsuario(usuario); 

        return barberoRepository.save(barbero); 
    }

    public void eliminarBarbero(Long id) {
        barberoRepository.deleteById(id);
    }
    
    @Transactional
    public Barbero actualizarBarbero(Long id, BarberoRegistroDTO dto) {
        // 1. Buscamos el barbero que ya existe en la BD
        Barbero barbero = barberoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));

        // 2. Actualizamos los datos del perfil público
        barbero.setNombre(dto.getNombre());
        barbero.setEspecialidad(dto.getEspecialidad());
        barbero.setDescripcion(dto.getDescripcion());
        barbero.setHorarios(dto.getHorarios());

        // 3. Buscamos y actualizamos su cuenta de Usuario asociada
        Usuario usuario = barbero.getUsuario();
        usuario.setNombre(dto.getNombre());
        usuario.setTelefono(dto.getTelefono());
        // El email generalmente no se actualiza porque es el "usuario" de login, pero puedes añadirlo si lo deseas

        // Si el admin escribió una contraseña nueva, la encriptamos y la cambiamos
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        usuarioRepository.save(usuario);
        return barberoRepository.save(barbero);
    }
}