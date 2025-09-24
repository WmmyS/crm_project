package com.wesley.crm.app.services;

import com.wesley.crm.domain.entities.Empresa;
import com.wesley.crm.app.models.dtos.empresa.EmpresaRequestDTO;
import com.wesley.crm.app.models.dtos.empresa.EmpresaResponseDTO;
import com.wesley.crm.infra.database.EmpresaRepository;
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

    public Page<EmpresaResponseDTO> listarTodas(Pageable pageable) {
        Page<Empresa> empresas = empresaRepository.findAll(pageable);
        List<EmpresaResponseDTO> empresaDTOs = empresas.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(empresaDTOs, pageable, empresas.getTotalElements());
    }

    public Optional<EmpresaResponseDTO> buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    public Optional<EmpresaResponseDTO> buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .map(this::convertToResponseDTO);
    }

    public Page<EmpresaResponseDTO> buscarPorTermo(String termo, Pageable pageable) {
        Page<Empresa> empresas = empresaRepository.buscarPorTermo(termo, pageable);
        List<EmpresaResponseDTO> empresaDTOs = empresas.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(empresaDTOs, pageable, empresas.getTotalElements());
    }

    public EmpresaResponseDTO criar(EmpresaRequestDTO empresaRequest) {
        Empresa empresa = convertToEntity(empresaRequest);
        Empresa empresaSalva = empresaRepository.save(empresa);
        return convertToResponseDTO(empresaSalva);
    }

    public Optional<EmpresaResponseDTO> atualizar(Long id, EmpresaRequestDTO empresaRequest) {
        return empresaRepository.findById(id)
                .map(empresa -> {
                    updateEntityFromRequest(empresa, empresaRequest);
                    Empresa empresaAtualizada = empresaRepository.save(empresa);
                    return convertToResponseDTO(empresaAtualizada);
                });
    }

    public void deletar(Long id) {
        empresaRepository.deleteById(id);
    }

    private EmpresaResponseDTO convertToResponseDTO(Empresa empresa) {
        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setId(empresa.getId());
        dto.setNome(empresa.getNome());
        dto.setCnpj(empresa.getCnpj());
        dto.setEmail(empresa.getEmail());
        dto.setTelefone(empresa.getTelefone());
        dto.setEndereco(empresa.getEndereco());
        dto.setDataCriacao(empresa.getDataCriacao());
        dto.setDataAtualizacao(empresa.getDataAtualizacao());
        return dto;
    }

    private Empresa convertToEntity(EmpresaRequestDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());
        empresa.setEndereco(dto.getEndereco());
        return empresa;
    }

    private void updateEntityFromRequest(Empresa empresa, EmpresaRequestDTO dto) {
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());
        empresa.setEndereco(dto.getEndereco());
    }
}
