package com.wesley.crm.app.models.dtos.empresa;

import jakarta.validation.constraints.*;

public class EmpresaRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200, message = "Nome não pode ter mais que 200 caracteres")
    private String nome;
    
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX")
    private String cnpj;
    
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
    
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato XXXXX-XXX")
    private String cep;
    
    @Size(max = 100, message = "Setor não pode ter mais que 100 caracteres")
    private String setor;
    
    private Integer numeroFuncionarios;
    
    // Constructors
    public EmpresaRequestDTO() {}
    
    public EmpresaRequestDTO(String nome, String cnpj, String email, String telefone, String endereco,
                           String cidade, String estado, String cep, String setor, Integer numeroFuncionarios) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.setor = setor;
        this.numeroFuncionarios = numeroFuncionarios;
    }
    
    // Getters and Setters
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
    
    public String getSetor() {
        return setor;
    }
    
    public void setSetor(String setor) {
        this.setor = setor;
    }
    
    public Integer getNumeroFuncionarios() {
        return numeroFuncionarios;
    }
    
    public void setNumeroFuncionarios(Integer numeroFuncionarios) {
        this.numeroFuncionarios = numeroFuncionarios;
    }
}