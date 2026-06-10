package combos.controller;

import combos.model.Carrito;
import combos.model.Cliente;
import combos.model.Producto;
import combos.model.Venta;
import combos.service.ClienteService;
import combos.service.ProductoService;
import combos.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired private ProductoService productoService;
    @Autowired private VentaService ventaService;
    @Autowired private ClienteService clienteService;

    @GetMapping("/ver")
    public String verCarrito(HttpSession session, Model model) {
        List<Carrito> miCarrito = (List<Carrito>) session.getAttribute("carrito");
        if (miCarrito == null) {
            miCarrito = new ArrayList<>();
        }

        double total = 0;
        for (Carrito detalle : miCarrito) {
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        model.addAttribute("detalles", miCarrito);
        model.addAttribute("total", total);
        
        return "cliente/carrito"; 
    }

    @PostMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable("id") Long id, @RequestParam(defaultValue = "1") int cantidad, HttpSession session) {
        Producto producto = productoService.buscarPorId(id);

        if (producto != null) {
            List<Carrito> miCarrito = (List<Carrito>) session.getAttribute("carrito");
            if (miCarrito == null) {
                miCarrito = new ArrayList<>();
            }

            boolean existe = false;
            for (Carrito detalle : miCarrito) {
                if (detalle.getProducto().getId().equals(producto.getId())) {
                    detalle.setCantidad(detalle.getCantidad() + cantidad);
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                Carrito detalle = new Carrito();
                detalle.setProducto(producto);
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setCantidad(cantidad); 
                miCarrito.add(detalle);
            }

            session.setAttribute("carrito", miCarrito);
        }
        return "redirect:/catalogo"; 
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        List<Carrito> miCarrito = (List<Carrito>) session.getAttribute("carrito");
        
        if (miCarrito != null) {
            miCarrito.removeIf(detalle -> detalle.getProducto().getId().equals(id));
            session.setAttribute("carrito", miCarrito);
        }
        return "redirect:/carrito/ver"; 
    }

    @GetMapping("/checkout")
    public String mostrarCheckout(HttpSession session, Model model) {
        List<Carrito> miCarrito = (List<Carrito>) session.getAttribute("carrito");
        
        if (miCarrito == null || miCarrito.isEmpty()) {
            return "redirect:/catalogo";
        }

        double total = 0;
        for (Carrito detalle : miCarrito) {
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        model.addAttribute("detalles", miCarrito);
        model.addAttribute("total", total);
        
        return "cliente/checkout"; 
    }

    @PostMapping("/procesar")
    public String procesarCompra(@RequestParam String nombre, @RequestParam String email, 
                                 @RequestParam(defaultValue = "Tarjeta de Crédito/Débito") String metodoPago, 
                                 HttpSession session, Model model) {
        List<Carrito> miCarrito = (List<Carrito>) session.getAttribute("carrito");
        
        if (miCarrito == null || miCarrito.isEmpty()) {
            return "redirect:/catalogo";
        }

        double total = 0;
        for (Carrito detalle : miCarrito) {
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        // --- PROCESAMIENTO INTELIGENTE EN BASE DE DATOS ---
        
        // A. Buscamos si el cliente ya compró antes en nuestra tienda
        Cliente clienteActual = clienteService.buscarPorEmail(email);
        
        // Si es nulo, significa que es un cliente nuevo y lo registramos
        if (clienteActual == null) {
            clienteActual = new Cliente();
            clienteActual.setNombre(nombre);
            clienteActual.setEmail(email);
            clienteActual.setRol("CLIENTE");
            clienteService.guardar(clienteActual); 
        }

        // B. Guardamos la Venta asociada a ese cliente (viejo o nuevo)
        Venta nuevaVenta = new Venta();
        nuevaVenta.setTotal(total);
        nuevaVenta.setFecha(LocalDateTime.now()); 
        nuevaVenta.setCliente(clienteActual); 
        nuevaVenta.setMetodoPago(metodoPago); 
        ventaService.registrarVenta(nuevaVenta);
        // ----------------------------------------------------

        model.addAttribute("nombreCliente", nombre);
        model.addAttribute("emailCliente", email);
        model.addAttribute("detalles", miCarrito);
        model.addAttribute("total", total);
        model.addAttribute("numeroPedido", nuevaVenta.getId() != null ? nuevaVenta.getId() : (int) (Math.random() * 1000000));

        session.removeAttribute("carrito");
        
        return "cliente/voucher"; 
    }
}