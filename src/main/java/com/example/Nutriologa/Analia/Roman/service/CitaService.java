package com.example.Nutriologa.Analia.Roman.service;
import com.example.Nutriologa.Analia.Roman.model.Cita;
import com.example.Nutriologa.Analia.Roman.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;



    public Cita save(Cita cita) {
        return citaRepository.save(cita);
    }

    public List<Cita> findByAll() {return citaRepository.findAll();
    }

    // Otros métodos según sea necesario (e.g., encontrar por ID, eliminar, etc.)
}