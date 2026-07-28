package com.dmfl.daguedu.services;

import com.dmfl.daguedu.dto.RegistroRequest;
import com.dmfl.daguedu.modelo.ClienteEntity;
import com.dmfl.daguedu.modelo.Rol;
import com.dmfl.daguedu.modelo.UsuarioEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dmfl.daguedu.repository.ClienteRepository;
import com.dmfl.daguedu.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
        ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
            this.usuarioRepository = usuarioRepository;
            this.clienteRepository = clienteRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @Transactional
        public UsuarioEntity saveUsuario(RegistroRequest request) {
            if (usuarioRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("el nombre de usuario ya esta en uso");
            }
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("el correo electrónico ya está en uso");
            }
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setUsername(request.getUsername());
            usuario.setEmail(request.getEmail());
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuario.setNombre(request.getNombre());

            Rol rol = Rol.ROLE_CLIENTE;
            if (request.getRol() != null && request.getRol().equalsIgnoreCase("ROLE_ADMIN")) {
                rol = Rol.ROLE_ADMIN;
            }
            usuario.setRole(rol);
            UsuarioEntity saveUsuario = usuarioRepository.save(usuario);
            if (rol == Rol.ROLE_CLIENTE) {
                ClienteEntity cliente = new ClienteEntity();
                cliente.setNombre(request.getNombre());
                cliente.setEmail(request.getEmail());
                cliente.setDireccion(request.getDireccion());
                cliente.setTelefono(request.getTelefono());
                clienteRepository.save(cliente);
            }
            return saveUsuario;
        }

}
