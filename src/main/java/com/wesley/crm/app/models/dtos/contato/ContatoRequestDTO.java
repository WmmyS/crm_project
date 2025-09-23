package com.wesley.crm.app.models.dtos.contato;

import com.wesley.crm.domain.entities.Contato;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class ContatoRequestDTO {
    
    @NotNull(message = "Tipo de contato é obrigatório")
    private Contato.TipoContato tipo;
    
    @NotBlank(message = "Assunto é obrigatório")
    @Size(max = 200, message = "Assunto não pode ter mais que 200 caracteres")
    private String assunto;
    
    @Size(max = 1000, message = "Descrição não pode ter mais que 1000 caracteres")
    private String descricao;
    
    @NotNull(message = "Data do contato é obrigatória")
    private LocalDateTime dataContato;
    
    @NotNull(message = "Status é obrigatório")
    private Contato.StatusContato status;
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    
    // Constructors
    public ContatoRequestDTO() {}
    
    public ContatoRequestDTO(Contato.TipoContato tipo, String assunto, String descricao,
                           LocalDateTime dataContato, Contato.StatusContato status, Long clienteId) {
        this.tipo = tipo;
        this.assunto = assunto;
        this.descricao = descricao;
        this.dataContato = dataContato;
        this.status = status;
        this.clienteId = clienteId;
    }
    
    // Getters and Setters
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
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}