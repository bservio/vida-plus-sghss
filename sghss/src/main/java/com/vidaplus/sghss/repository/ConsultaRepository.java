package com.vidaplus.sghss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vidaplus.sghss.entity.Consulta;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.entity.ProfissionalSaude;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    
    List<Consulta> findByPacienteOrderByDataHoraDesc(Paciente paciente);
    
    List<Consulta> findByProfissionalOrderByDataHoraDesc(ProfissionalSaude profissional);
    
    List<Consulta> findByPacienteIdOrderByDataHoraDesc(Long pacienteId);
    
    List<Consulta> findByProfissionalIdOrderByDataHoraDesc(Long profissionalId);
}
