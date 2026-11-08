package com.aracnidostore.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aracnidostore.model.Categoria;
import com.aracnidostore.model.Producto;
import com.aracnidostore.repository.CategoriaRepository;
import com.aracnidostore.repository.ProductoRepository;

/**
 * Carga datos de ejemplo la primera vez (si las tablas están vacías).
 * Se ejecuta al arrancar la app; si ya hay datos, no hace nada.
 */
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatosIniciales(CategoriaRepository categorias,
                                           ProductoRepository productos) {
        return args -> {
            if (categorias.count() > 0 || productos.count() > 0) {
                return;
            }
            Categoria trajes = categorias.save(new Categoria("Trajes"));
            Categoria gadgets = categorias.save(new Categoria("Gadgets"));
            Categoria coleccion = categorias.save(new Categoria("Coleccionables"));

            productos.saveAll(List.of(
                new Producto("Lanzarredes Mecánico", "Lanzarredes de muñeca con cartuchos recargables",
                        new BigDecimal("45000.00"), 15, null, gadgets),
                new Producto("Traje Arácnido Clásico", "Traje rojo y azul, talle ajustable",
                        new BigDecimal("89000.00"), 8, null, trajes),
                new Producto("Máscara Arácnida", "Máscara con lentes ajustables",
                        new BigDecimal("22000.00"), 20, null, trajes),
                new Producto("Cartucho de Telaraña x3", "Pack de 3 cartuchos de repuesto",
                        new BigDecimal("9500.00"), 3, null, gadgets),
                new Producto("Figura Coleccionable Arácnido", "Figura articulada de 30 cm",
                        new BigDecimal("35000.00"), 12, null, coleccion)
            ));
            System.out.println(">>> Datos de ejemplo cargados (3 categorías, 5 productos)");
        };
    }
}
