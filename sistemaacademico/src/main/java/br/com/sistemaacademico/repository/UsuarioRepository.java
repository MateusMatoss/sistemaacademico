package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.PerfilUsuario;
import br.com.sistemaacademico.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByUsername(String username);

    List<UsuarioModel> findByAtivo(Boolean ativo);

    List<UsuarioModel> findByPerfil(PerfilUsuario perfil);
}