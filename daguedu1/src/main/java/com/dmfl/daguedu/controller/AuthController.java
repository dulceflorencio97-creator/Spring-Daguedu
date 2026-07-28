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

import com.dmfl.daguedu.dto.AuthRequest;
import com.dmfl.daguedu.dto.AuthResponse;
import com.dmfl.daguedu.dto.RegistroRequest;
import com.dmfl.daguedu.modelo.UsuarioEntity;
import com.dmfl.daguedu.segurity.JwtTokenProvider;
import com.dmfl.daguedu.services.UsuarioService;


@RestController
@RequestMapping("/api/v1/auth")

public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtokenPrivider;
    private final UsuarioService usuarioService;


    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtokenPrivider,
            UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtokenPrivider = jwtokenPrivider;
        this.usuarioService = usuarioService;
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

        return ResponseEntity.ok(new AuthResponse(token,
            userPrincipal.getUsername(), userPrincipal.getUsername(), authority));
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


    

}
