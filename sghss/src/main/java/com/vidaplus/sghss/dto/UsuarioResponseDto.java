package com.vidaplus.sghss.dto;

import java.time.LocalDateTime;

import com.vidaplus.sghss.entity.UsuarioTipo;

public record UsuarioResponseDto(
    Long id,
    String nome,
    String email,
    UsuarioTipo tipo,
    Boolean ativo,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
