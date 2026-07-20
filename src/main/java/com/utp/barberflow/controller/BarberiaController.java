package com.utp.barberflow.controller;

import com.utp.barberflow.entity.Barberia;
import com.utp.barberflow.service.BarberiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barberias")
@CrossOrigin(origins = "http://localhost:4200")
public class BarberiaController {

    @Autowired
    private BarberiaService barberiaService;

    @GetMapping
    public List<Barberia> listarTodas() {
        return barberiaService.obtenerTodas();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Barberia> obtenerBarberia(@PathVariable Long id) {
        return ResponseEntity.ok(barberiaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barberia> actualizarBarberia(@PathVariable Long id, @RequestBody Barberia barberia) {
        return ResponseEntity.ok(barberiaService.actualizarBarberia(id, barberia));
    }
}