package com.vidaplus.sghss.dto;

import java.time.LocalDateTime;

import com.vidaplus.sghss.entity.ConsultaStatus;

public record ConsultaResponseDto(
    Long id,
    Long pacienteId,
    String pacienteNome,
    Long profissionalId,
    String profissionalNome,
    String profissionalEspecialidade,
    LocalDateTime dataHora,
    ConsultaStatus status,
    String observacoes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
