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
}