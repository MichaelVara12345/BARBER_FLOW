package com.utp.barberflow.controller;

import com.utp.barberflow.dto.SuperAdminDTO;
import com.utp.barberflow.entity.Usuario;
import com.utp.barberflow.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/superadmin")
@CrossOrigin(origins = "http://localhost:4200")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping("/barberias")
    public ResponseEntity<?> crearBarberia(@RequestBody SuperAdminDTO dto) {
        superAdminService.crearBarberiaConAdmin(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/barberias/{id}")
    public ResponseEntity<?> eliminarBarberia(@PathVariable Long id) {
        superAdminService.eliminarBarberiaTotal(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/barberias/{id}/admins")
    public ResponseEntity<List<Usuario>> verAdmins(@PathVariable Long id) {
        return ResponseEntity.ok(superAdminService.obtenerAdminsPorBarberia(id));
    }

    @PostMapping("/barberias/{id}/admins")
    public ResponseEntity<?> agregarAdmin(@PathVariable Long id, @RequestBody SuperAdminDTO dto) {
        superAdminService.añadirAdminABarberia(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<?> eliminarAdmin(@PathVariable Long id) {
        superAdminService.eliminarAdmin(id);
        return ResponseEntity.ok().build();
    }
}