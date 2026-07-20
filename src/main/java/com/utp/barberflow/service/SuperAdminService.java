package com.utp.barberflow.service;

import com.utp.barberflow.dto.SuperAdminDTO;
import com.utp.barberflow.entity.Barberia;
import com.utp.barberflow.entity.Usuario;
import com.utp.barberflow.repository.BarberiaRepository;
import com.utp.barberflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SuperAdminService {

    @Autowired
    private BarberiaRepository barberiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Crear Barbería Carcasa + Su primer Admin
    @Transactional
    public void crearBarberiaConAdmin(SuperAdminDTO dto) {
        // Creamos la carcasa vacía
        Barberia nuevaBarberia = new Barberia();
        nuevaBarberia.setNombre("Barberia_Nueva_" + System.currentTimeMillis()); 
        nuevaBarberia = barberiaRepository.save(nuevaBarberia);

        // Creamos su primer administrador
        Usuario nuevoAdmin = new Usuario();
        nuevoAdmin.setNombre(dto.getAdminNombre());
        nuevoAdmin.setEmail(dto.getAdminEmail());
        nuevoAdmin.setTelefono(dto.getAdminTelefono());
        nuevoAdmin.setPassword(passwordEncoder.encode(dto.getAdminPassword()));
        nuevoAdmin.setRol("ADMIN");
        nuevoAdmin.setBarberia(nuevaBarberia);
        
        usuarioRepository.save(nuevoAdmin);
    }

    // 2. Añadir un Admin extra a una barbería existente
    public void añadirAdminABarberia(Long barberiaId, SuperAdminDTO dto) {
        Barberia barberia = barberiaRepository.findById(barberiaId).orElseThrow();
        
        Usuario nuevoAdmin = new Usuario();
        nuevoAdmin.setNombre(dto.getAdminNombre());
        nuevoAdmin.setEmail(dto.getAdminEmail());
        nuevoAdmin.setTelefono(dto.getAdminTelefono());
        nuevoAdmin.setPassword(passwordEncoder.encode(dto.getAdminPassword()));
        nuevoAdmin.setRol("ADMIN");
        nuevoAdmin.setBarberia(barberia);
        
        usuarioRepository.save(nuevoAdmin);
    }

    // 3. Obtener todos los administradores de una barbería
    public List<Usuario> obtenerAdminsPorBarberia(Long barberiaId) {
        return usuarioRepository.findByBarberiaIdAndRol(barberiaId, "ADMIN");
    }

    // 4. Eliminar un administrador
    public void eliminarAdmin(Long adminId) {
        usuarioRepository.deleteById(adminId);
    }

    // 5. Eliminar Barbería (Borrado en cascada manual para evitar errores de Foreign Key)
    @Transactional
    public void eliminarBarberiaTotal(Long barberiaId) {
        // NOTA: Para que esto funcione perfecto, debes asegurarte de borrar primero 
        // los productos, citas y barberos asociados a esta barberiaId.
        // Por simplicidad, aquí borramos a sus administradores y luego el local:
        List<Usuario> admins = usuarioRepository.findByBarberiaIdAndRol(barberiaId, "ADMIN");
        usuarioRepository.deleteAll(admins);
        barberiaRepository.deleteById(barberiaId);
    }
}