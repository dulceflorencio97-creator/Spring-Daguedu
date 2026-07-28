package com.dmfl.daguedu.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dmfl.daguedu.modelo.ProveedorEntity;
import com.dmfl.daguedu.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository repository;

    @Transactional(readOnly = true)
    public List<ProveedorEntity> obtenerTodos(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ProveedorEntity obtenerPorId(Long id){
        return repository.findById(id).orElseThrow(
            () -> new RuntimeException("Proveedor no encontrado: " + id));
    }

    @Transactional
    public ProveedorEntity guardarProveedor(ProveedorEntity proveedor){
        return repository.save(proveedor);
    }

    @Transactional
    public void eliminarProveedor(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Proveedor no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public ProveedorEntity actualizarProveedor(Long id, ProveedorEntity proveedor){
        ProveedorEntity existente = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Proveedor no existe: " + id));
        
        existente.setNombre(proveedor.getNombre());
        existente.setContacto(proveedor.getContacto());
        existente.setEmail(proveedor.getEmail());
        existente.setTelefono(proveedor.getTelefono());

        return repository.save(existente);
    }

}
