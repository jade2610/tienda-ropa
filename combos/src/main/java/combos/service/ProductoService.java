package combos.service;

import combos.model.Producto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    private List<Producto> listaProductos = new ArrayList<>();

    // 🔥 DATOS INICIALES
    @PostConstruct
    public void cargarProductos() {

        listaProductos.add(new Producto(1, "Vestido Elegante", "Vestidos", "M", 120, 10,
                "https://images.unsplash.com/photo-1496747611176-843222e1e57c"));

        listaProductos.add(new Producto(2, "Chaqueta Urbana", "Chaquetas", "L", 180, 5,
                "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f"));

        listaProductos.add(new Producto(3, "Jeans Clásicos", "Pantalones", "M", 95, 15,
                "https://images.unsplash.com/photo-1541099649105-f69ad21f3246"));

        listaProductos.add(new Producto(4, "Blusa Chic", "Blusas", "S", 75, 8,
                "https://images.unsplash.com/photo-1521572267360-ee0c2909d518"));
    }

    // 📦 LISTAR
    public List<Producto> listarProductos() {
        return listaProductos;
    }

    // 💾 GUARDAR (CREAR + EDITAR)
    public void agregarProducto(Producto producto) {

        // 🔥 VALIDACIÓN: evitar null ID
        if (producto.getId() == 0) {
            producto.setId(generarId());
        }

        // ✏️ ACTUALIZAR SI EXISTE
        for (int i = 0; i < listaProductos.size(); i++) {

            if (listaProductos.get(i).getId() == producto.getId()) {

                listaProductos.set(i, producto); // UPDATE
                return;
            }
        }

        // ➕ SI NO EXISTE, INSERTAR
        listaProductos.add(producto);
    }

    // ❌ ELIMINAR
    public void eliminarProducto(int id) {
        listaProductos.removeIf(p -> p.getId() == id);
    }

    // 🔍 BUSCAR POR ID
    public Producto buscarPorId(int id) {

        for (Producto p : listaProductos) {
            if (p.getId() == id) {
                return p;
            }
        }

        return null;
    }

    // 🆔 GENERAR ID AUTOMÁTICO (MEJORA IMPORTANTE)
    private int generarId() {

        if (listaProductos.isEmpty()) {
            return 1;
        }

        return listaProductos.stream()
                .mapToInt(Producto::getId)
                .max()
                .getAsInt() + 1;
    }
}