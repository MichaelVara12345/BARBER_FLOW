package com.utp.barberflow.dto;

import lombok.Data;
import java.util.List;

@Data
public class BarberoRegistroDTO {
    private String nombre;
    private String email;
    private String telefono;
    private String password;
    private String especialidad;
    private String descripcion;
    private List<String> horarios;
}