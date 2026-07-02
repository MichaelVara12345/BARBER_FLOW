package com.utp.barberflow.service;

import com.utp.barberflow.entity.Producto;
import com.utp.barberflow.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerPorBarberia(Long barberiaId) {
        return productoRepository.findByBarberiaId(barberiaId);
    }
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}