package com.dmfl.daguedu.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dmfl.daguedu.modelo.CategoriaEntity;
import com.dmfl.daguedu.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    @Transactional(readOnly = true)
    public List<CategoriaEntity> obtenerTodos(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CategoriaEntity obtenerPorId(Long id){
        return repository.findById(id).orElseThrow(
            () -> new RuntimeException("Categoria no encontrada: " + id));
    }

    @Transactional
    public CategoriaEntity guardarCategoria(CategoriaEntity categoria){
        return repository.save(categoria);
    }

    @Transactional
    public void eliminarCategoria(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Categoria no encontrada: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public CategoriaEntity actualizarCategoria(Long id, CategoriaEntity categoria){
        CategoriaEntity existente = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Categoria no existe: " + id));

        existente.setNombre(categoria.getNombre());

        return repository.save(existente);
    }

}
