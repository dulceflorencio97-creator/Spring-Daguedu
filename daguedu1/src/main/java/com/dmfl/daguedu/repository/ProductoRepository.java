package com.dmfl.daguedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmfl.daguedu.modelo.ProductoEntity;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {

}
