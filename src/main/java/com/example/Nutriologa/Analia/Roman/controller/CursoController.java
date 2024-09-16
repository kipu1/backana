package com.example.Nutriologa.Analia.Roman.controller;

import com.example.Nutriologa.Analia.Roman.model.Curso;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import com.example.Nutriologa.Analia.Roman.repository.CursoRepository;
import com.example.Nutriologa.Analia.Roman.repository.UsuarioRepository;
import com.example.Nutriologa.Analia.Roman.service.CursoService;
import com.example.Nutriologa.Analia.Roman.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Crear Curso
    @PostMapping("/crear")
    public ResponseEntity<Object> crearCurso(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") String precio,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        // Obtener el usuario autenticado
        String correoUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar campos requeridos
        if (nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            return new ResponseEntity<>("Todos los campos obligatorios deben ser completados", HttpStatus.BAD_REQUEST);
        }

        // Guardar archivo
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al guardar el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Crear el nuevo curso
        Curso curso = new Curso();
        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        curso.setPrecio(precio);
        curso.setPassword(password);
        curso.setFileUrl(filePath);
        curso.setUsuario(usuario);

        // Guardar curso en la base de datos
        Curso nuevoCurso = cursoService.crearCurso(curso);
        return ResponseEntity.ok(nuevoCurso);
    }

    // Listar Cursos
    @GetMapping("/listar")
    public ResponseEntity<List<Map<String, Object>>> listarCursos(Authentication authentication) {
        // Obtener el correo del usuario autenticado
        String correoUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Curso> cursos = cursoService.obtenerCursosPorUsuario(usuario);

        // Crear una lista de mapas para agregar los detalles de cada curso junto con el teléfono del usuario
        List<Map<String, Object>> respuesta = new ArrayList<>();
        for (Curso curso : cursos) {
            Map<String, Object> cursoConDetalles = new HashMap<>();
            cursoConDetalles.put("id", curso.getId());  // Incluir el ID del curso
            cursoConDetalles.put("nombre", curso.getNombre());
            cursoConDetalles.put("descripcion", curso.getDescripcion());
            cursoConDetalles.put("precio", curso.getPrecio());
            cursoConDetalles.put("fileUrl", curso.getFileUrl());
            cursoConDetalles.put("telefonoUsuario", curso.getTelefonoUsuario()); // Obtener el teléfono del usuario
            respuesta.add(cursoConDetalles);
        }

        return ResponseEntity.ok(respuesta);
    }



    // Descargar Curso con Contraseña
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarCurso(@PathVariable Long id, @RequestParam String password) {
        Curso curso = cursoService.obtenerCursoPorId(id);

        if (curso == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!curso.getPassword().equals(password)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String filePath = curso.getFileUrl();
        File file = new File(filePath);

        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String mimeType;
        try {
            mimeType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            mimeType = "application/octet-stream";  // Si no se puede determinar el tipo, usar genérico
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }


    // Eliminar Curso
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar Curso
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Curso> actualizarCurso(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") String precio,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Curso curso = cursoService.obtenerCursoPorId(id);

        if (curso == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Actualizar campos del curso
        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        curso.setPrecio(precio);
        curso.setPassword(password);

        // Si hay un archivo, actualizar la URL del archivo
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = uploadDir + "/" + fileName;
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
                curso.setFileUrl(filePath); // Actualiza la URL del archivo
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Curso cursoActualizadoResponse = cursoService.crearCurso(curso);
        return ResponseEntity.ok(cursoActualizadoResponse);
    }
}