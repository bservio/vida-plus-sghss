package com.vidaplus.sghss.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vidaplus.sghss.dto.LoginDto;
import com.vidaplus.sghss.dto.PacienteRegisterDto;
import com.vidaplus.sghss.dto.ProfissionalRegisterDto;
import com.vidaplus.sghss.dto.TokenResponseDTO;
import com.vidaplus.sghss.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public TokenResponseDTO login(@RequestBody LoginDto request) {
    return authService.login(request);
  }

  @PostMapping("/register/paciente")
  public TokenResponseDTO registerPaciente(@RequestBody PacienteRegisterDto request) {
    return authService.registerPaciente(request);
  }

  @PostMapping("/register/profissional")
  public TokenResponseDTO registerProfissional(@RequestBody ProfissionalRegisterDto request) {
    return authService.registerProfissional(request);
  }
}
