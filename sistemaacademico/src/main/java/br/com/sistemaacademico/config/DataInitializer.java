package br.com.sistemaacademico.config;

import br.com.sistemaacademico.model.PerfilUsuario;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

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

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            UsuarioModel admin = new UsuarioModel();

            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setPerfil(PerfilUsuario.ADMIN);

            usuarioRepository.save(admin);

            System.out.println("Usuário administrador criado com sucesso!");
        }
    }
}