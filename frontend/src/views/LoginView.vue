<template>
  <section class="login-shell">
    <div class="login-brand">
      <div>
        <span class="login-kicker">Plataforma institucional</span>
        <h1>Sistema Acadêmico<br />Espalhando a Palavra</h1>
        <p>
          Ambiente centralizado para gestão de alunos, professores, cursos,
          disciplinas, matrículas, notas, frequências e usuários.
        </p>

        <div class="login-badges">
          <span>JWT</span>
          <span>Vue 3</span>
          <span>Spring Boot</span>
        </div>
      </div>

      <p>
        Organize o núcleo acadêmico em uma única interface responsiva,
        conectada diretamente ao backend em <strong>http://localhost:8080</strong>.
      </p>
    </div>

    <div class="login-panel">
      <div class="login-card">
        <div class="auth-switch">
          <button
            type="button"
            :class="['auth-switch__item', { active: activePanel === 'login' }]"
            @click="activePanel = 'login'"
          >
            Entrar
          </button>
          <button
            type="button"
            :class="['auth-switch__item', { active: activePanel === 'register' }]"
            @click="activePanel = 'register'"
          >
            Criar cadastro
          </button>
          <button
            type="button"
            :class="['auth-switch__item', { active: activePanel === 'forgot' }]"
            @click="activePanel = 'forgot'"
          >
            Esqueci a senha
          </button>
        </div>

        <template v-if="activePanel === 'login'">
          <h2>Acessar o sistema</h2>
          <p>Informe suas credenciais para carregar o painel do sistema.</p>

          <BaseAlert v-if="errorMessage" :message="errorMessage" type="error" />

          <form class="form-grid" @submit.prevent="submitLogin">
            <div class="field">
              <label for="username">Usuário</label>
              <input
                id="username"
                v-model="loginForm.username"
                type="text"
                placeholder="Digite seu usuário ou e-mail"
                required
              />
            </div>

            <div class="field">
              <label for="password">Senha</label>
              <input
                id="password"
                v-model="loginForm.password"
                type="password"
                placeholder="Digite sua senha"
                required
              />
            </div>

            <button type="submit" class="btn btn-primary" :disabled="loading">
              {{ loading ? "Entrando..." : "Entrar" }}
            </button>
          </form>

          <p class="login-hint">
            Usuário alternativo de teste: <strong>gestor.epalavra.2026</strong> /
            <strong>123456</strong>
          </p>
        </template>

        <template v-else-if="activePanel === 'register'">
          <h2>Criar novo cadastro de aluno</h2>
          <p>
            Este cadastro cria o acesso e o aluno no sistema. O e-mail informado
            será usado como login, e o RA será gerado automaticamente.
          </p>

          <BaseAlert
            v-if="registerMessage"
            :message="registerMessage"
            :type="registerMessageType"
          />

          <form class="form-grid" @submit.prevent="submitRegister">
            <div class="field">
              <label for="register-password">Senha</label>
              <input
                id="register-password"
                v-model="registerForm.password"
                type="password"
                placeholder="Crie uma senha"
                required
              />
            </div>

            <div class="field">
              <label for="register-nome">Nome completo</label>
              <input
                id="register-nome"
                v-model="registerForm.nomePessoa"
                type="text"
                placeholder="Nome do aluno"
                required
              />
            </div>

            <div class="field">
              <label for="register-cpf">CPF</label>
              <input
                id="register-cpf"
                v-model="registerForm.cpfPessoa"
                type="text"
                placeholder="000.000.000-00"
                required
              />
            </div>

            <div class="field">
              <label for="register-email">E-mail</label>
              <input
                id="register-email"
                v-model="registerForm.emailPessoa"
                type="email"
                placeholder="aluno@exemplo.com"
                required
              />
            </div>

            <div class="field">
              <label for="register-curso">Curso</label>
              <select
                id="register-curso"
                v-model="registerForm.idCurso"
                required
              >
                <option value="">Selecione um curso</option>
                <option
                  v-for="curso in cursos"
                  :key="curso.idCurso"
                  :value="curso.idCurso"
                >
                  {{ curso.nomeCurso }}
                </option>
              </select>
            </div>

            <button
              type="submit"
              class="btn btn-primary"
              :disabled="registerLoading"
            >
              {{ registerLoading ? "Cadastrando..." : "Cadastrar aluno" }}
            </button>
          </form>
        </template>

        <template v-else>
          <h2>Esqueci minha senha</h2>
          <p>
            A API atual ainda não possui fluxo automático de recuperação de senha.
            Use uma das opções abaixo para continuar.
          </p>

          <div class="forgot-box">
            <div class="forgot-step">
              <strong>1. Se você ainda não tem acesso</strong>
              <span>Use a aba <em>Criar cadastro</em> para registrar um novo usuário.</span>
            </div>
            <div class="forgot-step">
              <strong>2. Se já existe um usuário no sistema</strong>
              <span>Solicite ao administrador a redefinição da senha pela área de usuários.</span>
            </div>
            <div class="forgot-step">
              <strong>3. Se você é administrador</strong>
              <span>Faça login com um usuário ativo e altere a senha no backend ou na tela de usuários quando esse fluxo estiver disponível.</span>
            </div>
          </div>

          <button type="button" class="btn btn-primary" @click="activePanel = 'login'">
            Voltar para o login
          </button>
        </template>
      </div>
    </div>
  </section>
