package com.utp.barberflow.controller;

import com.utp.barberflow.dto.ReservaRequest;
import com.utp.barberflow.entity.Reserva;
import com.utp.barberflow.entity.ReservaProducto;
import com.utp.barberflow.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    

    @PostMapping
    public ResponseEntity<Map<String, String>> crearReserva(@RequestBody ReservaRequest request) {
        Map<String, String> respuesta = new HashMap<>();
        try {
            // Intentamos guardar la reserva
            reservaService.crearReservaCompleta(request);
            
            // Si funciona, devolvemos un mensaje simple (Esto evita el bucle infinito)
            respuesta.put("mensaje", "Reserva creada con éxito");
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            // Si falla, imprimimos el error en rojo en la consola de tu Eclipse/IntelliJ/VSCode
            e.printStackTrace(); 
            
            // Y le enviamos el motivo exacto a Angular para que lo leas en F12
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }
    @GetMapping("/{reservaId}/productos")
    public ResponseEntity<List<ReservaProducto>> obtenerProductosPorReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(reservaService.obtenerProductosPorReserva(reservaId));
    }

    @GetMapping("/mis-citas/{usuarioId}")
    public ResponseEntity<?> misReservas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reservaService.obtenerMisReservas(usuarioId));
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reservaService.actualizarEstado(id, body.get("estado")));
    }
    @GetMapping("/barbero/{barberoId}")
    public ResponseEntity<List<Reserva>> obtenerReservasPorBarbero(@PathVariable Long barberoId) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorBarbero(barberoId));
    }
    @PutMapping("/{id}/modificar")
    public ResponseEntity<?> modificarReserva(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String nuevaHora = (String) body.get("hora");
        List<Map<String, Object>> productos = (List<Map<String, Object>>) body.get("productos");
        
        reservaService.modificarReserva(id, nuevaHora, productos);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<String>> obtenerHorariosDisponibles(
            @RequestParam Long barberoId, 
            @RequestParam String fecha) {
        return ResponseEntity.ok(reservaService.obtenerHorariosDisponibles(barberoId, java.time.LocalDate.parse(fecha)));
    }
    @GetMapping("/barbero/{barberoId}/exportar")
    public ResponseEntity<byte[]> exportarCitasExcel(@PathVariable Long barberoId) {
        try {
            byte[] excelContent = reservaService.generarReporteCitasExcel(barberoId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "Reporte_Citas_Barbero_" + barberoId + ".xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelContent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}