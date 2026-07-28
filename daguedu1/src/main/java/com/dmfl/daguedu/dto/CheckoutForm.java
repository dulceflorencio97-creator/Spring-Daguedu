package com.dmfl.daguedu.dto;

import java.util.ArrayList;
import java.util.List;

public class CheckoutForm {
    private String email;
    private List<Item> items = new ArrayList<>();

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public static class Item {
        private Long productoId;
        private Integer cantidad;
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}
