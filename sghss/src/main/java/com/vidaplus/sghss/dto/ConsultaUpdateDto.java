package com.vidaplus.sghss.dto;

import java.time.LocalDateTime;

import com.vidaplus.sghss.entity.ConsultaStatus;

public record ConsultaUpdateDto(
    LocalDateTime dataHora,
    ConsultaStatus status,
    String observacoes
) {
}
