package com.dmfl.daguedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.dmfl.daguedu.modelo.ClienteEntity;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByEmail(String email);
}
