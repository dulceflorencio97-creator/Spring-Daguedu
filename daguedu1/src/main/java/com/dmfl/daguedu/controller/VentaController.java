package com.dmfl.daguedu.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmfl.daguedu.modelo.VentaEntity;
import com.dmfl.daguedu.modelo.DetalleVentaEntity;
import com.dmfl.daguedu.modelo.ProductoEntity;
import com.dmfl.daguedu.dto.CheckoutForm;
import com.dmfl.daguedu.services.VentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/venta") //MAPEO GENERAL VENTAS
@CrossOrigin(origins = "https://localhost:5173") //PERMISO A REACT
@RequiredArgsConstructor
public class VentaController {

    private final VentaService servicio;

    @GetMapping("/")
    public ResponseEntity<List<VentaEntity>> listar() {
        return ResponseEntity.ok(servicio.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaEntity> obtenerDetallesEntity(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    //eliminar por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try {
            servicio.eliminarVenta(id);
            return ResponseEntity.noContent().build(); //204
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //AGREGAR super revisar de favor ------------------------------------------------------------------------
    @PostMapping("/")
    public ResponseEntity<VentaEntity> crear(@RequestBody VentaEntity venta) {
        VentaEntity nuevo = servicio.guardarVenta(venta);
        return new ResponseEntity<> (nuevo, HttpStatus.CREATED);//201 CREATED

    }

    @PostMapping("/procesar")
    public ResponseEntity<?> procesar(@RequestBody VentaEntity venta, @RequestParam String email) {
        try {
            return new ResponseEntity<>(servicio.procesarVenta(venta, email), HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/{id}/agregar-productos")
    public ResponseEntity<?> agregarProductos(@PathVariable Long id, @RequestBody VentaEntity venta) {
        try {
            return ResponseEntity.ok(servicio.agregarProductosPendientes(id, venta.getDetalles()));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{idVenta}/detalles/{idDetalle}")
    public ResponseEntity<?> eliminarProductoPendiente(@PathVariable Long idVenta, @PathVariable Long idDetalle) {
        try {
            return ResponseEntity.ok(servicio.eliminarProductoPendiente(idVenta, idDetalle));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutForm formulario) {
        try {
            if (formulario.getEmail() == null || formulario.getItems() == null || formulario.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body("El carrito y el correo son obligatorios");
            }

            VentaEntity venta = new VentaEntity();
            for (CheckoutForm.Item item : formulario.getItems()) {
                if (item.getProductoId() == null || item.getCantidad() == null || item.getCantidad() <= 0) {
                    return ResponseEntity.badRequest().body("Los productos del carrito no son válidos");
                }
                ProductoEntity producto = new ProductoEntity();
                producto.setId(item.getProductoId());
                DetalleVentaEntity detalle = new DetalleVentaEntity();
                detalle.setProducto(producto);
                detalle.setCantidad(item.getCantidad());
                venta.getDetalles().add(detalle);
            }
            return new ResponseEntity<>(servicio.procesarVenta(venta, formulario.getEmail()), HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    //ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody VentaEntity producto) {
        try{
        VentaEntity ProductoAct = servicio.actualizarVenta(id, producto);
        return ResponseEntity.ok(ProductoAct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
