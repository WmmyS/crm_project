package com.wesley.crm.app.models.dtos.empresa;

import java.time.LocalDateTime;
import java.util.List;

public class EmpresaDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<ClienteResumoDTO> clientes;

    public EmpresaDTO(Long id, String nome, String cnpj, String email, String telefone,
                     String endereco, String cidade, String estado, String cep,
                     LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                     List<ClienteResumoDTO> clientes) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.clientes = clientes;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getCep() { return cep; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public List<ClienteResumoDTO> getClientes() { return clientes; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCep(String cep) { this.cep = cep; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public void setClientes(List<ClienteResumoDTO> clientes) { this.clientes = clientes; }
}