</template>

<script>
import BaseAlert from "../components/BaseAlert.vue";
import authService from "../services/authService";
import { getErrorMessage } from "../services/api";

const createEmptyRegisterForm = () => ({
  password: "",
  nomePessoa: "",
  cpfPessoa: "",
  emailPessoa: "",
  idCurso: "",
});

export default {
  name: "LoginView",
  components: {
    BaseAlert,
  },
  data() {
    return {
      activePanel: "login",
      cursos: [],
      loginForm: {
        username: "gestor.epalavra.2026",
        password: "123456",
      },
      registerForm: createEmptyRegisterForm(),
      loading: false,
      registerLoading: false,
      errorMessage: "",
      registerMessage: "",
      registerMessageType: "info",
    };
  },
  async created() {
    try {
      this.cursos = await authService.listRegistrationCourses();
    } catch {
      this.cursos = [];
    }
  },
  methods: {
    async submitLogin() {
      this.loading = true;
      this.errorMessage = "";

      try {
        await authService.login(this.loginForm);
        this.$router.push(
          authService.needsPasswordChange() ? "/primeiro-acesso" : "/"
        );
      } catch (error) {
        this.errorMessage = getErrorMessage(error);
      } finally {
        this.loading = false;
      }
    },
    async submitRegister() {
      this.registerLoading = true;
      this.registerMessage = "";
      this.registerMessageType = "info";

      try {
        const payload = {
          password: this.registerForm.password,
          perfil: "ALUNO",
          nomePessoa: this.registerForm.nomePessoa,
          cpfPessoa: this.registerForm.cpfPessoa,
          emailPessoa: this.registerForm.emailPessoa,
          idCurso: Number(this.registerForm.idCurso),
        };

        await authService.register(payload);

        this.registerMessage =
          "Cadastro realizado com sucesso. Use seu e-mail e a senha criada para entrar.";
        this.registerMessageType = "success";
        this.loginForm.username = this.registerForm.emailPessoa;
        this.loginForm.password = this.registerForm.password;
        this.registerForm = createEmptyRegisterForm();
        this.activePanel = "login";
      } catch (error) {
        this.registerMessage = getErrorMessage(error);
        this.registerMessageType = "error";
      } finally {
        this.registerLoading = false;
      }
    },
  },
};
</script>

<style scoped>
.auth-switch {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin-bottom: 24px;
  padding: 6px;
  background: #eef3fb;
  border-radius: 16px;
}

.auth-switch__item {
  border: 0;
  border-radius: 12px;
  padding: 11px 10px;
  font-weight: 700;
  color: #415067;
  background: transparent;
  cursor: pointer;
}

.auth-switch__item.active {
  background: #ffffff;
  color: #1d4ed8;
  box-shadow: 0 8px 18px rgba(15, 23, 42, 0.08);
}

.login-kicker {
  display: block;
  font-size: 12px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.58);
  margin-bottom: 8px;
}

.forgot-box {
  display: grid;
  gap: 14px;
  margin-bottom: 22px;
}

.forgot-step {
  display: grid;
  gap: 6px;
  padding: 16px;
  border-radius: 16px;
  background: #f8fbff;
  border: 1px solid #dbe7f6;
}

.forgot-step strong {
  color: #1d4ed8;
}

.forgot-step span {
  color: #51627b;
  line-height: 1.5;
}

@media (max-width: 720px) {
  .auth-switch {
    grid-template-columns: 1fr;
  }
}
</style>
