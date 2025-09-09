package com.vidaplus.sghss.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pacientes")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Paciente extends Usuario {
    
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;
    
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;
    
    @Column(length = 15)
    private String telefone;
    
    @Column(length = 255)
    private String endereco;
    
    public Paciente(String nome, String email, String senha, String cpf, LocalDate dataNascimento, String telefone, String endereco) {
        super(nome, email, senha, UsuarioTipo.PACIENTE);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.endereco = endereco;
    }
}
