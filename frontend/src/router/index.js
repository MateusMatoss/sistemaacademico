import { createRouter, createWebHistory } from "vue-router";
import DashboardView from "../views/DashboardView.vue";
import LoginView from "../views/LoginView.vue";
import AlunosView from "../views/AlunosView.vue";
import ProfessoresView from "../views/ProfessoresView.vue";
import CursosView from "../views/CursosView.vue";
import DisciplinasView from "../views/DisciplinasView.vue";
import MatriculasView from "../views/MatriculasView.vue";
import NotasView from "../views/NotasView.vue";
import FrequenciasView from "../views/FrequenciasView.vue";
import UsuariosView from "../views/UsuariosView.vue";
import MeusDadosView from "../views/MeusDadosView.vue";
import MeuCursoView from "../views/MeuCursoView.vue";
import MinhasDisciplinasView from "../views/MinhasDisciplinasView.vue";
import MinhasNotasView from "../views/MinhasNotasView.vue";
import MinhaFrequenciaView from "../views/MinhaFrequenciaView.vue";
import PrimeiroAcessoView from "../views/PrimeiroAcessoView.vue";
import authService from "../services/authService";
import { setUnauthorizedHandler } from "../services/api";

const routes = [
  {
    path: "/",
    redirect: () => authService.isAuthenticated() ? authService.getDefaultRoute() : "/login",
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: {
      guestOnly: true,
      title: "Login",
    },
  },
  {
    path: "/primeiro-acesso",
    name: "primeiro-acesso",
    component: PrimeiroAcessoView,
    meta: {
      requiresAuth: true,
      standalone: true,
      title: "Trocar senha",
      allowedPerfis: ["PROFESSOR"],
    },
  },
  {
    path: "/dashboard",
    name: "dashboard",
    component: DashboardView,
    meta: {
      requiresAuth: true,
      title: "Dashboard",
      allowedPerfis: ["ADMIN", "PROFESSOR", "ALUNO"],
    },
  },
  {
    path: "/alunos",
    name: "alunos",
    component: AlunosView,
    meta: {
      requiresAuth: true,
      title: "Alunos",
      allowedPerfis: ["ADMIN", "PROFESSOR", "ALUNO"],
    },
  },
  {
    path: "/professores",
    name: "professores",
    component: ProfessoresView,
    meta: {
      requiresAuth: true,
      title: "Professores",
      allowedPerfis: ["ADMIN"],
    },
  },
  {
    path: "/cursos",
    name: "cursos",
    component: CursosView,
    meta: {
      requiresAuth: true,
      title: "Cursos",
      allowedPerfis: ["ADMIN", "PROFESSOR"],
    },
  },
  {
    path: "/disciplinas",
    name: "disciplinas",
    component: DisciplinasView,
    meta: {
      requiresAuth: true,
      title: "Disciplinas",
      allowedPerfis: ["ADMIN", "PROFESSOR"],
    },
  },
  {
    path: "/matriculas",
    name: "matriculas",
    component: MatriculasView,
    meta: {
      requiresAuth: true,
      title: "Matrículas",
      allowedPerfis: ["ADMIN"],
    },
  },
  {
    path: "/notas",
    name: "notas",
    component: NotasView,
    meta: {
      requiresAuth: true,
      title: "Notas",
      allowedPerfis: ["ADMIN", "PROFESSOR"],
    },
  },
  {
    path: "/frequencias",
    name: "frequencias",
    component: FrequenciasView,
    meta: {
      requiresAuth: true,
      title: "Frequências",
      allowedPerfis: ["ADMIN", "PROFESSOR"],
    },
  },
  {
    path: "/usuarios",
    name: "usuarios",
    component: UsuariosView,
    meta: {
      requiresAuth: true,
      title: "Usuários",
      allowedPerfis: ["ADMIN"],
    },
  },
  {
    path: "/meus-dados",
    name: "meus-dados",
    component: MeusDadosView,
    meta: {
      requiresAuth: true,
      title: "Meus Dados",
      allowedPerfis: ["ALUNO"],
    },
  },
  {
    path: "/meu-curso",
    name: "meu-curso",
    component: MeuCursoView,
    meta: {
      requiresAuth: true,
      title: "Meu Curso",
      allowedPerfis: ["ALUNO"],
    },
  },
  {
    path: "/minhas-disciplinas",
    name: "minhas-disciplinas",
    component: MinhasDisciplinasView,
    meta: {
      requiresAuth: true,
      title: "Minhas Disciplinas",
      allowedPerfis: ["ALUNO"],
    },
  },
  {
    path: "/minhas-notas",
    name: "minhas-notas",
    component: MinhasNotasView,
    meta: {
      requiresAuth: true,
      title: "Minhas Notas",
      allowedPerfis: ["ALUNO"],
    },
  },
  {
    path: "/minha-frequencia",
    name: "minha-frequencia",
    component: MinhaFrequenciaView,
    meta: {
      requiresAuth: true,
      title: "Minha Frequência",
      allowedPerfis: ["ALUNO"],
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const isAuthenticated = authService.isAuthenticated();
  const perfil = authService.getPerfil();
  const needsPasswordChange = authService.needsPasswordChange();

  if (to.meta.requiresAuth && !isAuthenticated) {
    next("/login");
    return;
  }

  if (to.meta.guestOnly && isAuthenticated) {
    next(needsPasswordChange ? "/primeiro-acesso" : authService.getDefaultRoute());
    return;
  }

  if (isAuthenticated && needsPasswordChange && to.name !== "primeiro-acesso") {
    next("/primeiro-acesso");
    return;
  }

  if (isAuthenticated && !needsPasswordChange && to.name === "primeiro-acesso") {
    next(authService.getDefaultRoute());
    return;
  }

  if (
    to.meta.requiresAuth &&
    Array.isArray(to.meta.allowedPerfis) &&
    !to.meta.allowedPerfis.includes(perfil)
  ) {
    next(authService.getDefaultRoute());
    return;
  }

  next();
});

setUnauthorizedHandler(() => {
  if (router.currentRoute.value.path !== "/login") {
    router.push("/login");
  }
});

export default router;
