package com.utp.barberflow.service;

import com.utp.barberflow.entity.Reserva;
import com.utp.barberflow.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Guardar una nueva cita
    public Reserva crearReserva(Reserva reserva) {
        reserva.setEstado("PENDIENTE");
        return reservaRepository.save(reserva);
    }

    // Listar las citas de un cliente en específico
    public List<Reserva> obtenerMisReservas(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }
}