package com.dmfl.daguedu.segurity;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dmfl.daguedu.modelo.UsuarioEntity;
import com.dmfl.daguedu.repository.UsuarioRepository;

@Service

public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository= usuarioRepository;

    }
    @Override
    public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException{
        UsuarioEntity usuario= usuarioRepository.findByEmail(email)
        .orElseThrow(()-> new UsernameNotFoundException(
            "correo electrónico no encontrado: " + email ));
            return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(
                    usuario.getRole().name()
                ))
            );
       
        
    }

}
