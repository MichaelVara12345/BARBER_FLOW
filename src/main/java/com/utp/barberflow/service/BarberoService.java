package com.utp.barberflow.service;

import com.utp.barberflow.entity.Barbero;
import com.utp.barberflow.repository.BarberoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BarberoService {

    @Autowired
    private BarberoRepository barberoRepository;

    public List<Barbero> obtenerTodos() {
        return barberoRepository.findAll();
    }
}