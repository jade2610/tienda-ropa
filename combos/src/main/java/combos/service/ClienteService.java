package combos.service;

import combos.model.Cliente;
import combos.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente login(String email, String password) {
        Cliente c = clienteRepository.findByEmail(email);
        if (c != null && c.getPassword().equals(password)) {
            return c;
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public void guardar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    // --- ¡NUEVO! Permite buscar si el cliente ya compró antes ---
    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
}