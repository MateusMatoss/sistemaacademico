package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.LoginDTO;
import br.com.sistemaacademico.dto.LoginResponseDTO;
import br.com.sistemaacademico.dto.PrimeiroAcessoSenhaDTO;
import br.com.sistemaacademico.dto.UsuarioRegistroDTO;
import br.com.sistemaacademico.model.AlunoModel;
import br.com.sistemaacademico.model.CursoModel;
import br.com.sistemaacademico.exception.BusinessException;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.LogAcessoModel;
import br.com.sistemaacademico.model.PerfilUsuario;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.AlunoRepository;
import br.com.sistemaacademico.repository.CursoRepository;
import br.com.sistemaacademico.repository.LogAcessoRepository;
import br.com.sistemaacademico.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final LogAcessoRepository logAcessoRepository;

    public AuthService(
            UsuarioRepository usuarioRepository,
            AlunoRepository alunoRepository,
            CursoRepository cursoRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            LogAcessoRepository logAcessoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.logAcessoRepository = logAcessoRepository;
    }

    public UsuarioModel registrar(UsuarioRegistroDTO dto) {
        String username = resolverUsername(dto);

        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new BusinessException("Já existe um usuário cadastrado com esse username");
        }

        UsuarioModel usuario = new UsuarioModel();

        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        PerfilUsuario perfil = dto.getPerfil() == null
                ? PerfilUsuario.ALUNO
                : dto.getPerfil();

        usuario.setPerfil(perfil);
        usuario.setSenhaTemporaria(false);

        UsuarioModel usuarioSalvo = usuarioRepository.save(usuario);

        if (perfil == PerfilUsuario.ALUNO) {
            validarCamposObrigatoriosAluno(dto);
            criarAlunoVinculado(dto);
        }

        return usuarioSalvo;
    }

    private void validarCamposObrigatoriosAluno(UsuarioRegistroDTO dto) {
        if (dto.getNomePessoa() == null || dto.getNomePessoa().isBlank()) {
            throw new BusinessException("O nome do aluno é obrigatório para cadastro de aluno");
        }

        if (dto.getCpfPessoa() == null || dto.getCpfPessoa().isBlank()) {
            throw new BusinessException("O CPF do aluno é obrigatório para cadastro de aluno");
        }

        if (dto.getEmailPessoa() == null || dto.getEmailPessoa().isBlank()) {
            throw new BusinessException("O e-mail do aluno é obrigatório para cadastro de aluno");
        }
    }

    private void criarAlunoVinculado(UsuarioRegistroDTO dto) {
        if (alunoRepository.findByCpfPessoa(dto.getCpfPessoa()).isPresent()) {
            throw new BusinessException("Já existe um aluno cadastrado com esse CPF");
        }

        if (alunoRepository.findByEmailPessoaIgnoreCase(dto.getEmailPessoa()).isPresent()) {
            throw new BusinessException("Já existe um aluno cadastrado com esse e-mail");
        }

        AlunoModel aluno = new AlunoModel();
        aluno.setNomePessoa(dto.getNomePessoa());
        aluno.setCpfPessoa(dto.getCpfPessoa());
        aluno.setEmailPessoa(dto.getEmailPessoa());
        aluno.setRaAluno(gerarRaAluno());

        if (dto.getIdCurso() != null) {
            CursoModel curso = cursoRepository.findById(dto.getIdCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
            aluno.setCurso(curso);
        }

        alunoRepository.save(aluno);
    }

    private String resolverUsername(UsuarioRegistroDTO dto) {
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            return dto.getUsername().trim();
        }

        if (dto.getEmailPessoa() != null && !dto.getEmailPessoa().isBlank()) {
            return dto.getEmailPessoa().trim().toLowerCase();
        }

        throw new BusinessException("O e-mail do aluno é obrigatório para gerar o usuário de acesso");
    }

    private String gerarRaAluno() {
        String ra;

        do {
            ra = "RA" + System.currentTimeMillis();
        } while (alunoRepository.findByRaAluno(ra).isPresent());

        return ra;
    }

    public LoginResponseDTO login(LoginDTO dto) {
        UsuarioModel usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (usuario.getAtivo() == null || !usuario.getAtivo()) {
            throw new BusinessException("Usuário inativo. Entre em contato com o administrador.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new BusinessException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario.getUsername());

        return new LoginResponseDTO(
                token,
                usuario.getPerfil().name(),
                Boolean.TRUE.equals(usuario.getSenhaTemporaria())
        );
    }

    public void alterarSenhaPrimeiroAcesso(String username, PrimeiroAcessoSenhaDTO dto) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!Boolean.TRUE.equals(usuario.getSenhaTemporaria())) {
            throw new BusinessException("Esse usuário não possui troca obrigatória de senha pendente");
        }

        if (!dto.getNovaSenha().equals(dto.getConfirmarSenha())) {
            throw new BusinessException("A confirmação da senha não confere");
        }

        usuario.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
        usuario.setSenhaTemporaria(false);
        usuarioRepository.save(usuario);
    }
}
