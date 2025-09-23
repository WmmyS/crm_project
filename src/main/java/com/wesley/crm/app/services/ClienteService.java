package com.wesley.crm.app.services;

import com.wesley.crm.domain.entities.Cliente;
import com.wesley.crm.domain.entities.Empresa;
import com.wesley.crm.app.models.dtos.cliente.ClienteRequestDTO;
import com.wesley.crm.app.models.dtos.cliente.ClienteResponseDTO;
import com.wesley.crm.infra.database.ClienteRepository;
import com.wesley.crm.infra.database.EmpresaRepository;
import com.wesley.crm.exceptions.CrmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Métodos de consulta
    public Page<ClienteDTO> listarTodos(Pageable pageable) {
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        List<ClienteDTO> clienteDTOs = clientes.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(clienteDTOs, pageable, clientes.getTotalElements());
    }

    public Optional<ClienteDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<ClienteDTO> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    public Page<ClienteDTO> buscarPorTermo(String termo, Pageable pageable) {
        Page<Cliente> clientes = clienteRepository.buscarPorTermo(termo, pageable);
        List<ClienteDTO> clienteDTOs = clientes.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(clienteDTOs, pageable, clientes.getTotalElements());
    }

    public List<ClienteDTO> listarPorStatus(Cliente.StatusCliente status) {
        List<Cliente> clientes = clienteRepository.findByStatus(status);
        return clientes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ClienteDTO> listarPorEmpresa(Long empresaId) {
        List<Cliente> clientes = clienteRepository.findByEmpresaId(empresaId);
        return clientes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Métodos de modificação
    public Cliente criar(Cliente cliente) {
        validarCliente(cliente);
        validarEmailUnico(cliente.getEmail(), null);
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    validarCliente(clienteAtualizado);
                    validarEmailUnico(clienteAtualizado.getEmail(), id);
                    
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setEmail(clienteAtualizado.getEmail());
                    cliente.setTelefone(clienteAtualizado.getTelefone());
                    cliente.setCpf(clienteAtualizado.getCpf());
                    cliente.setDataNascimento(clienteAtualizado.getDataNascimento());
                    cliente.setEndereco(clienteAtualizado.getEndereco());
                    cliente.setCidade(clienteAtualizado.getCidade());
                    cliente.setEstado(clienteAtualizado.getEstado());
                    cliente.setCep(clienteAtualizado.getCep());
                    cliente.setStatus(clienteAtualizado.getStatus());
                    cliente.setEmpresa(clienteAtualizado.getEmpresa());
                    
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new CrmException("Cliente não encontrado com ID: " + id));
    }

    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new CrmException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    public boolean existePorId(Long id) {
        return clienteRepository.existsById(id);
    }

    // Métodos de relatórios
    public Long contarPorStatus(Cliente.StatusCliente status) {
        return clienteRepository.countByStatus(status);
    }

    public List<Object[]> clientesPorEstado() {
        return clienteRepository.countClientesPorEstado();
    }

    // Validações de regras de negócio
    private void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new CrmException("Nome é obrigatório");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new CrmException("Email é obrigatório");
        }
    }

    private void validarEmailUnico(String email, Long clienteId) {
        Optional<Cliente> clienteExistente = clienteRepository.findByEmail(email);
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(clienteId)) {
            throw new CrmException("Email já cadastrado para outro cliente");
        }
    }

    // Método para converter Cliente para ClienteDTO
    private ClienteDTO convertToDTO(Cliente cliente) {
        return new ClienteDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getCpf(),
            cliente.getDataNascimento(),
            cliente.getEndereco(),
            cliente.getCidade(),
            cliente.getEstado(),
            cliente.getCep(),
            cliente.getStatus() != null ? cliente.getStatus().toString() : null,
            cliente.getDataCriacao(),
            cliente.getDataAtualizacao(),
            cliente.getEmpresa() != null ? cliente.getEmpresa().getId() : null,
            cliente.getEmpresa() != null ? cliente.getEmpresa().getNome() : null
        );
    }
}