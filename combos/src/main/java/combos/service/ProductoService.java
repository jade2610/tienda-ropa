package combos.service;

import combos.model.Producto;
import combos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() { return productoRepository.findAll(); }
    public Producto buscarPorId(Long id) { return productoRepository.findById(id).orElse(null); }
    public void guardar(Producto p) { productoRepository.save(p); }
    public void eliminar(Long id) { productoRepository.deleteById(id); }
}