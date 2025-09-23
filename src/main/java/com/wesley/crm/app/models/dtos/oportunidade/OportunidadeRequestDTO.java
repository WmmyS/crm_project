package com.wesley.crm.app.models.dtos.oportunidade;

import com.wesley.crm.domain.entities.Oportunidade;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OportunidadeRequestDTO {
    
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título não pode ter mais que 200 caracteres")
    private String titulo;
    
    @Size(max = 1000, message = "Descrição não pode ter mais que 1000 caracteres")
    private String descricao;
    
    @NotNull(message = "Valor estimado é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor estimado deve ser maior que zero")
    private BigDecimal valorEstimado;
    
    @NotNull(message = "Probabilidade de fechamento é obrigatória")
    @Min(value = 0, message = "Probabilidade deve ser entre 0 e 100")
    @Max(value = 100, message = "Probabilidade deve ser entre 0 e 100")
    private Integer probabilidadeFechamento;
    
    @NotNull(message = "Status é obrigatório")
    private Oportunidade.StatusOportunidade status;
    
    @NotNull(message = "Data de fechamento prevista é obrigatória")
    @FutureOrPresent(message = "Data de fechamento prevista deve ser no futuro ou presente")
    private LocalDate dataFechamentoPrevista;
    
    private LocalDate dataFechamentoReal;
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    
    // Constructors
    public OportunidadeRequestDTO() {}
    
    public OportunidadeRequestDTO(String titulo, String descricao, BigDecimal valorEstimado, 
                                Integer probabilidadeFechamento, Oportunidade.StatusOportunidade status,
                                LocalDate dataFechamentoPrevista, LocalDate dataFechamentoReal, Long clienteId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.valorEstimado = valorEstimado;
        this.probabilidadeFechamento = probabilidadeFechamento;
        this.status = status;
        this.dataFechamentoPrevista = dataFechamentoPrevista;
        this.dataFechamentoReal = dataFechamentoReal;
        this.clienteId = clienteId;
    }
    
    // Getters and Setters
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }
    
    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }
    
    public Integer getProbabilidadeFechamento() {
        return probabilidadeFechamento;
    }
    
    public void setProbabilidadeFechamento(Integer probabilidadeFechamento) {
        this.probabilidadeFechamento = probabilidadeFechamento;
    }
    
    public Oportunidade.StatusOportunidade getStatus() {
        return status;
    }
    
    public void setStatus(Oportunidade.StatusOportunidade status) {
        this.status = status;
    }
    
    public LocalDate getDataFechamentoPrevista() {
        return dataFechamentoPrevista;
    }
    
    public void setDataFechamentoPrevista(LocalDate dataFechamentoPrevista) {
        this.dataFechamentoPrevista = dataFechamentoPrevista;
    }
    
    public LocalDate getDataFechamentoReal() {
        return dataFechamentoReal;
    }
    
    public void setDataFechamentoReal(LocalDate dataFechamentoReal) {
        this.dataFechamentoReal = dataFechamentoReal;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}