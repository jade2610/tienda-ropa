package combos.controller;

import combos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;

    // 1. La Portada (Libre, sin cargar base de datos)
    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    // 2. El Catálogo (Va a MySQL, jala la ropa y la manda a la vista)
    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        model.addAttribute("listaProductos", productoService.listarTodos());
        return "cliente/catalogo"; // Busca el archivo en templates/cliente/catalogo.html
    }
}