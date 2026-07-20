package com.utp.barberflow.dto;

import lombok.Data;
import java.util.List;

@Data
public class EdicionCitaDTO {
    private String nuevaHora;
    private List<ProductoEditado> productos;

    @Data
    public static class ProductoEditado {
        private Long reservaProductoId;
        private Integer nuevaCantidad;
    }
}