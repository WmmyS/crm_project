package com.wesley.crm.app.models.dtos.cliente;

import java.time.LocalDateTime;

public class ClienteResponseDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Informações da empresa
    private Long empresaId;
    private String empresaNome;
    private String empresaCnpj;
    
    // Constructors
    public ClienteResponseDTO() {}
    
    public ClienteResponseDTO(Long id, String nome, String email, String telefone, String endereco,
                            String cidade, String estado, String cep, LocalDateTime dataCriacao,
                            LocalDateTime dataAtualizacao, Long empresaId, String empresaNome, String empresaCnpj) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.empresaId = empresaId;
        this.empresaNome = empresaNome;
        this.empresaCnpj = empresaCnpj;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCep() {
        return cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
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
    
    public Long getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    
    public String getEmpresaNome() {
        return empresaNome;
    }
    
    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }
    
    public String getEmpresaCnpj() {
        return empresaCnpj;
    }
    
    public void setEmpresaCnpj(String empresaCnpj) {
        this.empresaCnpj = empresaCnpj;
    }
}