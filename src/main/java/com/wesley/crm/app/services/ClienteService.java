package com.wesley.crm.app.services;

import com.wesley.crm.domain.entities.Cliente;
import com.wesley.crm.app.models.dtos.cliente.ClienteDTO;
import com.wesley.crm.app.models.dtos.cliente.ClienteRequestDTO;
import com.wesley.crm.domain.entities.Empresa;
import com.wesley.crm.infra.database.EmpresaRepository;
import com.wesley.crm.infra.database.ClienteRepository;
import com.wesley.crm.app.services.FileUploadService;
import com.wesley.crm.app.utils.CepUtils;
import com.wesley.crm.exceptions.CrmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private FileUploadService fileUploadService;

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
    public ClienteDTO criar(ClienteRequestDTO clienteRequest) {
        validarClienteRequest(clienteRequest);
        validarEmailUnico(clienteRequest.getEmail(), null);

        Cliente cliente = convertFromDTO(clienteRequest);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return convertToDTO(clienteSalvo);
    }

    public ClienteDTO atualizar(Long id, ClienteRequestDTO clienteRequest) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    validarClienteRequest(clienteRequest);
                    validarEmailUnico(clienteRequest.getEmail(), id);

                    cliente.setNome(clienteRequest.getNome());
                    cliente.setEmail(clienteRequest.getEmail());
                    cliente.setTelefone(clienteRequest.getTelefone());
                    cliente.setEndereco(clienteRequest.getEndereco());
                    cliente.setCidade(clienteRequest.getCidade());
                    cliente.setEstado(clienteRequest.getEstado());
                    cliente.setCep(CepUtils.formatCep(clienteRequest.getCep()));

                    if (clienteRequest.getEmpresaId() != null) {
                        Empresa empresa = empresaRepository.findById(clienteRequest.getEmpresaId())
                                .orElseThrow(() -> new CrmException(
                                        "Empresa não encontrada com ID: " + clienteRequest.getEmpresaId()));
                        cliente.setEmpresa(empresa);
                    } else {
                        cliente.setEmpresa(null);
                    }

                    Cliente clienteSalvo = clienteRepository.save(cliente);
                    return convertToDTO(clienteSalvo);
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

    // Métodos de imagem
    public ClienteDTO uploadImagem(Long id, MultipartFile file) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new CrmException("Cliente não encontrado com ID: " + id));

        // Remover imagem anterior se existir
        if (cliente.getImagemUrl() != null) {
            fileUploadService.deleteImage(cliente.getImagemUrl());
        }

        // Upload da nova imagem
        String imageUrl = fileUploadService.uploadImage(file);
        cliente.setImagemUrl(imageUrl);

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return convertToDTO(clienteSalvo);
    }

    public ClienteDTO removerImagem(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new CrmException("Cliente não encontrado com ID: " + id));

        if (cliente.getImagemUrl() != null) {
            fileUploadService.deleteImage(cliente.getImagemUrl());
            cliente.setImagemUrl(null);
            Cliente clienteSalvo = clienteRepository.save(cliente);
            return convertToDTO(clienteSalvo);
        }

        return convertToDTO(cliente);
    }

    public ClienteDTO criarComImagem(ClienteRequestDTO clienteRequest, MultipartFile imagem) {
        validarClienteRequest(clienteRequest);
        validarEmailUnico(clienteRequest.getEmail(), null);
        
        Cliente cliente = convertFromDTO(clienteRequest);
        
        // Se tem imagem, fazer upload
        if (imagem != null && !imagem.isEmpty()) {
            String imageUrl = fileUploadService.uploadImage(imagem);
            cliente.setImagemUrl(imageUrl);
        }
        
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return convertToDTO(clienteSalvo);
    }

    // Validações de regras de negócio
    private void validarClienteRequest(ClienteRequestDTO clienteRequest) {
        if (clienteRequest.getNome() == null || clienteRequest.getNome().trim().isEmpty()) {
            throw new CrmException("Nome é obrigatório");
        }
        if (clienteRequest.getEmail() == null || clienteRequest.getEmail().trim().isEmpty()) {
            throw new CrmException("Email é obrigatório");
        }
    }

    private void validarEmailUnico(String email, Long clienteId) {
        Optional<Cliente> clienteExistente = clienteRepository.findByEmail(email);
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(clienteId)) {
            throw new CrmException("Email já cadastrado para outro cliente");
        }
    }

    // Métodos de conversão
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
                cliente.getEmpresa() != null ? cliente.getEmpresa().getNome() : null,
                cliente.getImagemUrl());
    }

    private Cliente convertFromDTO(ClienteRequestDTO clienteRequest) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequest.getNome());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setTelefone(clienteRequest.getTelefone());
        cliente.setEndereco(clienteRequest.getEndereco());
        cliente.setCidade(clienteRequest.getCidade());
        cliente.setEstado(clienteRequest.getEstado());
        cliente.setCep(CepUtils.formatCep(clienteRequest.getCep()));
        cliente.setStatus(Cliente.StatusCliente.ATIVO); // Status padrão

        if (clienteRequest.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(clienteRequest.getEmpresaId())
                    .orElseThrow(
                            () -> new CrmException("Empresa não encontrada com ID: " + clienteRequest.getEmpresaId()));
            cliente.setEmpresa(empresa);
        }

        return cliente;
    }
}