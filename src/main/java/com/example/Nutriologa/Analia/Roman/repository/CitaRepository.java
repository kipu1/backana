package com.example.Nutriologa.Analia.Roman.repository;
import com.example.Nutriologa.Analia.Roman.model.Cita;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByUsuario(Usuario usuario);
    List<Cita> findByCedula(String cedula);
    Optional<Cita> findByFechaHora(LocalDateTime fechaHora);
}