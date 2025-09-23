package com.wesley.crm.app.services;

import com.wesley.crm.domain.entities.Contato;
import com.wesley.crm.domain.entities.Cliente;
import com.wesley.crm.app.models.dtos.contato.ContatoRequestDTO;
import com.wesley.crm.app.models.dtos.contato.ContatoResponseDTO;
import com.wesley.crm.infra.database.ContatoRepository;
import com.wesley.crm.infra.database.ClienteRepository;
import com.wesley.crm.exceptions.CrmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    // Métodos de consulta
    public Page<ContatoDTO> listarTodos(Pageable pageable) {
        Page<Contato> contatos = contatoRepository.findAll(pageable);
        List<ContatoDTO> contatoDTOs = contatos.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(contatoDTOs, pageable, contatos.getTotalElements());
    }

    public Optional<ContatoDTO> buscarPorId(Long id) {
        return contatoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<ContatoDTO> listarPorCliente(Long clienteId) {
        List<Contato> contatos = contatoRepository.findByClienteId(clienteId);
        return contatos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContatoDTO> listarPorTipo(Contato.TipoContato tipo) {
        List<Contato> contatos = contatoRepository.findByTipo(tipo);
        return contatos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContatoDTO> listarPorStatus(Contato.StatusContato status) {
        List<Contato> contatos = contatoRepository.findByStatus(status);
        return contatos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContatoDTO> listarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Contato> contatos = contatoRepository.findByDataContatoBetween(dataInicio, dataFim);
        return contatos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Métodos de modificação
    public Contato criar(Contato contato) {
        validarContato(contato);
        validarClienteExiste(contato.getCliente().getId());
        return contatoRepository.save(contato);
    }

    public Contato atualizar(Long id, Contato contatoAtualizado) {
        return contatoRepository.findById(id)
                .map(contato -> {
                    validarContato(contatoAtualizado);
                    
                    if (contatoAtualizado.getCliente() != null && 
                        !contato.getCliente().getId().equals(contatoAtualizado.getCliente().getId())) {
                        validarClienteExiste(contatoAtualizado.getCliente().getId());
                        contato.setCliente(contatoAtualizado.getCliente());
                    }
                    
                    contato.setTipo(contatoAtualizado.getTipo());
                    contato.setAssunto(contatoAtualizado.getAssunto());
                    contato.setDescricao(contatoAtualizado.getDescricao());
                    contato.setDataContato(contatoAtualizado.getDataContato());
                    contato.setStatus(contatoAtualizado.getStatus());

                    return contatoRepository.save(contato);
                })
                .orElseThrow(() -> new CrmException("Contato não encontrado com ID: " + id));
    }

    public void deletar(Long id) {
        if (!contatoRepository.existsById(id)) {
            throw new CrmException("Contato não encontrado com ID: " + id);
        }
        contatoRepository.deleteById(id);
    }

    public boolean existePorId(Long id) {
        return contatoRepository.existsById(id);
    }

    // Métodos de relatórios
    public Long contarPorTipo(Contato.TipoContato tipo) {
        return contatoRepository.countByTipo(tipo);
    }

    public Long contarPorStatus(Contato.StatusContato status) {
        return contatoRepository.countByStatus(status);
    }

    public Long contarPorCliente(Long clienteId) {
        return contatoRepository.countByClienteId(clienteId);
    }

    public List<Object[]> contatosPorTipo() {
        return contatoRepository.countContatosPorTipo();
    }

    public List<Object[]> contatosPorMes() {
        return contatoRepository.countContatosPorMes();
    }

    // Métodos de validação
    private void validarContato(Contato contato) {
        if (contato.getCliente() == null || contato.getCliente().getId() == null) {
            throw new CrmException("Cliente é obrigatório para o contato");
        }
        
        if (contato.getTipo() == null) {
            throw new CrmException("Tipo de contato é obrigatório");
        }
        
        if (contato.getAssunto() == null || contato.getAssunto().trim().isEmpty()) {
            throw new CrmException("Assunto do contato é obrigatório");
        }
        
        if (contato.getDataContato() == null) {
            throw new CrmException("Data do contato é obrigatória");
        }
        
        // Validar se a data do contato não é muito futura
        if (contato.getDataContato().isAfter(LocalDateTime.now().plusYears(1))) {
            throw new CrmException("Data do contato não pode ser superior a 1 ano no futuro");
        }
    }

    private void validarClienteExiste(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new CrmException("Cliente não encontrado com ID: " + clienteId);
        }
    }

    // Método para converter Contato para ContatoDTO
    private ContatoDTO convertToDTO(Contato contato) {
        return new ContatoDTO(
                contato.getId(),
                contato.getCliente() != null ? contato.getCliente().getId() : null,
                contato.getCliente() != null ? contato.getCliente().getNome() : null,
                contato.getTipo() != null ? contato.getTipo().toString() : null,
                contato.getAssunto(),
                contato.getDescricao(),
                contato.getDataContato(),
                contato.getStatus() != null ? contato.getStatus().toString() : null,
                contato.getDataCriacao()
        );
    }
}