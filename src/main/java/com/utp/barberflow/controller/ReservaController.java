package com.utp.barberflow.controller;

import com.utp.barberflow.entity.Reserva;
import com.utp.barberflow.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public Reserva crearReserva(@RequestBody Reserva reserva) {
        return reservaService.crearReserva(reserva);
    }

    @GetMapping("/mis-citas/{usuarioId}")
    public List<Reserva> misReservas(@PathVariable Long usuarioId) {
        return reservaService.obtenerMisReservas(usuarioId);
    }
}