package com.wesley.crm.app.models.dtos.cliente;

import jakarta.validation.constraints.*;
import com.wesley.crm.app.validators.ValidCep;

public class ClienteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome não pode ter mais que 150 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    @Size(max = 150, message = "Email não pode ter mais que 150 caracteres")
    private String email;

    @Size(max = 20, message = "Telefone não pode ter mais que 20 caracteres")
    private String telefone;

    @Size(max = 300, message = "Endereço não pode ter mais que 300 caracteres")
    private String endereco;

    @Size(max = 100, message = "Cidade não pode ter mais que 100 caracteres")
    private String cidade;

    @Size(max = 2, message = "Estado deve ter 2 caracteres")
    private String estado;

    @ValidCep
    private String cep;

    private Long empresaId;

    // Constructors
    public ClienteRequestDTO() {
    }

    public ClienteRequestDTO(String nome, String email, String telefone, String endereco,
            String cidade, String estado, String cep, Long empresaId) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.empresaId = empresaId;
    }

    // Getters and Setters
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

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
}