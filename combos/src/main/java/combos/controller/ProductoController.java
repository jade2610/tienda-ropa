package combos.controller;

import combos.model.Producto;
import combos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // LISTAR PRODUCTOS
    @GetMapping
    public String listarProductos(Model model) {

        model.addAttribute("lista",
                productoService.listarProductos());

        return "productos";
    }

    // MOSTRAR FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {

        model.addAttribute("producto",
                new Producto());

        return "registrarProducto";
    }

    // GUARDAR (CREAR + EDITAR)
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {

        productoService.agregarProducto(producto);

        return "redirect:/productos";
    }

    // ✏️ EDITAR PRODUCTO (ESTO TE FALTABA)
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable int id, Model model) {

        Producto producto = productoService.buscarPorId(id);

        model.addAttribute("producto", producto);

        return "registrarProducto";
    }

    // ❌ ELIMINAR PRODUCTO
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable int id) {

        productoService.eliminarProducto(id);

        return "redirect:/productos";
    }
}