package br.com.sistemaacademico.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaacademico.dto.ProfessorRequestDTO;
import br.com.sistemaacademico.dto.ProfessorResumoDTO;
import br.com.sistemaacademico.exception.BusinessException;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.DisciplinaModel;
import br.com.sistemaacademico.model.PerfilUsuario;
import br.com.sistemaacademico.model.ProfessorModel;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.DisciplinaRepository;
import br.com.sistemaacademico.repository.ProfessorRepository;
import br.com.sistemaacademico.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfessorService(
            ProfessorRepository professorRepository,
            DisciplinaRepository disciplinaRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.professorRepository = professorRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ProfessorModel> listarTodos() {
        return professorRepository.findAll();
    }

    public ProfessorModel buscarPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
    }

    public ProfessorModel salvar(ProfessorRequestDTO dto) {
        validarDisponibilidadeUsuarioProfessor(dto.getEmailPessoa(), null);

        ProfessorModel professor = new ProfessorModel();

        professor.setNomePessoa(dto.getNomePessoa());
        professor.setCpfPessoa(dto.getCpfPessoa());
        professor.setEmailPessoa(dto.getEmailPessoa());
        professor.setFormacaoProfessor(dto.getFormacaoProfessor());
        professor.setTitulacaoProfessor(dto.getTitulacaoProfessor());

        ProfessorModel professorSalvo = professorRepository.save(professor);
        criarUsuarioProfessorSeNecessario(dto);

        return professorSalvo;
    }

    public ProfessorModel atualizar(Long id, ProfessorRequestDTO dto) {
        ProfessorModel professorExistente = buscarPorId(id);
        String emailAnterior = professorExistente.getEmailPessoa();

        validarDisponibilidadeUsuarioProfessor(dto.getEmailPessoa(), emailAnterior);

        professorExistente.setNomePessoa(dto.getNomePessoa());
        professorExistente.setCpfPessoa(dto.getCpfPessoa());
        professorExistente.setEmailPessoa(dto.getEmailPessoa());
        professorExistente.setFormacaoProfessor(dto.getFormacaoProfessor());
        professorExistente.setTitulacaoProfessor(dto.getTitulacaoProfessor());

        ProfessorModel professorAtualizado = professorRepository.save(professorExistente);
        sincronizarUsuarioProfessor(emailAnterior, dto);

        return professorAtualizado;
    }

    public Page<ProfessorModel> listarPaginado(Pageable pageable) {
        return professorRepository.findAll(pageable);
    }

    public List<ProfessorModel> buscarPorNome(String nome) {
        return professorRepository.findByNomePessoaContainingIgnoreCase(nome);
    }

    public ProfessorModel buscarPorCpf(String cpf) {
        return professorRepository.findByCpfPessoa(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado pelo CPF informado"));
    }

    public void deletar(Long id) {
        ProfessorModel professor = buscarPorId(id);
        removerUsuarioProfessorSeExistir(professor.getEmailPessoa());
        professorRepository.delete(professor);
    }

    public List<ProfessorResumoDTO> gerarResumoProfessor(Long idProfessor) {
        ProfessorModel professor = buscarPorId(idProfessor);

        List<DisciplinaModel> disciplinas = disciplinaRepository.findByProfessorIdPessoa(idProfessor);

        return disciplinas.stream().map(disciplina -> {
            String nomeCurso = disciplina.getCurso() != null
                    ? disciplina.getCurso().getNomeCurso()
                    : "Sem curso";

            return new ProfessorResumoDTO(
                    professor.getIdPessoa(),
                    professor.getNomePessoa(),
                    professor.getFormacaoProfessor(),
                    professor.getTitulacaoProfessor(),
                    disciplina.getNomeDisciplina(),
                    nomeCurso,
                    disciplina.getCargaHoraria()
            );
        }).toList();
    }

    private void criarUsuarioProfessorSeNecessario(ProfessorRequestDTO dto) {
        String username = normalizarUsernameProfessor(dto.getEmailPessoa());

        if (usuarioRepository.findByUsername(username).isPresent()) {
            return;
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(extrairSenhaInicialProfessor(dto.getCpfPessoa())));
        usuario.setPerfil(PerfilUsuario.PROFESSOR);
        usuario.setAtivo(true);
        usuario.setSenhaTemporaria(true);

        usuarioRepository.save(usuario);
    }

    private void sincronizarUsuarioProfessor(String emailAnterior, ProfessorRequestDTO dto) {
        String usernameAnterior = normalizarUsernameProfessor(emailAnterior);
        String novoUsername = normalizarUsernameProfessor(dto.getEmailPessoa());

        UsuarioModel usuario = usuarioRepository.findByUsername(usernameAnterior).orElse(null);

        if (usuario == null) {
            criarUsuarioProfessorSeNecessario(dto);
            return;
        }

        if (usuario.getPerfil() != PerfilUsuario.PROFESSOR) {
            throw new BusinessException("O e-mail informado já está vinculado a um usuário que não é professor");
        }

        usuario.setUsername(novoUsername);
        usuario.setPerfil(PerfilUsuario.PROFESSOR);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    private void removerUsuarioProfessorSeExistir(String emailProfessor) {
        String username = normalizarUsernameProfessor(emailProfessor);
        UsuarioModel usuario = usuarioRepository.findByUsername(username).orElse(null);

        if (usuario != null && usuario.getPerfil() == PerfilUsuario.PROFESSOR) {
            usuarioRepository.delete(usuario);
        }
    }

    private void validarDisponibilidadeUsuarioProfessor(String emailNovo, String emailAnterior) {
        String novoUsername = normalizarUsernameProfessor(emailNovo);
        String usernameAnterior = emailAnterior == null ? null : normalizarUsernameProfessor(emailAnterior);

        usuarioRepository.findByUsername(novoUsername).ifPresent(usuario -> {
            boolean mesmoRegistro = usernameAnterior != null && usernameAnterior.equalsIgnoreCase(novoUsername);

            if (!mesmoRegistro) {
                throw new BusinessException("Já existe um usuário cadastrado com o e-mail informado");
            }
        });
    }

    private String normalizarUsernameProfessor(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private String extrairSenhaInicialProfessor(String cpf) {
        String somenteNumeros = cpf == null ? "" : cpf.replaceAll("\\D", "");

        if (somenteNumeros.length() >= 6) {
            return somenteNumeros.substring(somenteNumeros.length() - 6);
        }

        throw new BusinessException("Não foi possível gerar a senha inicial do professor a partir do CPF informado");
    }
}
