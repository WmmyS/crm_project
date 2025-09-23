package com.wesley.crm.app.services;

import com.wesley.crm.domain.entities.Empresa;
import com.wesley.crm.domain.entities.Cliente;
import com.wesley.crm.app.models.dtos.empresa.EmpresaRequestDTO;
import com.wesley.crm.app.models.dtos.empresa.EmpresaResponseDTO;
import com.wesley.crm.app.models.dtos.cliente.ClienteResumoDTO;
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
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    // Métodos de consulta
    public Page<EmpresaResponseDTO> listarTodas(Pageable pageable) {
        Page<Empresa> empresas = empresaRepository.findAll(pageable);
        List<EmpresaResponseDTO> empresaDTOs = empresas.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(empresaDTOs, pageable, empresas.getTotalElements());
    }
        List<EmpresaDTO> empresaDTOs = empresas.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(empresaDTOs, pageable, empresas.getTotalElements());
    }

    public Optional<EmpresaResponseDTO> buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<EmpresaResponseDTO> buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .map(this::convertToDTO);
    }

    public Page<EmpresaResponseDTO> buscarPorTermo(String termo, Pageable pageable) {
        Page<Empresa> empresas = empresaRepository.buscarPorTermo(termo, pageable);
        List<EmpresaResponseDTO> empresaDTOs = empresas.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(empresaDTOs, pageable, empresas.getTotalElements());
    }

    public List<Empresa> listarPorEstado(String estado) {
        return empresaRepository.findByEstado(estado);
    }

    // Métodos de modificação
    public Empresa criar(Empresa empresa) {
        validarEmpresa(empresa);
        validarCnpjUnico(empresa.getCnpj(), null);
        return empresaRepository.save(empresa);
    }

    public Empresa atualizar(Long id, Empresa empresaAtualizada) {
        return empresaRepository.findById(id)
                .map(empresa -> {
                    validarEmpresa(empresaAtualizada);
                    validarCnpjUnico(empresaAtualizada.getCnpj(), id);
                    
                    empresa.setNome(empresaAtualizada.getNome());
                    empresa.setCnpj(empresaAtualizada.getCnpj());
                    empresa.setEmail(empresaAtualizada.getEmail());
                    empresa.setTelefone(empresaAtualizada.getTelefone());
                    empresa.setEndereco(empresaAtualizada.getEndereco());
                    empresa.setCidade(empresaAtualizada.getCidade());
                    empresa.setEstado(empresaAtualizada.getEstado());
                    empresa.setCep(empresaAtualizada.getCep());

                    return empresaRepository.save(empresa);
                })
                .orElseThrow(() -> new CrmException("Empresa não encontrada com ID: " + id));
    }

    public void deletar(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new CrmException("Empresa não encontrada com ID: " + id);
        }
        
        // Verificar se há clientes vinculados
        Optional<Empresa> empresa = empresaRepository.findById(id);
        if (empresa.isPresent() && empresa.get().getClientes() != null && !empresa.get().getClientes().isEmpty()) {
            throw new CrmException("Não é possível deletar empresa com clientes vinculados");
        }
        
        empresaRepository.deleteById(id);
    }

    public boolean existePorId(Long id) {
        return empresaRepository.existsById(id);
    }

    // Métodos de relatórios
    public Long contarPorEstado(String estado) {
        return empresaRepository.countByEstado(estado);
    }

    public List<Object[]> empresasPorEstado() {
        return empresaRepository.countEmpresasPorEstado();
    }

    // Métodos de validação
    private void validarEmpresa(Empresa empresa) {
        if (empresa.getNome() == null || empresa.getNome().trim().isEmpty()) {
            throw new CrmException("Nome da empresa é obrigatório");
        }
        
        if (empresa.getCnpj() != null && !empresa.getCnpj().trim().isEmpty()) {
            // Aqui você pode adicionar validação de CNPJ
            if (empresa.getCnpj().length() > 18) {
                throw new CrmException("CNPJ deve ter no máximo 18 caracteres");
            }
        }
    }

    private void validarCnpjUnico(String cnpj, Long empresaId) {
        if (cnpj != null && !cnpj.trim().isEmpty()) {
            Optional<Empresa> empresaExistente = empresaRepository.findByCnpj(cnpj);
            if (empresaExistente.isPresent() && !empresaExistente.get().getId().equals(empresaId)) {
                throw new CrmException("CNPJ já está em uso por outra empresa");
            }
        }
    }

    // Método para converter Empresa para EmpresaResponseDTO
    private EmpresaResponseDTO convertToDTO(Empresa empresa) {
        List<ClienteResumoDTO> clientesDTO = null;
        if (empresa.getClientes() != null) {
            clientesDTO = empresa.getClientes().stream()
                    .map(cliente -> new ClienteResumoDTO(
                            cliente.getId(),
                            cliente.getNome(),
                            cliente.getEmail(),
                            cliente.getTelefone(),
                            cliente.getStatus() != null ? cliente.getStatus().toString() : null
                    ))
                    .collect(Collectors.toList());
        }

        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getEmail(),
                empresa.getTelefone(),
                empresa.getEndereco(),
                empresa.getCidade(),
                empresa.getEstado(),
                empresa.getCep(),
                empresa.getDataCriacao(),
                empresa.getDataAtualizacao(),
                clientesDTO
        );
    }
}