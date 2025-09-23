package com.wesley.crm.app.models.dtos.contato;

import com.wesley.crm.domain.entities.Contato;

import java.time.LocalDateTime;

public class ContatoResponseDTO {
    
    private Long id;
    private Contato.TipoContato tipo;
    private String assunto;
    private String descricao;
    private LocalDateTime dataContato;
    private Contato.StatusContato status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Informações do cliente
    private Long clienteId;
    private String clienteNome;
    private String clienteEmail;
    
    // Constructors
    public ContatoResponseDTO() {}
    
    public ContatoResponseDTO(Long id, Contato.TipoContato tipo, String assunto, String descricao,
                            LocalDateTime dataContato, Contato.StatusContato status,
                            LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                            Long clienteId, String clienteNome, String clienteEmail) {
        this.id = id;
        this.tipo = tipo;
        this.assunto = assunto;
        this.descricao = descricao;
        this.dataContato = dataContato;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.clienteEmail = clienteEmail;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Contato.TipoContato getTipo() {
        return tipo;
    }
    
    public void setTipo(Contato.TipoContato tipo) {
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
    
    public Contato.StatusContato getStatus() {
        return status;
    }
    
    public void setStatus(Contato.StatusContato status) {
        this.status = status;
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
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNome() {
        return clienteNome;
    }
    
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
    
    public String getClienteEmail() {
        return clienteEmail;
    }
    
    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }
}