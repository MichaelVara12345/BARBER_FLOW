package com.utp.barberflow.service;

import com.utp.barberflow.dto.ReservaRequest;
import com.utp.barberflow.entity.*;
import com.utp.barberflow.repository.ProductoRepository;
import com.utp.barberflow.repository.ReservaProductoRepository;
import com.utp.barberflow.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ReservaService {
    @Autowired
    private com.utp.barberflow.repository.BarberoRepository barberoRepository;
@Autowired
private com.utp.barberflow.repository.UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService; // <-- Inyectamos el servicio
    
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaProductoRepository reservaProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

  @Transactional
    public Reserva crearReservaCompleta(ReservaRequest request) {
        Reserva reserva = new Reserva();
        reserva.setFecha(request.getFecha()); // <-- AHORA USA LA FECHA QUE ELIGIÓ EL CLIENTE
        reserva.setHora(request.getHorario());
        reserva.setEstado("PENDIENTE");
     

        Usuario u = new Usuario(); u.setId(request.getUsuarioId());
        reserva.setUsuario(u);

        Barberia b = new Barberia(); b.setId(request.getBarberiaId());
        reserva.setBarberia(b);

        Barbero barbero = new Barbero(); barbero.setId(request.getBarberoId());
        reserva.setBarbero(barbero);

        Reserva reservaGuardada = reservaRepository.save(reserva);

        // 2. Guardar los productos seleccionados
        if (request.getCarrito() != null) {
            for (ReservaRequest.ItemCarrito item : request.getCarrito()) {
                ReservaProducto rp = new ReservaProducto();
                rp.setReserva(reservaGuardada);
                
                Producto p = new Producto(); p.setId(item.getProducto().getId());
                rp.setProducto(p);
                
                rp.setCantidad(item.getCantidad());
                rp.setPrecioUnitario(BigDecimal.valueOf(item.getProducto().getPrecio()));
                
                reservaProductoRepository.save(rp);
            }
        }
     
        // --- 3. DISPARAR CORREO AUTOMÁTICO ---
      try {
        // Buscamos al usuario en la BD usando el ID que viene en la petición
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String asunto = "¡Reserva confirmada en BarberFlow!";
        String mensaje = "Hola " + usuario.getNombre() + 
                         ", tu reserva para el día " + reservaGuardada.getFecha() + 
                         " a las " + reservaGuardada.getHora() + 
                         " ha sido confirmada con éxito.";

        emailService.enviarCorreo(usuario.getEmail(), asunto, mensaje);
    } catch (Exception e) {
        System.err.println("❌ Error al enviar correo: " + e.getMessage());
    }

    return reservaGuardada;
}

    public List<Reserva> obtenerMisReservas(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    public List<ReservaProducto> obtenerProductosPorReserva(Long reservaId) {
        return reservaProductoRepository.findByReservaId(reservaId);
    }
  @Transactional // IMPORTANTE: Asegura que todo se guarde junto o nada se guarde
    public Reserva actualizarEstado(Long id, String nuevoEstado) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        // Verificamos si la cita está pasando a estado "TERMINADO" por primera vez
        if (nuevoEstado.equals("TERMINADO") && !reserva.getEstado().equals("TERMINADO")) {
            
            // 1. Buscamos qué productos compró en esta reserva
            List<ReservaProducto> productosComprados = reservaProductoRepository.findByReservaId(id);
            
            // 2. Recorremos la lista y restamos el stock de cada uno
            for (ReservaProducto item : productosComprados) {
                Producto productoDB = item.getProducto();
                
                // Nos aseguramos de que haya stock suficiente para restar
                if (productoDB.getStock() != null && productoDB.getStock() >= item.getCantidad()) {
                    productoDB.setStock(productoDB.getStock() - item.getCantidad());
                } else if (productoDB.getStock() != null) {
                    productoDB.setStock(0); // Si intentó comprar más de lo que hay, lo dejamos en 0
                }
                
                // Guardamos el nuevo stock en la base de datos
                productoRepository.save(productoDB);
            }
        }

        // Finalmente, actualizamos el estado de la cita
        reserva.setEstado(nuevoEstado);
        return reservaRepository.save(reserva);
    }
    public List<Reserva> obtenerReservasPorBarbero(Long barberoId) {
        return reservaRepository.findByBarberoId(barberoId);
    }
    @Transactional
    public void modificarReserva(Long id, String nuevaHora, List<Map<String, Object>> productosModificados) {
        // 1. Actualizamos la hora de la cita
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        reserva.setHora(nuevaHora);
        reservaRepository.save(reserva);

        // 2. Actualizamos las cantidades de los productos
        if (productosModificados != null) {
            for (Map<String, Object> p : productosModificados) {
                Long rpId = Long.valueOf(p.get("id").toString());
                Integer nuevaCantidad = Integer.valueOf(p.get("cantidad").toString());
                
                ReservaProducto rp = reservaProductoRepository.findById(rpId).orElseThrow();
                rp.setCantidad(nuevaCantidad);
                reservaProductoRepository.save(rp);
            }
        }
    }
   // --- MOTOR DE DISPONIBILIDAD CON DÍAS DE LA SEMANA ---
    public List<String> obtenerHorariosDisponibles(Long barberoId, LocalDate fecha) {
        Barbero barbero = barberoRepository.findById(barberoId).orElseThrow();
        List<String> horariosBase = barbero.getHorarios(); // Ej: ["Lunes - 10:00 AM", "Martes - 03:00 PM"]

        // 1. Averiguamos qué día de la semana es la fecha que eligió el cliente (en español)
        String diaSemana = obtenerDiaSemanaEspanol(fecha.getDayOfWeek().getValue());

        // 2. Filtramos el horario del barbero para sacar SOLO los turnos de ese día
        // Ignoramos mayúsculas/minúsculas y acentos por si el admin escribió "Lunes" o "lunes"
        List<String> horariosDelDia = horariosBase.stream()
                .filter(h -> h.toLowerCase().contains(diaSemana.toLowerCase()))
                .toList();

        // 3. Buscamos qué citas ya están confirmadas para ese día exacto
        List<Reserva> reservasEseDia = reservaRepository.findByBarberoIdAndFecha(barberoId, fecha);

        List<String> horasOcupadas = reservasEseDia.stream()
                .filter(r -> !r.getEstado().equals("CANCELADA"))
                .map(Reserva::getHora)
                .toList();

        // 4. Comparamos y devolvemos las horas libres
        return horariosDelDia.stream()
                .filter(hora -> !horasOcupadas.contains(hora))
                .toList();
    }

    // --- FUNCIÓN TRADUCTORA AUXILIAR ---
    private String obtenerDiaSemanaEspanol(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1: return "lunes";
            case 2: return "martes";
            case 3: return "miercoles"; // Sin tilde para evitar errores de tipeo del Admin
            case 4: return "jueves";
            case 5: return "viernes";
            case 6: return "sabado"; // Sin tilde
            case 7: return "domingo";
            default: return "";
        }
    }
    
    // --- GENERACIÓN DE REPORTE EXCEL (APACHE POI) ---
    public byte[] generarReporteCitasExcel(Long barberoId) throws Exception {
        // Obtener la lista de citas del barbero
        List<Reserva> citas = reservaRepository.findByBarberoId(barberoId);

        // Crear un libro de Excel en blanco y una hoja
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Mis Citas - BarberFlow");

        // Encabezados
        Row headerRow = sheet.createRow(0);
        String[] columnas = {"ID Reserva", "Nombre Cliente", "Teléfono", "Fecha", "Hora", "Estado"};
        
        // Estilo para el encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        //Llenar los datos de las citas en las filas siguientes
        int rowIdx = 1;
        for (Reserva cita : citas) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(cita.getId());
            
            // Validaciones por si el usuario es nulo
            String nombreCliente = (cita.getUsuario() != null && cita.getUsuario().getNombre() != null) ? cita.getUsuario().getNombre() : "Anónimo";
            String telefonoCliente = (cita.getUsuario() != null && cita.getUsuario().getTelefono() != null) ? cita.getUsuario().getTelefono() : "S/N";
            
            row.createCell(1).setCellValue(nombreCliente);
            row.createCell(2).setCellValue(telefonoCliente);
            row.createCell(3).setCellValue(cita.getFecha().toString());
            row.createCell(4).setCellValue(cita.getHora());
            row.createCell(5).setCellValue(cita.getEstado());
        }

        // ajustar el tamaño de las columnas automáticamente
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        //Convertir el archivo Excel a un arreglo de bytes para enviarlo por HTTP
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
    
}