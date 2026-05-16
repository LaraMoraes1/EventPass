package com.eventpass.api.service;

import com.eventpass.api.dto.*;
import com.eventpass.api.model.*;
import com.eventpass.api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UsuarioRepository usuarios;

    public AuthService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    public UserResponse login(LoginRequest request) {
        Usuario usuario = usuarios.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos"));
        if (!usuario.getSenha().equals(request.senha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");
        }
        return toResponse(usuario);
    }

    public UserResponse register(RegisterRequest request) {
        if (usuarios.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());
        usuario.setTipo("ADMIN".equalsIgnoreCase(request.tipo()) ? UserType.ADMIN : UserType.PARTICIPANTE);
        return toResponse(usuarios.save(usuario));
    }

    private UserResponse toResponse(Usuario usuario) {
        return new UserResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTipo().name());
    }
}
