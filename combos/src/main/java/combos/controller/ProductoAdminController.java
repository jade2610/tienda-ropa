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

    // --- EL MÉTODO ACTUALIZADO ---
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, 
                                  @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                                  @RequestParam(value = "imagenUrlTexto", required = false) String imagenUrlTexto,
                                  HttpSession session) {
        
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/admin/login";

        try {
            // 1. Buscamos si la prenda ya existía antes en la base de datos
            Producto productoExistente = null;
            if (producto.getId() != null) {
                productoExistente = productoService.buscarPorId(producto.getId());
            }

            boolean nuevaImagenSubida = false;

            // OPCIÓN A: Subió un archivo NUEVO desde su PC
            if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
                Path directorioImagenes = Paths.get("uploads");
                if (!Files.exists(directorioImagenes)) {
                    Files.createDirectories(directorioImagenes);
                }
                String nombreArchivo = imagenArchivo.getOriginalFilename().replace(" ", "_");
                Path rutaCompleta = directorioImagenes.resolve(nombreArchivo);
                Files.copy(imagenArchivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);
                
                producto.setImagenUrl("/uploads/" + nombreArchivo);
                nuevaImagenSubida = true;

            // OPCIÓN B: Pegó un enlace NUEVO de internet
            } else if (imagenUrlTexto != null && !imagenUrlTexto.trim().isEmpty()) {
                producto.setImagenUrl(imagenUrlTexto);
                nuevaImagenSubida = true;
            }

            // OPCIÓN C: No tocó las imágenes
            if (!nuevaImagenSubida) {
                if (productoExistente != null) {
                    // Si estamos EDITANDO, le devolvemos la imagen que ya tenía guardada
                    producto.setImagenUrl(productoExistente.getImagenUrl());
                } else {
                    // Si estamos CREANDO y no puso foto, ponemos la de por defecto
                    producto.setImagenUrl("https://placehold.co/400x500?text=Sube+una+Foto");
                }
            }

            // Guardamos (Spring Boot actualiza automáticamente si detecta que hay un ID)
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