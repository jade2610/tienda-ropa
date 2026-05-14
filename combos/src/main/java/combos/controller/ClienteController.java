package combos.controller;

import combos.model.Cliente;
import combos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // LISTAR CLIENTES
    @GetMapping
    public String listarClientes(Model model){

        model.addAttribute("lista",
                clienteService.listarClientes());

        return "clientes";
    }

    // MOSTRAR FORMULARIO
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model){

        model.addAttribute("cliente",
                new Cliente());

        return "registrarCliente";
    }

    // GUARDAR CLIENTE
    @PostMapping("/guardar")
    public String guardarCliente(Cliente cliente){

        clienteService.agregarCliente(cliente);

        return "redirect:/clientes";
    }

    // ELIMINAR CLIENTE
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable int id){

        clienteService.eliminarCliente(id);

        return "redirect:/clientes";
    }

    // EDITAR CLIENTE
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable int id, Model model){

        Cliente cliente = clienteService.buscarCliente(id);

        model.addAttribute("cliente", cliente);

        return "registrarCliente";
    }

}