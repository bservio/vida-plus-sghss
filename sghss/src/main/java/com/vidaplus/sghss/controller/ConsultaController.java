package com.vidaplus.sghss.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vidaplus.sghss.dto.ConsultaCreateDto;
import com.vidaplus.sghss.dto.ConsultaResponseDto;
import com.vidaplus.sghss.dto.ConsultaUpdateDto;
import com.vidaplus.sghss.service.ConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    
    private final ConsultaService consultaService;
    
    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }
    
    @PostMapping
    public ConsultaResponseDto agendarConsulta(@RequestBody ConsultaCreateDto dto) {
        String email = getCurrentUserEmail();
        return consultaService.criarConsulta(email, dto);
    }
    
    @GetMapping("/minhas")
    public List<ConsultaResponseDto> listarMinhasConsultas() {
        String email = getCurrentUserEmail();
        return consultaService.listarConsultasPaciente(email);
    }
    
    @GetMapping("/profissional")
    public List<ConsultaResponseDto> listarConsultasProfissional() {
        String email = getCurrentUserEmail();
        return consultaService.listarConsultasProfissional(email);
    }
    
    @PutMapping("/{id}")
    public ConsultaResponseDto atualizarConsulta(@PathVariable Long id, @RequestBody ConsultaUpdateDto dto) {
        String email = getCurrentUserEmail();
        return consultaService.atualizarConsulta(id, email, dto);
    }
    
    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Não autenticado");
        }
        return auth.getName();
    }
}
