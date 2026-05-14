package combos.controller;

import combos.model.Carrito;
import combos.model.Producto;
import combos.model.Venta;

import combos.service.CarritoService;
import combos.service.ProductoService;
import combos.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    // MOSTRAR CARRITO
    @GetMapping
    public String verCarrito(Model model) {

        model.addAttribute("listaCarrito",
                carritoService.listarCarrito());

        model.addAttribute("total",
                carritoService.calcularTotal());

        return "carrito";
    }

    // AGREGAR PRODUCTO
    @GetMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable int id) {

        Producto producto =
                productoService.buscarPorId(id);

        if(producto != null){

            List<Carrito> lista =
                    carritoService.listarCarrito();

            boolean existe = false;

            for(Carrito item : lista){

                if(item.getId() == producto.getId()){

                    item.setCantidad(
                            item.getCantidad() + 1
                    );

                    item.setSubtotal(
                            item.getCantidad() *
                            producto.getPrecio()
                    );

                    existe = true;
                    break;
                }
            }

            if(!existe){

                Carrito item = new Carrito();

                item.setId(producto.getId());
                item.setProducto(producto.getNombre());
                item.setCantidad(1);
                item.setSubtotal(producto.getPrecio());

                carritoService.agregarCarrito(item);
            }
        }

        return "redirect:/carrito";
    }

    // ELIMINAR PRODUCTO
    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable int id){

        carritoService.eliminarCarrito(id);

        return "redirect:/carrito";
    }

    // FINALIZAR COMPRA
    @GetMapping("/finalizar")
    public String finalizarCompra(){

        List<Carrito> carrito =
                carritoService.listarCarrito();

        int idVenta =
                ventaService.listarVentas().size() + 1;

        for(Carrito item : carrito){

            Venta venta = new Venta(
                    idVenta,
                    "Cliente General",
                    item.getProducto(),
                    item.getCantidad(),
                    item.getSubtotal(),
                    LocalDate.now().toString()
            );

            ventaService.agregarVenta(venta);

            idVenta++;
        }

        carrito.clear();

        return "redirect:/ventas";
    }

}