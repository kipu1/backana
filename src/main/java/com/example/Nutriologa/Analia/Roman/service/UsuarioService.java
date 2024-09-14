package com.example.Nutriologa.Analia.Roman.service;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import com.example.Nutriologa.Analia.Roman.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    // Otros métodos según sea necesario (e.g., actualizar, eliminar, etc.)
}