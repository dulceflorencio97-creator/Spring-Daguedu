package com.dmfl.daguedu.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dmfl.daguedu.modelo.ClienteEntity;
import com.dmfl.daguedu.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional(readOnly = true)
    public List<ClienteEntity> obtenerTodos(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ClienteEntity obtenerPorId(Long id){
        return repository.findById(id).orElseThrow(
            () -> new RuntimeException("Cliente no encontrado: " + id));
    }

    @Transactional
    public ClienteEntity guardarCliente(ClienteEntity cliente){
        return repository.save(cliente);
    }

    @Transactional
    public void eliminarCliente(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Cliente no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public ClienteEntity actualizarCliente(Long id, ClienteEntity cliente){
        ClienteEntity existente = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cliente no existe: " + id));

        existente.setNombre(cliente.getNombre());
        existente.setEmail(cliente.getEmail());
        existente.setDireccion(cliente.getDireccion());
        existente.setTelefono(cliente.getTelefono());

        return repository.save(existente);
    }

}
