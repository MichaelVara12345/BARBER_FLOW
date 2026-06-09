package com.utp.barberflow.controller;

import com.utp.barberflow.entity.Barberia;
import com.utp.barberflow.service.BarberiaService;
import org.springframework.beans.factory.annotation.Autowired;
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
}