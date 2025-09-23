package com.wesley.crm.app.models.dtos.contato;

import com.wesley.crm.domain.entities.Contato;
import java.time.LocalDateTime;

public record ContatoDTO(
    Long id,
    Long clienteId,
    String clienteNome,
    String tipo,
    String assunto,
    String descricao,
    LocalDateTime dataContato,
    String status,
    LocalDateTime dataCriacao
) {}