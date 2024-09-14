package com.example.Nutriologa.Analia.Roman.security;

import com.example.Nutriologa.Analia.Roman.model.Usuario;
import com.example.Nutriologa.Analia.Roman.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.userdetails.User;
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;  // Asegúrate de tener esta inyección

    /*public String login(String correo, String contrasena) {
        // Carga el usuario por correo
        UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

        // Verifica la contraseña
        if (passwordEncoder.matches(contrasena, userDetails.getPassword())) {
            // Genera el token JWT
            return jwtUtil.generateToken(userDetails);  // Devuelve el token como String
        } else {
            throw new BadCredentialsException("Contraseña incorrecta");
        }
    }*/

    public Usuario registrar(Usuario usuario) {
        usuario.setContrasena(bCryptPasswordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }
    public Map<String, Object> login(String correo, String contrasena) {
        // Busca el usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

        // Verifica la contraseña
        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        // Crear un objeto UserDetails desde el Usuario
        UserDetails userDetails = new User(usuario.getCorreo(), usuario.getContrasena(), new ArrayList<>());

        // Genera el token JWT
        String token = jwtUtil.generateToken(userDetails);

        // Devuelve el usuario y el token en un mapa
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("usuario", usuario);
        resultado.put("token", token);

        return resultado;
    }
}