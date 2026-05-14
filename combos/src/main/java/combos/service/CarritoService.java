package combos.service;

import combos.model.Carrito;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {

    private List<Carrito> listaCarrito = new ArrayList<>();

    // AGREGAR AL CARRITO
    public void agregarCarrito(Carrito nuevoItem){

        // VERIFICAR SI YA EXISTE
        for(Carrito item : listaCarrito){

            // SI EL PRODUCTO YA ESTÁ
            if(item.getProducto().equals(nuevoItem.getProducto())){

                // AUMENTAR CANTIDAD
                item.setCantidad(item.getCantidad() + 1);

                // ACTUALIZAR SUBTOTAL
                item.setSubtotal(
                        item.getSubtotal() + nuevoItem.getSubtotal()
                );

                return;
            }
        }

        // SI NO EXISTE → AGREGAR NUEVO
        listaCarrito.add(nuevoItem);
    }

    // LISTAR
    public List<Carrito> listarCarrito(){

        return listaCarrito;
    }

    // ELIMINAR
    public void eliminarCarrito(int id){

        listaCarrito.removeIf(c -> c.getId() == id);
    }

    // TOTAL GENERAL
    public double calcularTotal(){

        double total = 0;

        for(Carrito item : listaCarrito){

            total += item.getSubtotal();
        }

        return total;
    }
}