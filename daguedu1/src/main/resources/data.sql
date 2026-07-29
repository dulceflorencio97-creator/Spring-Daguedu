-- Catalogo inicial para una instalacion nueva. INSERT IGNORE conserva los datos existentes.
INSERT IGNORE INTO categorias (id, nombre) VALUES
  (1, 'Electronica'), (2, 'Computo'), (3, 'Hogar'), (4, 'Accesorios'),
  (5, 'Moda'), (6, 'Deportes'), (7, 'Belleza'), (8, 'Supermercado');

INSERT IGNORE INTO proveedores (id, nombre, email, empresa, telefono) VALUES
  (1, 'Andrea Lopez', 'contacto@tecnologiamx.com', 'Tecnologia MX', '5512345678'),
  (2, 'Carlos Ruiz', 'ventas@hogarymas.com', 'Hogar y Mas', '5587654321'),
  (3, 'Sofia Hernandez', 'sofia@modaurbana.com', 'Moda Urbana', '5545678912'),
  (4, 'Miguel Torres', 'ventas@deportesactivos.com', 'Deportes Activos', '5598761234');

INSERT IGNORE INTO productos (id, descripcion, imagen_url, nombre, precio, stock, categoria_id, proveedor_id) VALUES
  (1, 'Laptop para estudio y trabajo con pantalla de 15.6 pulgadas.', 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&q=80&w=800', 'Laptop Lenovo IdeaPad', 13000, 6, 2, 1),
  (2, 'Tablet ligera para entretenimiento, clases y tareas diarias.', 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&q=80&w=900', 'Tablet Samsung Galaxy Tab', 6499, 11, 1, 1),
  (3, 'Smartphone con camara de alta resolucion y bateria de larga duracion.', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&q=80&w=800', 'Celular Samsung Galaxy', 8999, 14, 1, 1),
  (4, 'Audifonos inalambricos con estuche de carga y sonido envolvente.', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&q=80&w=800', 'Audifonos Bluetooth', 899, 24, 4, 1),
  (5, 'Reloj deportivo con monitor de actividad y notificaciones.', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&q=80&w=800', 'Reloj Inteligente', 1599, 18, 4, 1),
  (6, 'Cafetera para preparar cafe fresco en casa cada manana.', 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&q=80&w=800', 'Cafetera Programable', 1199, 10, 3, 2),
  (7, 'Aspiradora practica para la limpieza diaria del hogar.', 'https://images.unsplash.com/photo-1558317374-067fb5f30001?auto=format&fit=crop&q=80&w=800', 'Aspiradora Compacta', 2499, 7, 3, 2),
  (8, 'Mochila acolchada con compartimento seguro para laptop.', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&q=80&w=800', 'Mochila para Laptop', 749, 20, 4, 1),
  (9, 'Tenis ligeros y comodos para uso diario y entrenamiento.', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&q=80&w=900', 'Tenis Deportivos Urbanos', 1299, 14, 6, 4),
  (10, 'Mochila resistente con espacio para ropa, botella y accesorios.', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&q=80&w=900', 'Mochila Deportiva', 799, 16, 6, 4),
  (11, 'Balon profesional para entrenamientos y partidos.', 'https://images.unsplash.com/photo-1614632537190-23e4146777db?auto=format&fit=crop&q=80&w=900', 'Balon de Futbol', 549, 9, 6, 4),
  (12, 'Sudadera unisex de algodon ideal para clima fresco.', 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?auto=format&fit=crop&q=80&w=900', 'Sudadera Casual', 699, 18, 5, 3),
  (13, 'Bolso practico con diseno moderno para uso diario.', 'https://images.unsplash.com/photo-1584917865442-de89df76afd3?auto=format&fit=crop&q=80&w=900', 'Bolso de Mano', 899, 11, 5, 3),
  (14, 'Playera de algodon de corte comodo y diseno versatil.', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&q=80&w=900', 'Playera Basica', 299, 30, 5, 3),
  (15, 'Teclado con iluminacion para estudio, trabajo y videojuegos.', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&q=80&w=900', 'Teclado Mecanico', 1399, 6, 2, 1),
  (16, 'Lampara LED ajustable para lectura y espacios de trabajo.', 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&q=80&w=900', 'Lampara de Escritorio', 459, 12, 3, 2),
  (17, 'Kit de maquillaje con sombras y labiales para uso diario.', 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&q=80&w=800', 'Set de Maquillaje', 649, 13, 7, 3),
  (18, 'Crema facial de uso diario para una piel suave.', 'https://images.unsplash.com/photo-1556229010-6c3f2c9ca5f8?auto=format&fit=crop&q=80&w=800', 'Crema Hidratante', 259, 22, 7, 3),
  (19, 'Cafe tostado de aroma intenso para preparar en casa.', 'https://images.unsplash.com/photo-1447933601403-0c6688de566e?auto=format&fit=crop&q=80&w=900', 'Cafe Molido Premium', 189, 25, 8, 2),
  (20, 'Seleccion de botanas para compartir en cualquier momento.', 'https://images.unsplash.com/photo-1582058091505-f87a2e55a40f?auto=format&fit=crop&q=80&w=900', 'Paquete de Snacks', 159, 20, 8, 2);
