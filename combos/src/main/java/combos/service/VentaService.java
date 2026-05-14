package combos.service;

import combos.model.Venta;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private List<Venta> listaVentas = new ArrayList<>();

    // LISTAR VENTAS
    public List<Venta> listarVentas(){
        return listaVentas;
    }

    // AGREGAR VENTA
    public void agregarVenta(Venta venta){
        listaVentas.add(venta);
    }

}