package com.vidaplus.sghss.dto;

import java.time.LocalDateTime;

public record ConsultaCreateDto(
    Long profissionalId,
    LocalDateTime dataHora,
    String observacoes
) {
}
