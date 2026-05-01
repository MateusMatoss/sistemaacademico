package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.LoginDTO;
import br.com.sistemaacademico.dto.LoginResponseDTO;
import br.com.sistemaacademico.dto.PrimeiroAcessoSenhaDTO;
import br.com.sistemaacademico.dto.UsuarioRegistroDTO;
import br.com.sistemaacademico.model.CursoModel;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.service.AuthService;
import br.com.sistemaacademico.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CursoService cursoService;

    public AuthController(AuthService authService, CursoService cursoService) {
        this.authService = authService;
        this.cursoService = cursoService;
    }

    @PostMapping("/registrar")
    public UsuarioModel registrar(@RequestBody @Valid UsuarioRegistroDTO dto) {
        return authService.registrar(dto);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO dto) {
        return authService.login(dto);
    }

    @GetMapping("/cursos")
    public List<CursoModel> listarCursosParaCadastro() {
        return cursoService.listarTodos();
    }

    @PutMapping("/primeiro-acesso")
    public void alterarSenhaPrimeiroAcesso(
            Authentication authentication,
            @RequestBody @Valid PrimeiroAcessoSenhaDTO dto
    ) {
        authService.alterarSenhaPrimeiroAcesso(authentication.getName(), dto);
    }
}
