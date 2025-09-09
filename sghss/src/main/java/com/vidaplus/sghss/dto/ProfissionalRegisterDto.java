package com.vidaplus.sghss.dto;

import java.time.LocalDate;

public record ProfissionalRegisterDto(
    String nome,
    String email,
    String senha,
    String cpf,
    LocalDate dataNascimento,
    String telefone,
    String endereco,
    String codigoRegistroProfissional,
    String especialidade
) {
}
