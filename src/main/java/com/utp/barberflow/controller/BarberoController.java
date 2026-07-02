package com.utp.barberflow.controller;

import com.utp.barberflow.dto.BarberoRegistroDTO;
import com.utp.barberflow.entity.Barbero;
import com.utp.barberflow.service.BarberoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/barberos")
@CrossOrigin(origins = "http://localhost:4200")
public class BarberoController {

    @Autowired
    private BarberoService barberoService;

    @GetMapping
    public List<Barbero> listarBarberos() {
        return barberoService.obtenerTodos();
    }

    @GetMapping("/barberia/{barberiaId}")
    public ResponseEntity<List<Barbero>> obtenerBarberosPorBarberia(@PathVariable Long barberiaId) {
        List<Barbero> barberos = barberoService.obtenerPorBarberia(barberiaId);
        return ResponseEntity.ok(barberos);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Barbero> obtenerBarberoPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(barberoService.obtenerPorUsuario(usuarioId));
    }

    // --- ESTE ES EL MÉTODO CORREGIDO ---
    @PostMapping("/barberia/{barberiaId}")
    public ResponseEntity<Map<String, String>> registrarEmpleado(@PathVariable Long barberiaId, @RequestBody BarberoRegistroDTO dto) {
        Map<String, String> respuesta = new HashMap<>();
        try {
            barberoService.registrarBarberoConUsuario(dto, barberiaId);
            respuesta.put("mensaje", "Empleado registrado con éxito");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace(); 
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        barberoService.eliminarBarbero(id);
        return ResponseEntity.noContent().build();
    }
}