package br.com.sistemaacademico.service;

import br.com.sistemaacademico.model.LogAcessoModel;
import br.com.sistemaacademico.repository.LogAcessoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Page<LogAcessoModel> listarPaginado(Pageable pageable) {
        return logAcessoRepository.findAll(pageable);
    }

    public List<LogAcessoModel> listarPorUsername(String username) {
        return logAcessoRepository.findByUsername(username);
    }

    public List<LogAcessoModel> listarPorPerfil(String perfil) {
        return logAcessoRepository.findByPerfil(perfil.toUpperCase());
    }

    public List<LogAcessoModel> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(23, 59, 59);

        return logAcessoRepository.findByDataHoraLoginBetween(inicio, fim);
    }
}