package com.example.Nutriologa.Analia.Roman.service;

import com.example.Nutriologa.Analia.Roman.model.Servicio;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import com.example.Nutriologa.Analia.Roman.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public Servicio guardarServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public List<Servicio> obtenerServiciosPorUsuario(Usuario usuario) {
        return servicioRepository.findByUsuario(usuario);
    }

    public void eliminarServicio(Long id) {
        servicioRepository.deleteById(id);
    }

    public Servicio obtenerServicioPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }
}
