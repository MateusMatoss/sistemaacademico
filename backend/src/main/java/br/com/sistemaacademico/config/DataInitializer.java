package br.com.sistemaacademico.config;

import br.com.sistemaacademico.model.PerfilUsuario;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_SEED_USERNAME = "gestor.epalavra.2026";
    private static final String ADMIN_SEED_PASSWORD = "123456";
    private static final String PROFESSOR_SEED_USERNAME = "professor@epalavra.edu";
    private static final String PROFESSOR_SEED_PASSWORD = "123456";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        usuarioRepository.findByUsername(ADMIN_USERNAME).ifPresentOrElse(adminExistente -> {
            boolean precisaSalvar = false;

            if (adminExistente.getAtivo() == null || !adminExistente.getAtivo()) {
                adminExistente.setAtivo(true);
                precisaSalvar = true;
            }

            if (adminExistente.getPerfil() != PerfilUsuario.ADMIN) {
                adminExistente.setPerfil(PerfilUsuario.ADMIN);
                precisaSalvar = true;
            }

            if (precisaSalvar) {
                usuarioRepository.save(adminExistente);
                System.out.println("Usuário administrador reativado com sucesso!");
            }
        }, () -> {
            UsuarioModel admin = new UsuarioModel();

            admin.setUsername(ADMIN_USERNAME);
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setPerfil(PerfilUsuario.ADMIN);
            admin.setAtivo(true);

            usuarioRepository.save(admin);

            System.out.println("Usuário administrador criado com sucesso!");
        });

        if (usuarioRepository.findByUsername(ADMIN_SEED_USERNAME).isEmpty()) {
            UsuarioModel adminSeed = new UsuarioModel();

            adminSeed.setUsername(ADMIN_SEED_USERNAME);
            adminSeed.setPassword(passwordEncoder.encode(ADMIN_SEED_PASSWORD));
            adminSeed.setPerfil(PerfilUsuario.ADMIN);
            adminSeed.setAtivo(true);

            usuarioRepository.save(adminSeed);

            System.out.println("Usuário administrador alternativo criado com sucesso!");
            System.out.println("Login: " + ADMIN_SEED_USERNAME + " | Senha: " + ADMIN_SEED_PASSWORD);
        }

        usuarioRepository.findByUsername(PROFESSOR_SEED_USERNAME).ifPresentOrElse(professorExistente -> {
            boolean precisaSalvar = false;

            if (professorExistente.getAtivo() == null || !professorExistente.getAtivo()) {
                professorExistente.setAtivo(true);
                precisaSalvar = true;
            }

            if (professorExistente.getPerfil() != PerfilUsuario.PROFESSOR) {
                professorExistente.setPerfil(PerfilUsuario.PROFESSOR);
                precisaSalvar = true;
            }

            if (professorExistente.getSenhaTemporaria() == null || !professorExistente.getSenhaTemporaria()) {
                professorExistente.setSenhaTemporaria(true);
                precisaSalvar = true;
            }

            if (precisaSalvar) {
                usuarioRepository.save(professorExistente);
            }
        }, () -> {
            UsuarioModel professorSeed = new UsuarioModel();

            professorSeed.setUsername(PROFESSOR_SEED_USERNAME);
            professorSeed.setPassword(passwordEncoder.encode(PROFESSOR_SEED_PASSWORD));
            professorSeed.setPerfil(PerfilUsuario.PROFESSOR);
            professorSeed.setAtivo(true);
            professorSeed.setSenhaTemporaria(true);

            usuarioRepository.save(professorSeed);

            System.out.println("Usuário professor de teste criado com sucesso!");
            System.out.println("Login: " + PROFESSOR_SEED_USERNAME + " | Senha: " + PROFESSOR_SEED_PASSWORD);
        });
    }
}
