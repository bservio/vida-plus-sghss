package com.vidaplus.sghss.dto;

import java.time.LocalDate;

public record PacienteResponseDto(
  Long id,
  String nome,
  String email,
  String cpf,
  LocalDate dataNascimento,
  String telefone,
  String endereco
) {}


