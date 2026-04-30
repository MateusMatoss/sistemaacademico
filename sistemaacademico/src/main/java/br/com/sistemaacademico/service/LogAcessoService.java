package br.com.sistemaacademico.service;

import br.com.sistemaacademico.model.LogAcessoModel;
import br.com.sistemaacademico.repository.LogAcessoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogAcessoService {

    private final LogAcessoRepository logAcessoRepository;

    public LogAcessoService(LogAcessoRepository logAcessoRepository) {
        this.logAcessoRepository = logAcessoRepository;
    }

    public List<LogAcessoModel> listarTodos() {
        return logAcessoRepository.findAll();
    }
}