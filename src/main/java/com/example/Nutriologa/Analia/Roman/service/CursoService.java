package com.example.Nutriologa.Analia.Roman.service;


import com.example.Nutriologa.Analia.Roman.model.Curso;
import com.example.Nutriologa.Analia.Roman.model.Servicio;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import com.example.Nutriologa.Analia.Roman.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Curso crearCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public List<Curso> obtenerCursos() {
        return cursoRepository.findAll();
    }

    public Curso obtenerCursoPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }
    public List<Curso> obtenerServiciosPorUsuario(Usuario usuario) {
        return cursoRepository.findByUsuario(usuario);
    }
    public boolean verificarPassword(Long id, String password) {
        Curso curso = obtenerCursoPorId(id);
        return curso != null && curso.getPassword().equals(password);
    }
}