package com.utp.barberflow.service;

import com.utp.barberflow.entity.Barberia;
import com.utp.barberflow.repository.BarberiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberiaService {

    @Autowired
    private BarberiaRepository barberiaRepository;

    public List<Barberia> obtenerTodas() {
        return barberiaRepository.findAll();
    }
    public Barberia obtenerPorId(Long id) {
        return barberiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbería no encontrada"));
    }

    public Barberia actualizarBarberia(Long id, Barberia datosNuevos) {
        Barberia barberia = obtenerPorId(id);
        barberia.setNombre(datosNuevos.getNombre());
        barberia.setCategoria(datosNuevos.getCategoria());
        barberia.setDireccion(datosNuevos.getDireccion());
        barberia.setTelefono(datosNuevos.getTelefono());
        barberia.setDescripcion(datosNuevos.getDescripcion());
        barberia.setImagen(datosNuevos.getImagen()); // Guardamos la nueva foto
        
        return barberiaRepository.save(barberia);
    }
}