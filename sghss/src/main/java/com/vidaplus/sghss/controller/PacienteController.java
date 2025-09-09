package com.vidaplus.sghss.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vidaplus.sghss.dto.PacienteResponseDto;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.entity.Usuario;
import com.vidaplus.sghss.entity.UsuarioTipo;
import com.vidaplus.sghss.repository.PacienteRepository;
import com.vidaplus.sghss.repository.UsuarioRepository;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
  private final PacienteRepository pacienteRepository;
  private final UsuarioRepository usuarioRepository;

  public PacienteController(PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository) {
    this.pacienteRepository = pacienteRepository;
    this.usuarioRepository = usuarioRepository;
  }

  @GetMapping
  public List<PacienteResponseDto> listar() {
    garantirProfissional();
    return pacienteRepository.findAll().stream().map(this::toDto).toList();
  }

  @GetMapping("/{id}")
  public PacienteResponseDto buscarPorId(@PathVariable Long id) {
    garantirProfissional();
    Paciente paciente = pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    return toDto(paciente);
  }

  private void garantirProfissional() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getName() == null) {
      throw new RuntimeException("Não autenticado");
    }
    String email = auth.getName();
    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    if (usuario.getTipo() != UsuarioTipo.PROFISSIONAL) {
      throw new RuntimeException("Acesso negado: apenas profissionais");
    }
  }

  private PacienteResponseDto toDto(Paciente p) {
    return new PacienteResponseDto(
      p.getId(),
      p.getNome(),
      p.getEmail(),
      p.getCpf(),
      p.getDataNascimento(),
      p.getTelefone(),
      p.getEndereco()
    );
  }
}


