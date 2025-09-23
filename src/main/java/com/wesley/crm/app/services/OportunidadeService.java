package com.wesley.crm.app.services;

import com.wesley.crm.domain.entities.Oportunidade;
import com.wesley.crm.domain.entities.Cliente;
import com.wesley.crm.app.models.dtos.oportunidade.OportunidadeRequestDTO;
import com.wesley.crm.app.models.dtos.oportunidade.OportunidadeResponseDTO;
import com.wesley.crm.infra.database.OportunidadeRepository;
import com.wesley.crm.infra.database.ClienteRepository;
import com.wesley.crm.exceptions.CrmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OportunidadeService {

    @Autowired
    private OportunidadeRepository oportunidadeRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    // Métodos de consulta
    public Page<OportunidadeResponseDTO> listarTodas(Pageable pageable) {
        Page<Oportunidade> oportunidades = oportunidadeRepository.findAll(pageable);
        List<OportunidadeResponseDTO> oportunidadeDTOs = oportunidades.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(oportunidadeDTOs, pageable, oportunidades.getTotalElements());
    }

    public Optional<OportunidadeResponseDTO> buscarPorIdOpcional(Long id) {
        return oportunidadeRepository.findById(id)
                .map(this::convertToDTO);
    }

    public OportunidadeResponseDTO buscarPorId(Long id) {
        return oportunidadeRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new CrmException("Oportunidade não encontrada com ID: " + id));
    }

    public List<OportunidadeResponseDTO> listarPorCliente(Long clienteId) {
        List<Oportunidade> oportunidades = oportunidadeRepository.findByClienteId(clienteId);
        return oportunidades.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<OportunidadeResponseDTO> listarPorCliente(Long clienteId, Pageable pageable) {
        Page<Oportunidade> oportunidades = oportunidadeRepository.findByClienteIdOrderByDataAtualizacaoDesc(clienteId, pageable);
        List<OportunidadeResponseDTO> oportunidadeDTOs = oportunidades.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(oportunidadeDTOs, pageable, oportunidades.getTotalElements());
    }

    public List<OportunidadeResponseDTO> listarPorStatus(Oportunidade.StatusOportunidade status) {
        List<Oportunidade> oportunidades = oportunidadeRepository.findByStatus(status);
        return oportunidades.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OportunidadeResponseDTO> listarPorFaixaValor(BigDecimal valorMinimo, BigDecimal valorMaximo) {
        List<Oportunidade> oportunidades = oportunidadeRepository.findByValorEstimadoBetween(valorMinimo, valorMaximo);
        return oportunidades.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<OportunidadeResponseDTO> buscarPorTermo(String termo, Pageable pageable) {
        Page<Oportunidade> oportunidades = oportunidadeRepository.buscarPorTermo(termo, pageable);
        List<OportunidadeResponseDTO> oportunidadeDTOs = oportunidades.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(oportunidadeDTOs, pageable, oportunidades.getTotalElements());
    }

    public List<OportunidadeResponseDTO> listarPorProbabilidade(Integer probabilidadeMinima) {
        List<Oportunidade> oportunidades = oportunidadeRepository.findByProbabilidadeFechamentoGreaterThanEqual(probabilidadeMinima);
        return oportunidades.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OportunidadeResponseDTO> listarComFechamentoPrevisto(LocalDate dataInicio, LocalDate dataFim) {
        List<Oportunidade> oportunidades = oportunidadeRepository.findByDataFechamentoPrevistaBetween(dataInicio, dataFim);
        return oportunidades.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Métodos de modificação
    public OportunidadeResponseDTO criar(OportunidadeRequestDTO oportunidadeDTO) {
        Oportunidade oportunidade = convertFromDTO(oportunidadeDTO);
        validarOportunidade(oportunidade);
        validarClienteExiste(oportunidadeDTO.getClienteId());
        
        Cliente cliente = clienteRepository.findById(oportunidadeDTO.getClienteId())
                .orElseThrow(() -> new CrmException("Cliente não encontrado"));
        oportunidade.setCliente(cliente);
        
        Oportunidade oportunidadeSalva = oportunidadeRepository.save(oportunidade);
        return convertToDTO(oportunidadeSalva);
    }

    public OportunidadeResponseDTO atualizar(Long id, OportunidadeRequestDTO oportunidadeDTO) {
        return oportunidadeRepository.findById(id)
                .map(oportunidade -> {
                    Oportunidade oportunidadeAtualizada = convertFromDTO(oportunidadeDTO);
                    validarOportunidade(oportunidadeAtualizada);
                    
                    if (oportunidadeDTO.getClienteId() != null && 
                        !oportunidade.getCliente().getId().equals(oportunidadeDTO.getClienteId())) {
                        validarClienteExiste(oportunidadeDTO.getClienteId());
                        Cliente cliente = clienteRepository.findById(oportunidadeDTO.getClienteId())
                                .orElseThrow(() -> new CrmException("Cliente não encontrado"));
                        oportunidade.setCliente(cliente);
                    }
                    
                    oportunidade.setTitulo(oportunidadeAtualizada.getTitulo());
                    oportunidade.setDescricao(oportunidadeAtualizada.getDescricao());
                    oportunidade.setValorEstimado(oportunidadeAtualizada.getValorEstimado());
                    oportunidade.setProbabilidadeFechamento(oportunidadeAtualizada.getProbabilidadeFechamento());
                    oportunidade.setStatus(oportunidadeAtualizada.getStatus());
                    oportunidade.setDataFechamentoPrevista(oportunidadeAtualizada.getDataFechamentoPrevista());
                    oportunidade.setDataFechamentoReal(oportunidadeAtualizada.getDataFechamentoReal());
                    
                    Oportunidade oportunidadeSalva = oportunidadeRepository.save(oportunidade);
                    return convertToDTO(oportunidadeSalva);
                })
                .orElseThrow(() -> new CrmException("Oportunidade não encontrada"));
    }

    public void deletar(Long id) {
        if (!oportunidadeRepository.existsById(id)) {
            throw new CrmException("Oportunidade não encontrada");
        }
        oportunidadeRepository.deleteById(id);
    }

    // Métodos antigos - manter para compatibilidade
    public Oportunidade criar(Oportunidade oportunidade) {
        validarOportunidade(oportunidade);
        validarClienteExiste(oportunidade.getCliente().getId());
        return oportunidadeRepository.save(oportunidade);
    }

    public Oportunidade atualizar(Long id, Oportunidade oportunidadeAtualizada) {
        return oportunidadeRepository.findById(id)
                .map(oportunidade -> {
                    validarOportunidade(oportunidadeAtualizada);
                    
                    if (oportunidadeAtualizada.getCliente() != null && 
                        !oportunidade.getCliente().getId().equals(oportunidadeAtualizada.getCliente().getId())) {
                        validarClienteExiste(oportunidadeAtualizada.getCliente().getId());
                        oportunidade.setCliente(oportunidadeAtualizada.getCliente());
                    }
                    
                    oportunidade.setTitulo(oportunidadeAtualizada.getTitulo());
                    oportunidade.setDescricao(oportunidadeAtualizada.getDescricao());
                    oportunidade.setValorEstimado(oportunidadeAtualizada.getValorEstimado());
                    oportunidade.setProbabilidadeFechamento(oportunidadeAtualizada.getProbabilidadeFechamento());
                    oportunidade.setStatus(oportunidadeAtualizada.getStatus());
                    oportunidade.setDataFechamentoPrevista(oportunidadeAtualizada.getDataFechamentoPrevista());
                    oportunidade.setDataFechamentoReal(oportunidadeAtualizada.getDataFechamentoReal());

                    return oportunidadeRepository.save(oportunidade);
                })
                .orElseThrow(() -> new CrmException("Oportunidade não encontrada com ID: " + id));
    }

    public Oportunidade atualizarStatus(Long id, Oportunidade.StatusOportunidade novoStatus) {
        return oportunidadeRepository.findById(id)
                .map(oportunidade -> {
                    oportunidade.setStatus(novoStatus);
                    
                    // Se fechou (ganho ou perdido), definir data de fechamento real
                    if (novoStatus == Oportunidade.StatusOportunidade.FECHADO || 
                        novoStatus == Oportunidade.StatusOportunidade.PERDIDO) {
                        oportunidade.setDataFechamentoReal(LocalDate.now());
                    }
                    
                    return oportunidadeRepository.save(oportunidade);
                })
                .orElseThrow(() -> new CrmException("Oportunidade não encontrada com ID: " + id));
    }

    public void deletar(Long id) {
        if (!oportunidadeRepository.existsById(id)) {
            throw new CrmException("Oportunidade não encontrada com ID: " + id);
        }
        oportunidadeRepository.deleteById(id);
    }

    public boolean existePorId(Long id) {
        return oportunidadeRepository.existsById(id);
    }

    // Métodos de relatórios
    public Long contarPorStatus(Oportunidade.StatusOportunidade status) {
        return oportunidadeRepository.countByStatus(status);
    }

    public Long contarPorCliente(Long clienteId) {
        return oportunidadeRepository.countByClienteId(clienteId);
    }

    public BigDecimal somarValorPorStatus(Oportunidade.StatusOportunidade status) {
        return oportunidadeRepository.sumValorEstimadoByStatus(status);
    }

    public List<Object[]> oportunidadesPorStatus() {
        return oportunidadeRepository.countOportunidadesPorStatus();
    }

    public List<Object[]> valorTotalPorMes() {
        return oportunidadeRepository.sumValorEstimadoPorMes();
    }

    public Double calcularTaxaConversao() {
        Long totalOportunidades = oportunidadeRepository.count();
        Long oportunidadesFechadas = oportunidadeRepository.countByStatus(Oportunidade.StatusOportunidade.FECHADO);
        
        if (totalOportunidades == 0) {
            return 0.0;
        }
        
        return (oportunidadesFechadas.doubleValue() / totalOportunidades.doubleValue()) * 100;
    }

    // Métodos de validação
    private void validarOportunidade(Oportunidade oportunidade) {
        if (oportunidade.getCliente() == null || oportunidade.getCliente().getId() == null) {
            throw new CrmException("Cliente é obrigatório para a oportunidade");
        }
        
        if (oportunidade.getTitulo() == null || oportunidade.getTitulo().trim().isEmpty()) {
            throw new CrmException("Título da oportunidade é obrigatório");
        }
        
        if (oportunidade.getValorEstimado() != null && oportunidade.getValorEstimado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CrmException("Valor estimado deve ser maior que zero");
        }
        
        if (oportunidade.getProbabilidadeFechamento() != null) {
            if (oportunidade.getProbabilidadeFechamento() < 0 || oportunidade.getProbabilidadeFechamento() > 100) {
                throw new CrmException("Probabilidade de fechamento deve estar entre 0 e 100");
            }
        }
        
        // Validar datas
        if (oportunidade.getDataFechamentoPrevista() != null && 
            oportunidade.getDataFechamentoPrevista().isBefore(LocalDate.now())) {
            throw new CrmException("Data de fechamento prevista não pode ser anterior à data atual");
        }
        
        if (oportunidade.getDataFechamentoReal() != null && 
            oportunidade.getDataFechamentoPrevista() != null &&
            oportunidade.getDataFechamentoReal().isBefore(oportunidade.getDataFechamentoPrevista())) {
            throw new CrmException("Data de fechamento real não pode ser anterior à data prevista");
        }
    }

    private void validarClienteExiste(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new CrmException("Cliente não encontrado com ID: " + clienteId);
        }
    }

    // Método para converter OportunidadeRequestDTO para Oportunidade
    private Oportunidade convertFromDTO(OportunidadeRequestDTO dto) {
        Oportunidade oportunidade = new Oportunidade();
        oportunidade.setTitulo(dto.getTitulo());
        oportunidade.setDescricao(dto.getDescricao());
        oportunidade.setValorEstimado(dto.getValorEstimado());
        oportunidade.setProbabilidadeFechamento(dto.getProbabilidadeFechamento());
        oportunidade.setStatus(dto.getStatus());
        oportunidade.setDataFechamentoPrevista(dto.getDataFechamentoPrevista());
        oportunidade.setDataFechamentoReal(dto.getDataFechamentoReal());
        return oportunidade;
    }

    // Método para converter Oportunidade para OportunidadeResponseDTO
    private OportunidadeResponseDTO convertToDTO(Oportunidade oportunidade) {
        return new OportunidadeResponseDTO(
                oportunidade.getId(),
                oportunidade.getCliente() != null ? oportunidade.getCliente().getId() : null,
                oportunidade.getCliente() != null ? oportunidade.getCliente().getNome() : null,
                oportunidade.getTitulo(),
                oportunidade.getDescricao(),
                oportunidade.getValorEstimado(),
                oportunidade.getProbabilidadeFechamento(),
                oportunidade.getStatus() != null ? oportunidade.getStatus().name() : null,
                oportunidade.getDataFechamentoPrevista(),
                oportunidade.getDataFechamentoReal(),
                oportunidade.getDataCriacao(),
                oportunidade.getDataAtualizacao()
        );
    }
}