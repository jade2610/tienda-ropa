package combos.controller;

import combos.model.Venta;

import combos.service.ClienteService;
import combos.service.ProductoService;
import combos.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public String mostrarDashboard(Model model){

        // TOTAL PRODUCTOS
        int totalProductos =
                productoService.listarProductos().size();

        // TOTAL CLIENTES
        int totalClientes =
                clienteService.listarClientes().size();

        // TOTAL VENTAS
        int totalVentas =
                ventaService.listarVentas().size();

        // INGRESOS TOTALES
        double ingresosTotales = 0;

        for(Venta venta : ventaService.listarVentas()){

            ingresosTotales += venta.getTotal();

        }

        // ENVIAR DATOS
        model.addAttribute("totalProductos",
                totalProductos);

        model.addAttribute("totalClientes",
                totalClientes);

        model.addAttribute("totalVentas",
                totalVentas);

        model.addAttribute("ingresosTotales",
                ingresosTotales);

        return "dashboard";
    }

}