package com.vidaplus.sghss.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vidaplus.sghss.dto.ConsultaCreateDto;
import com.vidaplus.sghss.dto.ConsultaResponseDto;
import com.vidaplus.sghss.dto.ConsultaUpdateDto;
import com.vidaplus.sghss.entity.Consulta;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.entity.ProfissionalSaude;
import com.vidaplus.sghss.entity.Usuario;
import com.vidaplus.sghss.entity.UsuarioTipo;
import com.vidaplus.sghss.repository.ConsultaRepository;
import com.vidaplus.sghss.repository.PacienteRepository;
import com.vidaplus.sghss.repository.ProfissionalSaudeRepository;
import com.vidaplus.sghss.repository.UsuarioRepository;

@Service
public class ConsultaService {
    
    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalSaudeRepository profissionalSaudeRepository;
    private final UsuarioRepository usuarioRepository;
    
    public ConsultaService(
        ConsultaRepository consultaRepository,
        PacienteRepository pacienteRepository,
        ProfissionalSaudeRepository profissionalSaudeRepository,
        UsuarioRepository usuarioRepository
    ) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalSaudeRepository = profissionalSaudeRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    public ConsultaResponseDto criarConsulta(String emailPaciente, ConsultaCreateDto dto) {
        // Buscar paciente pelo email
        Usuario usuario = usuarioRepository.findByEmail(emailPaciente)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (usuario.getTipo() != UsuarioTipo.PACIENTE) {
            throw new RuntimeException("Apenas pacientes podem agendar consultas");
        }
        
        Paciente paciente = pacienteRepository.findById(usuario.getId())
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        
        // Buscar profissional
        ProfissionalSaude profissional = profissionalSaudeRepository.findById(dto.profissionalId())
            .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        
        // Criar consulta
        Consulta consulta = new Consulta(paciente, profissional, dto.dataHora(), dto.observacoes());
        Consulta consultaSalva = consultaRepository.save(consulta);
        
        return toResponseDto(consultaSalva);
    }
    
    public List<ConsultaResponseDto> listarConsultasPaciente(String emailPaciente) {
        Usuario usuario = usuarioRepository.findByEmail(emailPaciente)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (usuario.getTipo() != UsuarioTipo.PACIENTE) {
            throw new RuntimeException("Apenas pacientes podem visualizar suas consultas");
        }
        
        Paciente paciente = pacienteRepository.findById(usuario.getId())
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        
        return consultaRepository.findByPacienteOrderByDataHoraDesc(paciente)
            .stream()
            .map(this::toResponseDto)
            .toList();
    }
    
    public List<ConsultaResponseDto> listarConsultasProfissional(String emailProfissional) {
        Usuario usuario = usuarioRepository.findByEmail(emailProfissional)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (usuario.getTipo() != UsuarioTipo.PROFISSIONAL) {
            throw new RuntimeException("Apenas profissionais podem visualizar suas consultas");
        }
        
        ProfissionalSaude profissional = profissionalSaudeRepository.findById(usuario.getId())
            .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        
        return consultaRepository.findByProfissionalOrderByDataHoraDesc(profissional)
            .stream()
            .map(this::toResponseDto)
            .toList();
    }
    
    public ConsultaResponseDto atualizarConsulta(Long consultaId, String emailProfissional, ConsultaUpdateDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(emailProfissional)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (usuario.getTipo() != UsuarioTipo.PROFISSIONAL) {
            throw new RuntimeException("Apenas profissionais podem atualizar consultas");
        }
        
        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        
        // Verificar se o profissional é o responsável pela consulta
        if (!consulta.getProfissional().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você só pode atualizar suas próprias consultas");
        }
        
        // Atualizar campos
        if (dto.dataHora() != null) {
            consulta.setDataHora(dto.dataHora());
        }
        if (dto.status() != null) {
            consulta.setStatus(dto.status());
        }
        if (dto.observacoes() != null) {
            consulta.setObservacoes(dto.observacoes());
        }
        
        Consulta consultaAtualizada = consultaRepository.save(consulta);
        return toResponseDto(consultaAtualizada);
    }
    
    private ConsultaResponseDto toResponseDto(Consulta consulta) {
        return new ConsultaResponseDto(
            consulta.getId(),
            consulta.getPaciente().getId(),
            consulta.getPaciente().getNome(),
            consulta.getProfissional().getId(),
            consulta.getProfissional().getNome(),
            consulta.getProfissional().getEspecialidade(),
            consulta.getDataHora(),
            consulta.getStatus(),
            consulta.getObservacoes(),
            consulta.getCreatedAt(),
            consulta.getUpdatedAt()
        );
    }
}
