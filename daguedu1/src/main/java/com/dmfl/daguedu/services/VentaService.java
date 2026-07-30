package com.dmfl.daguedu.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmfl.daguedu.modelo.ClienteEntity;
import com.dmfl.daguedu.modelo.DetalleVentaEntity;
import com.dmfl.daguedu.modelo.ProductoEntity;
import com.dmfl.daguedu.modelo.VentaEntity;
import com.dmfl.daguedu.repository.ClienteRepository;
import com.dmfl.daguedu.repository.DetalleVentaRepository;
import com.dmfl.daguedu.repository.ProductoRepository;
import com.dmfl.daguedu.repository.VentaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    
    //LEER TODOS LOS REGISTROS
    @Transactional(readOnly = true)
    public List<VentaEntity> obtenerTodos(){
        return repository.findAll();
    }

    //BUSCAR POR ID
    @Transactional(readOnly = true)
    public VentaEntity obtenerPorId(Long id){
        return repository.findById(id).orElseThrow(
            () -> new RuntimeException("Venta no encontrada: " + id));
    }
    //metodo para procesar venta 
    @Transactional
    public VentaEntity procesarVenta(VentaEntity ventaRequest, String email){
        ClienteEntity cliente = clienteRepository.findByEmail(email)
        .orElseThrow(()-> new RuntimeException("Cliente no registrado: "+ email));

        ventaRequest.setCliente(cliente);
        ventaRequest.setFecha(LocalDateTime.now() );
        ventaRequest.setEstadoPago("PENDIENTE");

           double total = 0;
        for(DetalleVentaEntity detalle : ventaRequest.getDetalles()){

            ProductoEntity producto = productoRepository.findById(detalle.getProducto().
            getId()).orElseThrow(() -> new RuntimeException("Producto no existe"));

            if(producto.getStock() < detalle.getCantidad()){
                throw new RuntimeException("Stock insuficiente del producto");
            }

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
            
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
            detalle.setVenta(ventaRequest);

            total += detalle.getSubtotal();


        }
        ventaRequest.setTotal(total);
        return repository.save(ventaRequest);

    }

    //metodo para procesar pago 
    @Transactional
    public VentaEntity confirmarPago(long idVenta){
        VentaEntity venta = repository.findById(idVenta)
        .orElseThrow(() -> new RuntimeException("VENTA NO ENCONTRADA CON ID: " + idVenta));
        venta.setEstadoPago("PAGADO");
        return repository.save(venta);
    }

    @Transactional
    public VentaEntity agregarProductosPendientes(Long idVenta, List<DetalleVentaEntity> detallesNuevos) {
        VentaEntity venta = repository.findById(idVenta)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + idVenta));

        if (!"PENDIENTE".equalsIgnoreCase(venta.getEstadoPago())) {
            throw new RuntimeException("Solo se pueden agregar productos a compras pendientes");
        }
        if (detallesNuevos == null || detallesNuevos.isEmpty()) {
            throw new RuntimeException("Selecciona al menos un producto");
        }

        double totalActualizado = venta.getTotal() == null ? 0 : venta.getTotal();
        for (DetalleVentaEntity detalle : detallesNuevos) {
            if (detalle.getProducto() == null || detalle.getProducto().getId() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new RuntimeException("Los productos agregados no son validos");
            }
            ProductoEntity producto = productoRepository.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no existe"));
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
            detalle.setVenta(venta);
            venta.getDetalles().add(detalle);
            totalActualizado += detalle.getSubtotal();
        }
        venta.setTotal(totalActualizado);
        return repository.save(venta);
    }

    @Transactional
    public VentaEntity eliminarProductoPendiente(Long idVenta, Long idDetalle) {
        VentaEntity venta = repository.findById(idVenta)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + idVenta));
        if (!"PENDIENTE".equalsIgnoreCase(venta.getEstadoPago())) {
            throw new RuntimeException("Solo se pueden modificar compras pendientes");
        }
        if (venta.getDetalles().size() <= 1) {
            throw new RuntimeException("La compra debe conservar al menos un producto");
        }

        DetalleVentaEntity detalle = detalleVentaRepository.findById(idDetalle)
            .orElseThrow(() -> new RuntimeException("Detalle de compra no encontrado"));
        if (detalle.getVenta() == null || !venta.getId().equals(detalle.getVenta().getId())) {
            throw new RuntimeException("El producto no pertenece a esta compra");
        }

        ProductoEntity producto = detalle.getProducto();
        producto.setStock(producto.getStock() + detalle.getCantidad());
        productoRepository.save(producto);

        venta.getDetalles().removeIf(item -> item.getId().equals(idDetalle));
        detalleVentaRepository.delete(detalle);
        double totalActualizado = venta.getDetalles().stream()
            .mapToDouble(item -> item.getSubtotal() == null ? 0 : item.getSubtotal())
            .sum();
        venta.setTotal(totalActualizado);
        return repository.save(venta);
    }
    


    //GUARDAR VENTA
    @Transactional
    public VentaEntity guardarVenta (VentaEntity venta){
        return repository.save(venta);
        //AQUI PUEDEN IR TODAS LAS VALIDACIONES
    }

    //ELIMINAR VENTA
    @Transactional(readOnly = true)
    public void eliminarVenta (Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Venta no encontrada: " + id);
        }
        repository.deleteById(id);
    }

    //ACTUALIZAR VENTA
    @Transactional
    public VentaEntity actualizarVenta (Long id, VentaEntity detalleVentaEntity){
        VentaEntity ventaExistente = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Venta no existe! : " + id));
        
        BeanUtils.copyProperties(detalleVentaEntity, ventaExistente, "id");

        return repository.save(ventaExistente);
    }

}
