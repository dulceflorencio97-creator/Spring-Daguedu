package com.dmfl.daguedu.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dmfl.daguedu.modelo.ProductoEntity;
import com.dmfl.daguedu.repository.ProductoRepository;
import com.dmfl.daguedu.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repository;
    private final ProveedorRepository proveedorRepository;

    @Transactional(readOnly = true)
    public List<ProductoEntity> obtenerTodos(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ProductoEntity obtenerPorId(Long id){
        return repository.findById(id).orElseThrow(
            () -> new RuntimeException("Producto no encontrado: " + id));
    }

    @Transactional
    public ProductoEntity guardarProducto(ProductoEntity producto){
        asignarProveedor(producto);
        return repository.save(producto);
    }

    @Transactional
    public void eliminarProducto(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Producto no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public ProductoEntity actualizarProducto(Long id, ProductoEntity producto){
        ProductoEntity existente = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no existe: " + id));

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setImagenUrl(producto.getImagenUrl());
        existente.setCategoria(producto.getCategoria());
        existente.setProveedor(producto.getProveedor());
        asignarProveedor(existente);

        return repository.save(existente);
    }

    private void asignarProveedor(ProductoEntity producto) {
        if (producto.getProveedor() == null || producto.getProveedor().getId() == null) {
            throw new RuntimeException("Selecciona un proveedor para el producto");
        }
        producto.setProveedor(proveedorRepository.findById(producto.getProveedor().getId())
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado")));
    }

}
