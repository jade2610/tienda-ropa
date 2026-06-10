package combos.controller;

import combos.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clientes")
public class ClienteAdminController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(HttpSession session, Model model) {
        // La barrera de seguridad de siempre
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/admin/login";
        }
        
        // Enviamos la lista de clientes a la vista
        model.addAttribute("listaClientes", clienteService.listarTodos());
        return "admin/clientes"; 
    }
}
