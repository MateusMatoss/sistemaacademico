import api from "./api";

const TOKEN_KEY = "sistemaacademico.token";
const PERFIL_KEY = "sistemaacademico.perfil";
const SENHA_TEMPORARIA_KEY = "sistemaacademico.senhaTemporaria";

const PERFIL_ACCESS = {
  ADMIN: [
    "dashboard",
    "alunos",
    "professores",
    "cursos",
    "disciplinas",
    "matriculas",
    "notas",
    "frequencias",
    "usuarios",
  ],
  PROFESSOR: [
    "dashboard",
    "alunos",
    "cursos",
    "disciplinas",
    "notas",
    "frequencias",
  ],
  ALUNO: [
    "meus-dados",
    "meu-curso",
    "minhas-disciplinas",
    "minhas-notas",
    "minha-frequencia",
  ],
};

export default {
  async listRegistrationCourses() {
    const { data } = await api.get("/auth/cursos");
    return data;
  },
  async register(payload) {
    const { data } = await api.post("/auth/registrar", payload);
    return data;
  },
  async login(credentials) {
    const { data } = await api.post("/auth/login", credentials);

    localStorage.setItem(TOKEN_KEY, data.token);
    localStorage.setItem(PERFIL_KEY, data.perfil);
    localStorage.setItem(
      SENHA_TEMPORARIA_KEY,
      data.senhaTemporaria ? "true" : "false"
    );

    return data;
  },
  async changeTemporaryPassword(payload) {
    const { data } = await api.put("/auth/primeiro-acesso", payload);
    localStorage.setItem(SENHA_TEMPORARIA_KEY, "false");
    return data;
  },
  logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(PERFIL_KEY);
    localStorage.removeItem(SENHA_TEMPORARIA_KEY);
  },
  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },
  getPerfil() {
    return localStorage.getItem(PERFIL_KEY);
  },
  needsPasswordChange() {
    return localStorage.getItem(SENHA_TEMPORARIA_KEY) === "true";
  },
  isAuthenticated() {
    return Boolean(localStorage.getItem(TOKEN_KEY));
  },
  canAccessRoute(routeName) {
    const perfil = this.getPerfil();

    if (!perfil || !routeName) {
      return false;
    }

    return PERFIL_ACCESS[perfil]?.includes(routeName) ?? false;
  },
  getAllowedRoutes() {
    const perfil = this.getPerfil();
    return PERFIL_ACCESS[perfil] || [];
  },
  getDefaultRoute() {
    const firstRoute = this.getAllowedRoutes()[0];
    return firstRoute ? `/${firstRoute}`.replace("/meus-dados", "/meus-dados").replace("/meu-curso", "/meu-curso").replace("/minhas-disciplinas", "/minhas-disciplinas").replace("/minhas-notas", "/minhas-notas").replace("/minha-frequencia", "/minha-frequencia") : "/login";
  },
};
