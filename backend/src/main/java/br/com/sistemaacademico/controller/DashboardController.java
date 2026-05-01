package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.DashboardResumoDTO;
import br.com.sistemaacademico.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard/resumo")
    public DashboardResumoDTO gerarResumoGeral() {
        return dashboardService.gerarResumoGeral();
    }
}