package com.vidaplus.sghss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vidaplus.sghss.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
