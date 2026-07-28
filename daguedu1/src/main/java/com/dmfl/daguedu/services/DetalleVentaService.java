package com.dmfl.daguedu.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dmfl.daguedu.modelo.DetalleVentaEntity;
import com.dmfl.daguedu.repository.DetalleVentaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetalleVentaService {

    private final DetalleVentaRepository repository;

    @Transactional(readOnly = true)
    public List<DetalleVentaEntity> obtenerTodos(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public DetalleVentaEntity obtenerPorId(Long id){
        return repository.findById(id).orElseThrow(
            () -> new RuntimeException("DetalleVenta no encontrado: " + id));
    }

    @Transactional
    public DetalleVentaEntity guardarDetalle(DetalleVentaEntity detalle){
        return repository.save(detalle);
    }

    @Transactional
    public void eliminarDetalle(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("DetalleVenta no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public DetalleVentaEntity actualizarDetalle(Long id, DetalleVentaEntity detalle){
        DetalleVentaEntity existente = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("DetalleVenta no existe: " + id));

        existente.setCantidad(detalle.getCantidad());
        existente.setPrecioUnitario(detalle.getPrecioUnitario());
        existente.setSubtotal(detalle.getSubtotal());

        return repository.save(existente);
    }

}
