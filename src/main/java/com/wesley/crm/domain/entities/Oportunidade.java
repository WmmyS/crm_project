package com.wesley.crm.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidades")
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "Cliente é obrigatório")
    @JsonIgnore
    private Cliente cliente;

    @NotBlank(message = "Título da oportunidade é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @DecimalMin(value = "0.0", inclusive = false, message = "Valor estimado deve ser maior que zero")
    @Column(name = "valor_estimado", precision = 15, scale = 2)
    private BigDecimal valorEstimado;

    @Min(value = 0, message = "Probabilidade deve ser no mínimo 0")
    @Max(value = 100, message = "Probabilidade deve ser no máximo 100")
    @Column(name = "probabilidade_fechamento")
    private Integer probabilidadeFechamento = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private StatusOportunidade status = StatusOportunidade.PROSPECCAO;

    @Column(name = "data_fechamento_prevista")
    private LocalDate dataFechamentoPrevista;

    @Column(name = "data_fechamento_real")
    private LocalDate dataFechamentoReal;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    // Enums
    public enum StatusOportunidade {
        PROSPECCAO("Prospecção"),
        QUALIFICACAO("Qualificação"),
        PROPOSTA("Proposta"),
        NEGOCIACAO("Negociação"),
        FECHADO("Fechado - Ganho"),
        PERDIDO("Perdido");

        private final String descricao;

        StatusOportunidade(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Constructors
    public Oportunidade() {}

    public Oportunidade(Cliente cliente, String titulo, BigDecimal valorEstimado) {
        this.cliente = cliente;
        this.titulo = titulo;
        this.valorEstimado = valorEstimado;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

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

    public StatusOportunidade getStatus() {
        return status;
    }

    public void setStatus(StatusOportunidade status) {
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}