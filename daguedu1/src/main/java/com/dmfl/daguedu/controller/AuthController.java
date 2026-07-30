package com.dmfl.daguedu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dmfl.daguedu.dto.AuthRequest;
import com.dmfl.daguedu.dto.AuthResponse;
import com.dmfl.daguedu.dto.RegistroRequest;
import com.dmfl.daguedu.dto.PerfilRequest;
import com.dmfl.daguedu.modelo.ClienteEntity;
import com.dmfl.daguedu.modelo.UsuarioEntity;
import com.dmfl.daguedu.repository.ClienteRepository;
import com.dmfl.daguedu.repository.UsuarioRepository;
import com.dmfl.daguedu.segurity.JwtTokenProvider;
import com.dmfl.daguedu.services.UsuarioService;
import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")

public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtokenPrivider;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;


    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtokenPrivider,
            UsuarioService usuarioService, UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtokenPrivider = jwtokenPrivider;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userPrincipal = (User) authentication.getPrincipal();
        String token = jwtokenPrivider.generateToken(authentication);
        String authority = userPrincipal.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .findFirst()
            .orElse("ROLE_CLIENTE");

        UsuarioEntity usuario = usuarioRepository.findByEmail(userPrincipal.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        ClienteEntity cliente = clienteRepository.findByEmail(usuario.getEmail()).orElse(null);

        return ResponseEntity.ok(new AuthResponse(token, usuario.getUsername(), usuario.getNombre(), authority,
            usuario.getEmail(), usuario.getDireccion() != null ? usuario.getDireccion() : (cliente != null ? cliente.getDireccion() : null),
            usuario.getTelefono() != null ? usuario.getTelefono() : (cliente != null ? cliente.getTelefono() : null)));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {
        try {
            UsuarioEntity usuario = usuarioService.saveUsuario(request);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioEntity>> usuarios() {
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }

    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarPerfil(@RequestBody PerfilRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.actualizarPerfil(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody RegistroRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    

}
