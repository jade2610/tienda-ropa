package combos.controller;

import combos.model.Venta;
import combos.service.ClienteService;
import combos.service.ProductoService;
import combos.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Inyectamos los servicios para poder consultar la base de datos
    @Autowired private ProductoService productoService;
    @Autowired private VentaService ventaService;
    @Autowired private ClienteService clienteService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        if ("admin@jvxf.com".equals(email) && "admin123".equals(password)) {
            session.setAttribute("usuarioLogueado", "ADMIN");
            return "redirect:/admin/dashboard"; 
        }
        model.addAttribute("error", "Correo o contraseña incorrectos.");
        return "admin/login";
    }

    // AQUI ESTÁ LA MAGIA: El Dashboard ahora es dinámico
    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/admin/login";
        }

        // 1. Sumar todo el dinero de las ventas
        List<Venta> listaVentas = ventaService.listarTodas();
        double cajaTotal = 0;
        for (Venta v : listaVentas) {
            cajaTotal += v.getTotal();
        }

        // 2. Contar cuántos productos y clientes hay en la base de datos
        int totalPrendas = productoService.listarTodos().size();
        int totalClientes = clienteService.listarTodos().size();

        // 3. Enviar las variables reales al HTML
        model.addAttribute("cajaTotal", cajaTotal);
        model.addAttribute("totalPrendas", totalPrendas);
        model.addAttribute("totalClientes", totalClientes);

        return "admin/dashboard"; 
    }
}