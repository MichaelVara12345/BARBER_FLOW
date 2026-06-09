package com.utp.barberflow.controller;

import com.utp.barberflow.entity.Usuario;
import com.utp.barberflow.service.UsuarioService;
import com.utp.barberflow.repository.UsuarioRepository; // <-- Agregamos el repositorio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200") // Permite la conexión con Angular
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository; // <-- Lo inyectamos para los nuevos métodos

    // ==========================================
    // MÉTODOS ORIGINALES (Login y Registro)
    // ==========================================

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            Usuario usuario = usuarioService.login(credenciales.get("email"), credenciales.get("password"));
            return ResponseEntity.ok(usuario); // Retorna los datos del usuario si el login es exitoso
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // NUEVOS MÉTODOS PARA LA VISTA "MIS DATOS"
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario datosActualizados) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setNombre(datosActualizados.getNombre());
                usuario.setTelefono(datosActualizados.getTelefono());
                usuario.setEmail(datosActualizados.getEmail());
                
                // Solo actualizamos la contraseña si el usuario escribió una nueva
                if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isEmpty()) {
                    usuario.setPassword(datosActualizados.getPassword());
                }
                
                Usuario actualizado = usuarioRepository.save(usuario);
                return ResponseEntity.ok(actualizado);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}