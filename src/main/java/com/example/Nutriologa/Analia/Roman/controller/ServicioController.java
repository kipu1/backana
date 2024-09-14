package com.example.Nutriologa.Analia.Roman.controller;

import com.example.Nutriologa.Analia.Roman.model.Servicio;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import com.example.Nutriologa.Analia.Roman.repository.UsuarioRepository;
import com.example.Nutriologa.Analia.Roman.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/crear")
    public ResponseEntity<Servicio> crearServicio(@RequestBody Servicio servicio, Authentication authentication) {
        String correoUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        servicio.setUsuario(usuario);
        Servicio nuevoServicio = servicioService.guardarServicio(servicio);
        return ResponseEntity.ok(nuevoServicio);
    }
    @GetMapping("/listar")
    public ResponseEntity<List<Map<String, Object>>> listarServicios(Authentication authentication) {
        String correoUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Servicio> servicios = servicioService.obtenerServiciosPorUsuario(usuario);

        // Crear una lista de mapas donde cada mapa contiene los detalles del servicio y el teléfono
        List<Map<String, Object>> respuesta = new ArrayList<>();
        for (Servicio servicio : servicios) {
            Map<String, Object> servicioConDetalles = new HashMap<>();
            servicioConDetalles.put("id", servicio.getId());  // Incluir el ID
            servicioConDetalles.put("titulo", servicio.getTitulo());
            servicioConDetalles.put("descripcion", servicio.getDescripcion());
            servicioConDetalles.put("telefonoUsuario", servicio.getTelefonoUsuario()); // Obtener el teléfono del usuario
            respuesta.add(servicioConDetalles);
        }

        return ResponseEntity.ok(respuesta);
    }


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Servicio> actualizarServicio(@PathVariable Long id, @RequestBody Servicio servicioActualizado) {
        Servicio servicio = servicioService.obtenerServicioPorId(id);

        servicio.setTitulo(servicioActualizado.getTitulo());
        servicio.setDescripcion(servicioActualizado.getDescripcion());

        Servicio servicioActualizadoResponse = servicioService.guardarServicio(servicio);
        return ResponseEntity.ok(servicioActualizadoResponse);
    }
}