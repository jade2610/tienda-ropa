package combos.controller;

import combos.model.Producto;
import combos.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/productos")
public class ProductoAdminController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String listarProductos(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/admin/login";
        model.addAttribute("listaProductos", productoService.listarTodos());
        return "admin/productos"; 
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/admin/login";
        model.addAttribute("producto", new Producto());
        return "admin/registrarProducto";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/admin/login";
        model.addAttribute("producto", productoService.buscarPorId(id));
        return "admin/registrarProducto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, 
                                  @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                                  @RequestParam(value = "imagenUrlTexto", required = false) String imagenUrlTexto,
                                  @RequestParam(value = "tipoImagen", required = false) String tipoImagen,
                                  HttpSession session) {
        
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/admin/login";

        try {
            // Lógica para procesar la imagen (Archivo o URL)
            if ("btnArchivo".equals(tipoImagen) && imagenArchivo != null && !imagenArchivo.isEmpty()) {
                Path directorio = Paths.get("uploads"); // Carpeta en la raíz del proyecto
                if (!Files.exists(directorio)) Files.createDirectories(directorio);
                
                String nombreUnico = UUID.randomUUID().toString() + "_" + imagenArchivo.getOriginalFilename().replace(" ", "_");
                Files.copy(imagenArchivo.getInputStream(), directorio.resolve(nombreUnico), StandardCopyOption.REPLACE_EXISTING);
                
                producto.setImagenUrl("/uploads/" + nombreUnico);
            } 
            else if ("btnUrl".equals(tipoImagen) && imagenUrlTexto != null && !imagenUrlTexto.isEmpty()) {
                producto.setImagenUrl(imagenUrlTexto);
            }
            // Si el producto es nuevo y no tiene imagen, ponemos un placeholder
            else if (producto.getId() == null && (producto.getImagenUrl() == null || producto.getImagenUrl().isEmpty())) {
                producto.setImagenUrl("https://placehold.co/400x500?text=Sin+Foto");
            }

            productoService.guardar(producto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/productos"; 
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/admin/login";
        productoService.eliminar(id);
        return "redirect:/productos";
    }
}