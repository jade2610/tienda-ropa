package combos.service;

import combos.model.Carrito;
import combos.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    public List<Carrito> obtenerDetallesDeVenta(Long ventaId) {
        return carritoRepository.findByVentaId(ventaId);
    }
}