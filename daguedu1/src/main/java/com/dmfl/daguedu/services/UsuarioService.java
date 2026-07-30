package com.dmfl.daguedu.services;

import com.dmfl.daguedu.dto.RegistroRequest;
import com.dmfl.daguedu.dto.PerfilRequest;
import com.dmfl.daguedu.modelo.ClienteEntity;
import com.dmfl.daguedu.modelo.Rol;
import com.dmfl.daguedu.modelo.UsuarioEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dmfl.daguedu.repository.ClienteRepository;
import com.dmfl.daguedu.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import java.util.List;

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
            usuario.setDireccion(request.getDireccion());
            usuario.setTelefono(request.getTelefono());

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

        @Transactional
        public UsuarioEntity actualizarPerfil(PerfilRequest request) {
            UsuarioEntity usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            usuario.setNombre(request.getNombre());
            usuario.setDireccion(request.getDireccion());
            usuario.setTelefono(request.getTelefono());
            UsuarioEntity actualizado = usuarioRepository.save(usuario);

            if (actualizado.getRole() == Rol.ROLE_CLIENTE) {
                ClienteEntity cliente = clienteRepository.findByEmail(actualizado.getEmail())
                        .orElseGet(ClienteEntity::new);
                cliente.setNombre(actualizado.getNombre());
                cliente.setEmail(actualizado.getEmail());
                cliente.setDireccion(actualizado.getDireccion());
                cliente.setTelefono(actualizado.getTelefono());
                clienteRepository.save(cliente);
            }
            return actualizado;
        }

        @Transactional
        public List<UsuarioEntity> obtenerUsuarios() {
            return usuarioRepository.findAll();
        }

        @Transactional
        public UsuarioEntity actualizarUsuario(Long id, RegistroRequest request) {
            UsuarioEntity usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            usuario.setNombre(request.getNombre());
            usuario.setDireccion(request.getDireccion());
            usuario.setTelefono(request.getTelefono());
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            Rol rol = "ROLE_ADMIN".equalsIgnoreCase(request.getRol()) ? Rol.ROLE_ADMIN : Rol.ROLE_CLIENTE;
            usuario.setRole(rol);
            UsuarioEntity actualizado = usuarioRepository.save(usuario);

            ClienteEntity cliente = clienteRepository.findByEmail(actualizado.getEmail()).orElseGet(ClienteEntity::new);
            if (rol == Rol.ROLE_CLIENTE) {
                cliente.setNombre(actualizado.getNombre());
                cliente.setEmail(actualizado.getEmail());
                cliente.setDireccion(actualizado.getDireccion());
                cliente.setTelefono(actualizado.getTelefono());
                clienteRepository.save(cliente);
            } else {
                clienteRepository.findByEmail(actualizado.getEmail()).ifPresent(clienteRepository::delete);
            }
            return actualizado;
        }

        @Transactional
        public void eliminarUsuario(Long id) {
            UsuarioEntity usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            clienteRepository.findByEmail(usuario.getEmail()).ifPresent(clienteRepository::delete);
            usuarioRepository.delete(usuario);
        }

}
