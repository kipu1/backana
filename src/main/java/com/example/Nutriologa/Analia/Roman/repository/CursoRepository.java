package com.example.Nutriologa.Analia.Roman.repository;
import com.example.Nutriologa.Analia.Roman.model.Curso;
import com.example.Nutriologa.Analia.Roman.model.Servicio;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByUsuario(Usuario usuario);

}