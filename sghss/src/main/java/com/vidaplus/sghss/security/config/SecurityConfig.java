package com.vidaplus.sghss.security.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vidaplus.sghss.repository.UsuarioRepository;
import com.vidaplus.sghss.security.jwt.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
  private final JwtUtil jwtUtil;
  private final UsuarioRepository usuarioRepository;

  public SecurityConfig(
    JwtUtil jwtUtil,
    UsuarioRepository usuarioRepository
  ) {
    this.jwtUtil = jwtUtil;
    this.usuarioRepository = usuarioRepository;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests( auth -> auth.requestMatchers("/auth/**", "/login")
            .permitAll()
            .anyRequest()
            .authenticated()
            )
            .addFilterBefore(new JwtFilter(jwtUtil, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  static class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public JwtFilter(
      JwtUtil jwtUtil,
      UsuarioRepository usuarioRepository
    ){
      this.jwtUtil = jwtUtil;
      this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
      @NonNull HttpServletRequest request, 
      @NonNull HttpServletResponse response, 
      @NonNull FilterChain filterChain
    ) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
          String token = header.substring(7);
          if (jwtUtil.validarToken(token)) {
            String email = jwtUtil.extrairEmail(token);
            var usuario = usuarioRepository.findByEmail(email).orElse(null);
            if (usuario != null) {
              var auth = new UsernamePasswordAuthenticationToken(email,null, null);
              SecurityContextHolder.getContext().setAuthentication(auth);
            }
          }
        }
        filterChain.doFilter(request, response);
      }
      
  }
}
