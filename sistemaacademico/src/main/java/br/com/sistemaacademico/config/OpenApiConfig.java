package br.com.sistemaacademico.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sistemaAcademicoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Acadêmico - Espalhando a Palavra")
                        .description("API REST para gerenciamento acadêmico de alunos, professores, cursos, disciplinas, matrículas, notas e frequências.")
                        .version("1.0.0"));
    }
}