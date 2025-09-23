package com.wesley.crm.app.models.dtos.oportunidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OportunidadeResponseDTO(
    Long id,
    Long clienteId,
    String clienteNome,
    String titulo,
    String descricao,
    BigDecimal valorEstimado,
    Integer probabilidadeFechamento,
    String status,
    LocalDate dataFechamentoPrevista,
    LocalDate dataFechamentoReal,
    LocalDateTime dataCriacao,
    LocalDateTime dataAtualizacao
) {}