package com.utp.barberflow.controller;

import com.utp.barberflow.entity.Barbero;
import com.utp.barberflow.service.BarberoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}