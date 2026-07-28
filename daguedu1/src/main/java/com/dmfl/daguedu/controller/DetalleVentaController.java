package com.dmfl.daguedu.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmfl.daguedu.modelo.DetalleVentaEntity;
import com.dmfl.daguedu.services.DetalleVentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/detalleventa")
@RequiredArgsConstructor
public class DetalleVentaController {

    private final DetalleVentaService servicio;

    @GetMapping("/")
    public ResponseEntity<List<DetalleVentaEntity>> listar() {
        return ResponseEntity.ok(servicio.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaEntity> obtenerDetallesEntity(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DetalleVentaEntity> eliminar(@PathVariable Long id){
        servicio.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/")
    public ResponseEntity<DetalleVentaEntity> crear(@RequestBody DetalleVentaEntity detalle) {
        DetalleVentaEntity nuevo = servicio.guardarDetalle(detalle);
        return new ResponseEntity<> (nuevo, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody DetalleVentaEntity detalle) {
        try{
        DetalleVentaEntity actualizado = servicio.actualizarDetalle(id, detalle);
        return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
