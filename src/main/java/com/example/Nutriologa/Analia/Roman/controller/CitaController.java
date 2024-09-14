package com.example.Nutriologa.Analia.Roman.controller;

import com.example.Nutriologa.Analia.Roman.model.Cita;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import com.example.Nutriologa.Analia.Roman.repository.CitaRepository;
import com.example.Nutriologa.Analia.Roman.repository.UsuarioRepository;
import com.example.Nutriologa.Analia.Roman.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private CitaService  citaService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint para agendar una cita sin autenticación
    @PostMapping("/agendar")
    public ResponseEntity<Map<String, String>> agendarCita(@RequestBody Cita cita) {
        Map<String, String> response = new HashMap<>();

        try {
            // Verificar si la fecha y hora ya están ocupadas
            Optional<Cita> citaExistente = citaRepository.findByFechaHora(cita.getFechaHora());

            if (citaExistente.isPresent()) {
                // Si ya hay una cita con la misma fecha y hora, retornar error
                response.put("error", "La fecha y hora seleccionada ya está ocupada.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Asignar el nutriólogo a la cita
            Usuario nutriologo = usuarioRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Nutriólogo no encontrado"));
            cita.setUsuario(nutriologo);

            // Guardar la nueva cita
            Cita nuevaCita = citaRepository.save(cita);

            response.put("status", "Cita agendada con éxito");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("error", "Error al agendar la cita");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/historia/{cedula}")
    public ResponseEntity<List<Cita>> obtenerCitasPorCedula(@PathVariable String cedula) {
        List<Cita> citas = citaRepository.findByCedula(cedula);
        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(citas);
    }
    @GetMapping("/listar")
    public ResponseEntity<List<Cita>> listarCitas(Authentication authentication) {
        String correoUsuario = authentication.getName(); // Obtener el correo del usuario autenticado
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Cita> citas = citaRepository.findByUsuario(usuario); // Obtener solo las citas del Nutriólogo autenticado
        return ResponseEntity.ok(citas); // Enviar las citas en formato JSON
    }
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar una cita
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita citaActualizada) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada con id: " + id));

        cita.setNombre(citaActualizada.getNombre());
        cita.setFechaHora(citaActualizada.getFechaHora());
        cita.setMotivo(citaActualizada.getMotivo());
        cita.setTelefono(citaActualizada.getTelefono());

        Cita citaActualizadaResponse = citaRepository.save(cita);
        return ResponseEntity.ok(citaActualizadaResponse);
    }



    }
    /*  @GetMapping("/listar")
    public ResponseEntity<List<Cita>> read() {
        return new ResponseEntity<>(citaService.findByAll(), HttpStatus.OK);
    }
    // Endpoint para obtener todas las citas vinculadas al nutriólogo autenticado
   @GetMapping("/listar")
    public ResponseEntity<List<Cita>> listarCitas(Authentication authentication) {
        String correoUsuario = authentication.getName(); // Obtener el correo del usuario autenticado
        System.out.println("Correo del usuario autenticado: " + correoUsuario);

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        System.out.println("Usuario encontrado: " + usuario.getNombre());

        List<Cita> citas = citaRepository.findByUsuario(usuario); // Obtener solo las citas del Nutriólogo autenticado
        return ResponseEntity.ok(citas); // Asegúrate de retornar las citas correctamente en formato JSON
    }*/




