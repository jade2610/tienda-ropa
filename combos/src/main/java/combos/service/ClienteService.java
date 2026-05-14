package combos.service;

import combos.model.Cliente;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private List<Cliente> listaClientes = new ArrayList<>();

    // DATOS DE PRUEBA
    @PostConstruct
    public void cargarClientes(){

        listaClientes.add(new Cliente(
                1,
                "María López",
                "maria@gmail.com",
                "987654321",
                "Lima"
        ));

        listaClientes.add(new Cliente(
                2,
                "Carlos Pérez",
                "carlos@gmail.com",
                "999888777",
                "Arequipa"
        ));

    }

    // LISTAR
    public List<Cliente> listarClientes(){

        return listaClientes;
    }

    // AGREGAR Y EDITAR
    public void agregarCliente(Cliente cliente){

        if(cliente.getId() == 0){

            cliente.setId(listaClientes.size() + 1);

            listaClientes.add(cliente);

        } else {

            for(int i = 0; i < listaClientes.size(); i++){

                if(listaClientes.get(i).getId() == cliente.getId()){

                    listaClientes.set(i, cliente);

                    break;
                }
            }

        }

    }

    // ELIMINAR
    public void eliminarCliente(int id){

        listaClientes.removeIf(cliente ->
                cliente.getId() == id);
    }

    // BUSCAR POR ID
    public Cliente buscarCliente(int id){

        for(Cliente c : listaClientes){

            if(c.getId() == id){

                return c;
            }
        }

        return null;
    }

}