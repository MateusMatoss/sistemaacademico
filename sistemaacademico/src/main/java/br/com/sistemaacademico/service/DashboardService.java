package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.DashboardResumoDTO;
import br.com.sistemaacademico.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final CursoRepository cursoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final MatriculaRepository matriculaRepository;
    private final NotaRepository notaRepository;
    private final FrequenciaRepository frequenciaRepository;

    public DashboardService(
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository,
            CursoRepository cursoRepository,
            DisciplinaRepository disciplinaRepository,
            MatriculaRepository matriculaRepository,
            NotaRepository notaRepository,
            FrequenciaRepository frequenciaRepository
    ) {
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.cursoRepository = cursoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.matriculaRepository = matriculaRepository;
        this.notaRepository = notaRepository;
        this.frequenciaRepository = frequenciaRepository;
    }

    public DashboardResumoDTO gerarResumoGeral() {
        return new DashboardResumoDTO(
                alunoRepository.count(),
                professorRepository.count(),
                cursoRepository.count(),
                disciplinaRepository.count(),
                matriculaRepository.count(),
                notaRepository.count(),
                frequenciaRepository.count()
        );
    }
}