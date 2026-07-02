package com.utp.barberflow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos extra que mande Angular
public class ReservaRequest {
    private Long usuarioId;
    private Long barberiaId;
    private Long barberoId;
    private String horario;
    private List<ItemCarrito> carrito;
private java.time.LocalDate fecha; 
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemCarrito {
        private ProductoDto producto;
        private Integer cantidad;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductoDto {
        private Long id;
        private Double precio;
    }
}