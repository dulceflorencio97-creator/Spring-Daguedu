package com.dmfl.daguedu.dto;

public class PagoRequest {
    private long idVenta;
    private String moneda;
    public long getIdVenta() {
        return idVenta;
    }
    
    public PagoRequest(long idVenta, String moneda) {
        this.idVenta = idVenta;
        this.moneda = moneda;
    }

    public String getMoneda() {
        return moneda;
    }
    public void setIdVenta(long idVenta) {
        this.idVenta = idVenta;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}


    
        
    


