package com.wesley.crm.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "contatos")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @NotNull(message = "Tipo de contato é obrigatório")
    private TipoContato tipo;

    @NotBlank(message = "Assunto é obrigatório")
    @Size(max = 200, message = "Assunto deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String assunto;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_contato", nullable = false)
    @NotNull(message = "Data do contato é obrigatória")
    private LocalDateTime dataContato;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusContato status = StatusContato.CONCLUIDO;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    // Enums
    public enum TipoContato {
        EMAIL, TELEFONE, REUNIAO, CHAT, SMS, VISITA, OUTROS
    }

    public enum StatusContato {
        AGENDADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO
    }

    // Constructors
    public Contato() {}

    public Contato(Cliente cliente, TipoContato tipo, String assunto, LocalDateTime dataContato) {
        this.cliente = cliente;
        this.tipo = tipo;
        this.assunto = assunto;
        this.dataContato = dataContato;
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

    public TipoContato getTipo() {
        return tipo;
    }

    public void setTipo(TipoContato tipo) {
        this.tipo = tipo;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataContato() {
        return dataContato;
    }

    public void setDataContato(LocalDateTime dataContato) {
        this.dataContato = dataContato;
    }

    public StatusContato getStatus() {
        return status;
    }

    public void setStatus(StatusContato status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}