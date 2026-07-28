package com.dmfl.daguedu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmfl.daguedu.modelo.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>{
    Optional<UsuarioEntity> findByUsername(String username);
    Optional<UsuarioEntity> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


}
