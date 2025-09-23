package com.wesley.crm.app.models.dtos.cliente;

import java.time.LocalDateTime;

public class ClienteResumoDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String status;
    private LocalDateTime dataCriacao;

    public ClienteResumoDTO(Long id, String nome, String email, String telefone, 
                           String status, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setStatus(String status) { this.status = status; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}