package combos.controller;

import combos.service.VentaService; // Asegúrate de tener este servicio creado
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ventas")
public class VentaAdminController {

    @Autowired
    private VentaService ventaService;

    // Mostrar el historial maestro de ventas
    @GetMapping
    public String listarVentas(HttpSession session, Model model) {
        // Barrera de seguridad
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/admin/login";
        }
        
        // Enviamos la lista de comprobantes a la vista
        model.addAttribute("listaVentas", ventaService.listarTodas());
        return "admin/ventas"; 
    }
}