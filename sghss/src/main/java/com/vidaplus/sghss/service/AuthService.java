package com.vidaplus.sghss.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vidaplus.sghss.dto.LoginDto;
import com.vidaplus.sghss.dto.PacienteRegisterDto;
import com.vidaplus.sghss.dto.ProfissionalRegisterDto;
import com.vidaplus.sghss.dto.TokenResponseDTO;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.entity.ProfissionalSaude;
import com.vidaplus.sghss.entity.Usuario;
import com.vidaplus.sghss.repository.PacienteRepository;
import com.vidaplus.sghss.repository.ProfissionalSaudeRepository;
import com.vidaplus.sghss.repository.UsuarioRepository;
import com.vidaplus.sghss.security.jwt.JwtUtil;

@Service
public class AuthService {
  private final UsuarioRepository usuarioRepository;
  private final PacienteRepository pacienteRepository;
  private final ProfissionalSaudeRepository profissionalSaudeRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(
    UsuarioRepository usuarioRepository,
    PacienteRepository pacienteRepository,
    ProfissionalSaudeRepository profissionalSaudeRepository,
    PasswordEncoder passwordEncoder,
    JwtUtil jwtUtil
  ) {
    this.usuarioRepository = usuarioRepository;
    this.pacienteRepository = pacienteRepository;
    this.profissionalSaudeRepository = profissionalSaudeRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public TokenResponseDTO login (LoginDto loginDto) {
    Usuario usuario = usuarioRepository.findByEmail(loginDto.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!usuario.getAtivo()) {
      throw new RuntimeException("Usuário inativo");
    }

    if (!passwordEncoder.matches(loginDto.senha(), usuario.getSenha())) {
      throw new RuntimeException("Senha inválida");
    }

    String token = jwtUtil.gerarToken(usuario.getEmail());
    return new TokenResponseDTO(token);
  }


  public TokenResponseDTO registerPaciente(PacienteRegisterDto pacienteDto) {
    // Verificar se o email já existe
    if (usuarioRepository.findByEmail(pacienteDto.email()).isPresent()) {
      throw new RuntimeException("Email já cadastrado");
    }

    // Criar novo paciente
    Paciente novoPaciente = new Paciente(
      pacienteDto.nome(),
      pacienteDto.email(),
      passwordEncoder.encode(pacienteDto.senha()),
      pacienteDto.cpf(),
      pacienteDto.dataNascimento(),
      pacienteDto.telefone(),
      pacienteDto.endereco()
    );

    // Salvar no banco de dados
    Paciente pacienteSalvo = pacienteRepository.save(novoPaciente);

    // Gerar token JWT
    String token = jwtUtil.gerarToken(pacienteSalvo.getEmail());
    return new TokenResponseDTO(token);
  }

  public TokenResponseDTO registerProfissional(ProfissionalRegisterDto profissionalDto) {
    // Verificar se o email já existe
    if (usuarioRepository.findByEmail(profissionalDto.email()).isPresent()) {
      throw new RuntimeException("Email já cadastrado");
    }

    // Criar novo profissional
    ProfissionalSaude novoProfissional = new ProfissionalSaude(
      profissionalDto.nome(),
      profissionalDto.email(),
      passwordEncoder.encode(profissionalDto.senha()),
      profissionalDto.cpf(),
      profissionalDto.dataNascimento(),
      profissionalDto.telefone(),
      profissionalDto.endereco(),
      profissionalDto.codigoRegistroProfissional(),
      profissionalDto.especialidade()
    );

    // Salvar no banco de dados
    ProfissionalSaude profissionalSalvo = profissionalSaudeRepository.save(novoProfissional);

    // Gerar token JWT
    String token = jwtUtil.gerarToken(profissionalSalvo.getEmail());
    return new TokenResponseDTO(token);
  }
}
