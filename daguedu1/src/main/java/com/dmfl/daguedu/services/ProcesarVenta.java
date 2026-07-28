package com.dmfl.daguedu.services;

import org.springframework.stereotype.Service;

import com.dmfl.daguedu.modelo.DetalleVentaEntity;
import com.dmfl.daguedu.modelo.ProductoEntity;
import com.dmfl.daguedu.modelo.VentaEntity;
import com.dmfl.daguedu.repository.ProductoRepository;
import com.dmfl.daguedu.repository.VentaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProcesarVenta {
    private final VentaRepository ventarepo;
    private final ProductoRepository prodrepo;


    @Transactional
    public VentaEntity procesarVenta(VentaEntity ventaRequest){
        ventaRequest.setFecha(java.time.LocalDateTime.now());
        ventaRequest.setEstadoPago("pendiente");

        //calcular total 
        double total=0.0;
        for(DetalleVentaEntity detalle: ventaRequest.getDetalles()){
            ProductoEntity p=prodrepo.findById(detalle.getProducto().getId()).orElseThrow();
            p.setStock(p.getStock() - detalle.getCantidad());

            detalle.setPrecioUnitario(p.getPrecio());
            detalle.setSubtotal(p.getPrecio()*detalle.getCantidad());
            detalle.setVenta(ventaRequest);
            total += detalle.getSubtotal();
        }
        ventaRequest.setTotal(total);
        return ventarepo.save(ventaRequest);

    }

}
