package com.example.Nutriologa.Analia.Roman.controller;

import com.example.Nutriologa.Analia.Roman.model.LoginRequest;
import com.example.Nutriologa.Analia.Roman.model.Usuario;
import com.example.Nutriologa.Analia.Roman.repository.UsuarioRepository;
import com.example.Nutriologa.Analia.Roman.security.AuthService;
import com.example.Nutriologa.Analia.Roman.security.JwtResponse;
import com.example.Nutriologa.Analia.Roman.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registro")
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return authService.registrar(usuario);  // Asegúrate de que el servicio `registrar` esté funcionando correctamente
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> resultado = authService.login(loginRequest.getCorreo(), loginRequest.getContrasena());
        Usuario usuario = (Usuario) resultado.get("usuario");
        String token = (String) resultado.get("token");

        if (token != null) {
            Map<String, String> response = new HashMap<>();
            response.put("nombre", usuario.getNombre());
            response.put("direccion", usuario.getDireccion());
            response.put("telefono", usuario.getTelefono());
            response.put("token", token);
            return ResponseEntity.ok(response);  // Devuelve tanto el nombre como el token
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<Usuario> obtenerPerfil(Authentication authentication) {
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(usuario);
    }

    // Actualizar el perfil del usuario autenticado
    @PutMapping("/perfil/actualizar")
    public ResponseEntity<Usuario> actualizarPerfil(@RequestBody Usuario usuarioActualizado, Authentication authentication) {
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        Usuario usuarioActualizadoResponse = usuarioRepository.save(usuario);

        return ResponseEntity.ok(usuarioActualizadoResponse);
    }


}
