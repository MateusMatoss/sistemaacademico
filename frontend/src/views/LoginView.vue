<template>
  <section class="auth-scene">
    <div class="auth-scene__veil"></div>
    <div class="auth-scene__grid"></div>

    <div class="auth-floating auth-floating--book">📘</div>
    <div class="auth-floating auth-floating--cap">🎓</div>
    <div class="auth-floating auth-floating--atom">✦</div>
    <div class="auth-floating auth-floating--laptop">⌘</div>
    <div class="auth-floating auth-floating--certificate">✦</div>

    <div class="auth-layout">
      <div :class="['auth-card', `auth-card--${activePanel}`]">
        <div class="auth-card__brand">
          <img
            class="auth-card__logo"
            src="/logosistema.png"
            alt="Logo do Sistema Acadêmico"
          />

          <div>
            <strong>Sistema Acadêmico</strong>
            <span>Espalhando a Palavra</span>
          </div>
        </div>

        <template v-if="activePanel === 'login'">
          <header class="auth-card__header">
            <h2>Bem-vindo de volta!</h2>
            <p>Acesse sua conta para continuar.</p>
          </header>

          <BaseAlert v-if="errorMessage" :message="errorMessage" type="error" />

          <form class="auth-form" @submit.prevent="submitLogin">
            <label class="auth-field">
              <span class="auth-field__label">E-mail ou usuário</span>
              <div class="auth-field__box">
                <span class="auth-field__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M4 6h16v12H4z" />
                    <path d="m4 8 8 6 8-6" />
                  </svg>
                </span>

                <input
                  id="username"
                  v-model="loginForm.username"
                  type="text"
                  placeholder="exemplo@instituicao.edu.br"
                  autocomplete="username"
                  required
                />
              </div>
            </label>

            <label class="auth-field">
              <span class="auth-field__label">Senha</span>
              <div class="auth-field__box">
                <span class="auth-field__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <rect x="4" y="11" width="16" height="10" rx="2" />
                    <path d="M8 11V8a4 4 0 1 1 8 0v3" />
                  </svg>
                </span>

                <input
                  id="password"
                  v-model="loginForm.password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="Digite sua senha"
                  autocomplete="current-password"
                  required
                />

                <button
                  type="button"
                  class="auth-field__toggle"
                  @click="showPassword = !showPassword"
                >
                  <svg v-if="showPassword" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M3 3 21 21" />
                    <path d="M10.58 10.58A2 2 0 0 0 12 14a2 2 0 0 0 1.42-.58" />
                    <path d="M9.88 5.09A9.77 9.77 0 0 1 12 4c5 0 9.27 3.11 11 8-0.51 1.44-1.33 2.75-2.39 3.84" />
                    <path d="M6.61 6.61C4.62 7.95 3.11 9.8 2 12c1.73 4.89 6 8 10 8a9.8 9.8 0 0 0 5.39-1.61" />
                  </svg>
                  <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M2 12s3.5-8 10-8 10 8 10 8-3.5 8-10 8S2 12 2 12Z" />
                    <circle cx="12" cy="12" r="3" />
                  </svg>
                </button>
              </div>
            </label>

            <div class="auth-row">
              <label class="auth-check">
                <input v-model="rememberMe" type="checkbox" />
                <span>Lembrar-me</span>
              </label>

              <button
                type="button"
                class="auth-link"
                @click="switchPanel('forgot')"
              >
                Esqueceu sua senha?
              </button>
            </div>

            <button type="submit" class="auth-submit" :disabled="loading">
              {{ loading ? "Entrando..." : "Acessar sistema" }}
            </button>
          </form>

          <footer class="auth-footer">
            <span>Não tem uma conta?</span>
            <button
              type="button"
              class="auth-footer__link"
              @click="switchPanel('register')"
            >
              Solicite acesso
            </button>
          </footer>
        </template>

        <template v-else-if="activePanel === 'register'">
          <header class="auth-card__header auth-card__header--register">
            <h2>Solicitar acesso</h2>
            <p>
              Preencha seus dados para criar o acesso do aluno no sistema.
            </p>
          </header>

          <BaseAlert
            v-if="registerMessage"
            :message="registerMessage"
            :type="registerMessageType"
          />

          <form class="auth-form auth-form--compact auth-form--register" @submit.prevent="submitRegister">
            <label class="auth-field">
              <span class="auth-field__label">Nome completo</span>
              <div class="auth-field__box">
                <span class="auth-field__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M20 21a8 8 0 1 0-16 0" />
                    <circle cx="12" cy="8" r="4" />
                  </svg>
                </span>
                <input
                  v-model="registerForm.nomePessoa"
                  type="text"
                  placeholder="Nome do aluno"
                  required
                />
              </div>
            </label>

            <label class="auth-field">
              <span class="auth-field__label">CPF</span>
              <div class="auth-field__box">
                <span class="auth-field__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <rect x="4" y="3" width="16" height="18" rx="2" />
                    <path d="M8 7h8M8 12h8M8 17h5" />
                  </svg>
                </span>
                <input
                  v-model="registerForm.cpfPessoa"
                  type="text"
                  placeholder="000.000.000-00"
                  required
                />
              </div>
            </label>

            <label class="auth-field">
              <span class="auth-field__label">E-mail</span>
              <div class="auth-field__box">
                <span class="auth-field__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M4 6h16v12H4z" />
                    <path d="m4 8 8 6 8-6" />
                  </svg>
                </span>
                <input
                  v-model="registerForm.emailPessoa"
                  type="email"
                  placeholder="aluno@instituicao.edu.br"
                  required
                />
              </div>
            </label>

            <label class="auth-field">
              <span class="auth-field__label">Senha</span>
              <div class="auth-field__box">
                <span class="auth-field__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <rect x="4" y="11" width="16" height="10" rx="2" />
                    <path d="M8 11V8a4 4 0 1 1 8 0v3" />
                  </svg>
                </span>
                <input
                  v-model="registerForm.password"
                  type="password"
                  placeholder="Crie uma senha"
                  required
                />
              </div>
            </label>

            <label class="auth-field">
              <span class="auth-field__label">Curso</span>
              <div class="auth-field__box auth-field__box--select">
                <span class="auth-field__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M12 3 2 8l10 5 10-5-10-5Z" />
                    <path d="M6 10.5V16c0 1.7 2.7 3 6 3s6-1.3 6-3v-5.5" />
                  </svg>
                </span>
                <select v-model="registerForm.idCurso" required>
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
            </label>

            <button type="submit" class="auth-submit" :disabled="registerLoading">
              {{ registerLoading ? "Enviando..." : "Solicitar acesso" }}
            </button>
          </form>

          <footer class="auth-footer auth-footer--back">
            <button
              type="button"
              class="auth-footer__link"
              @click="switchPanel('login')"
            >
              Voltar para o login
            </button>
          </footer>
        </template>

        <template v-else>
          <header class="auth-card__header auth-card__header--forgot">
            <h2>Recuperar acesso</h2>
            <p>
              O fluxo automático ainda não está disponível. Siga as orientações
              abaixo para continuar.
            </p>
          </header>

          <div class="auth-help">
            <div class="auth-help__item">
              <strong>Não possui acesso?</strong>
              <p>Use a opção de solicitação de acesso para criar seu cadastro de aluno.</p>
            </div>

            <div class="auth-help__item">
              <strong>Já possui conta?</strong>
              <p>Solicite ao administrador a redefinição da senha pela área de usuários.</p>
            </div>

            <div class="auth-help__item">
              <strong>Primeiro acesso de professor</strong>
              <p>Professores cadastrados pelo administrador devem trocar a senha após o primeiro login.</p>
            </div>
          </div>

          <footer class="auth-footer auth-footer--back">
            <button
              type="button"
              class="auth-footer__link"
              @click="switchPanel('login')"
            >
              Voltar para o login
            </button>
          </footer>
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
      rememberMe: true,
      showPassword: false,
      loginForm: {
        username: "",
        password: "",
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
    switchPanel(panel) {
      this.activePanel = panel;
      this.errorMessage = "";
      this.registerMessage = "";
    },
    async submitLogin() {
      this.loading = true;
      this.errorMessage = "";

      try {
        await authService.login(this.loginForm);

        // O sistema atual mantém a sessão em localStorage.
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
.auth-scene {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  background:
    radial-gradient(circle at top center, rgba(255, 255, 255, 0.92), rgba(255, 255, 255, 0.35) 28%, rgba(191, 219, 254, 0.28) 54%, rgba(147, 197, 253, 0.28) 100%),
    linear-gradient(115deg, rgba(240, 247, 255, 0.98) 0%, rgba(221, 236, 255, 0.92) 42%, rgba(243, 248, 255, 0.94) 100%);
}

.auth-scene__veil {
  position: absolute;
  inset: 0;
  background:
    linear-gradient(135deg, rgba(37, 99, 235, 0.08) 0%, transparent 32%),
    linear-gradient(315deg, rgba(14, 116, 144, 0.08) 0%, transparent 30%);
}

.auth-scene__grid {
  position: absolute;
  inset: -12% auto auto -8%;
  width: 54%;
  height: 120%;
  background:
    linear-gradient(135deg, rgba(59, 130, 246, 0.08) 25%, transparent 25%) -24px 0/240px 240px,
    linear-gradient(225deg, rgba(59, 130, 246, 0.08) 25%, transparent 25%) -24px 0/240px 240px,
    linear-gradient(315deg, rgba(59, 130, 246, 0.08) 25%, transparent 25%) 0 0/240px 240px,
    linear-gradient(45deg, rgba(59, 130, 246, 0.08) 25%, transparent 25%) 0 0/240px 240px;
  opacity: 0.9;
  transform: rotate(8deg);
}

.auth-layout {
  position: relative;
  z-index: 1;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
}

.auth-card {
  width: min(100%, 760px);
  min-height: auto;
  padding: 38px 44px 28px;
  border-radius: 34px;
  background: rgba(255, 255, 255, 0.94);
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow:
    0 30px 80px rgba(15, 23, 42, 0.12),
    0 8px 30px rgba(59, 130, 246, 0.08);
  backdrop-filter: blur(18px);
}

.auth-card__brand {
  display: inline-flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.auth-card__logo {
  width: 74px;
  height: 74px;
  object-fit: contain;
  flex: 0 0 74px;
}

.auth-card__brand strong {
  display: block;
  color: #0b2b63;
  font-size: 28px;
  line-height: 0.95;
  letter-spacing: -0.03em;
}

.auth-card__brand span {
  display: block;
  margin-top: 6px;
  color: #2d9be8;
  font-size: 18px;
  font-weight: 500;
  letter-spacing: -0.03em;
}

.auth-card__header h2 {
  margin: 0 0 12px;
  color: #0b2b63;
  font-size: 38px;
  line-height: 0.98;
  letter-spacing: -0.04em;
}

.auth-card__header p {
  margin: 0 0 18px;
  color: #1c3360;
  font-size: 17px;
}

.auth-form {
  display: grid;
  gap: 16px;
}

.auth-form--compact {
  gap: 14px;
}

.auth-form--register {
  gap: 14px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  column-gap: 14px;
}

.auth-field {
  display: grid;
  gap: 8px;
}

.auth-field__label {
  width: fit-content;
  padding: 0 10px;
  margin-left: 18px;
  color: #4f4f4f;
  font-size: 15px;
  line-height: 1;
  background: rgba(255, 255, 255, 0.96);
  transform: translateY(13px);
  position: relative;
  z-index: 1;
}

.auth-field__box {
  min-height: 66px;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 0 16px;
  border: 2px solid #35a7e6;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.4);
}

.auth-field__box:focus-within {
  border-color: #1483db;
  box-shadow:
    inset 0 0 0 1px rgba(255, 255, 255, 0.4),
    0 0 0 6px rgba(53, 167, 230, 0.12);
}

.auth-field__box input,
.auth-field__box select {
  flex: 1;
  min-width: 0;
  border: 0;
  outline: 0;
  background: transparent;
  color: #505050;
  font-size: 17px;
  line-height: 1.2;
}

.auth-field__box input::placeholder,
.auth-field__box select {
  color: #707070;
}

.auth-field__box select {
  appearance: none;
}

.auth-field__box--select::after {
  content: "";
  width: 12px;
  height: 12px;
  border-right: 2px solid #2668bf;
  border-bottom: 2px solid #2668bf;
  transform: rotate(45deg);
  margin-right: 4px;
}

.auth-field__icon {
  width: 28px;
  height: 28px;
  flex: 0 0 28px;
  color: #1f73ec;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.auth-field__icon svg,
.auth-field__toggle svg {
  width: 22px;
  height: 22px;
}

.auth-field__toggle {
  border: 0;
  background: transparent;
  color: #374151;
  padding: 0;
  cursor: pointer;
}

.auth-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-top: 2px;
}

.auth-check {
  display: inline-flex;
  align-items: center;
  gap: 14px;
  color: #0f223f;
  font-size: 16px;
}

.auth-check input {
  width: 18px;
  height: 18px;
  accent-color: #1f73ec;
}

.auth-link,
.auth-footer__link {
  border: 0;
  padding: 0;
  background: transparent;
  color: #2996df;
  font-size: 16px;
  cursor: pointer;
}

.auth-submit {
  min-height: 62px;
  border: 0;
  border-radius: 22px;
  color: #ffffff;
  font-size: 17px;
  font-weight: 800;
  letter-spacing: 0.02em;
  text-transform: uppercase;
  cursor: pointer;
  background: linear-gradient(180deg, #2285ff 0%, #166ee3 100%);
  box-shadow: 0 12px 28px rgba(34, 133, 255, 0.32);
}

.auth-submit:disabled {
  opacity: 0.75;
  cursor: not-allowed;
}

.auth-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 14px;
  color: #0f223f;
  font-size: 16px;
}

.auth-footer--back {
  justify-content: flex-start;
}

.auth-help {
  display: grid;
  gap: 16px;
}

.auth-help__item {
  padding: 20px 22px;
  border-radius: 20px;
  background: #f7fbff;
  border: 1px solid #d7ebfb;
}

.auth-help__item strong {
  display: block;
  margin-bottom: 8px;
  color: #0b2b63;
  font-size: 20px;
}

.auth-help__item p {
  margin: 0;
  color: #52627c;
  font-size: 17px;
  line-height: 1.6;
}

.auth-card__header--register h2,
.auth-card__header--forgot h2 {
  font-size: 30px;
}

.auth-card__header--register p,
.auth-card__header--forgot p {
  margin-bottom: 14px;
  font-size: 15px;
}

.auth-form--register .auth-field {
  gap: 6px;
}

.auth-form--register .auth-field__label {
  font-size: 14px;
  margin-left: 12px;
  transform: translateY(12px);
}

.auth-form--register .auth-field__box {
  min-height: 58px;
  padding: 0 14px;
  border-width: 2px;
  border-radius: 18px;
}

.auth-form--register .auth-field__box input,
.auth-form--register .auth-field__box select {
  font-size: 15px;
}

.auth-form--register .auth-field__icon {
  width: 22px;
  height: 22px;
  flex-basis: 22px;
}

.auth-form--register .auth-field__icon svg {
  width: 18px;
  height: 18px;
}

.auth-form--register .auth-submit {
  min-height: 58px;
  font-size: 16px;
  margin-top: 2px;
  grid-column: 1 / -1;
}

.auth-form--register + .auth-footer,
.auth-help + .auth-footer {
  margin-top: 12px;
}

.auth-card--register,
.auth-card--forgot {
  max-width: 760px;
}

.auth-floating {
  position: absolute;
  z-index: 0;
  color: rgba(110, 144, 193, 0.18);
  user-select: none;
  pointer-events: none;
}

.auth-floating--book {
  top: 68px;
  left: 180px;
  font-size: 86px;
}

.auth-floating--cap {
  top: 220px;
  right: 320px;
  font-size: 76px;
}

.auth-floating--atom {
  top: 280px;
  left: 500px;
  font-size: 92px;
}

.auth-floating--laptop {
  bottom: 110px;
  left: 780px;
  font-size: 100px;
}

.auth-floating--certificate {
  bottom: 220px;
  left: 430px;
  font-size: 76px;
}

@media (max-width: 1500px) {
  .auth-layout {
    padding: 28px;
  }

  .auth-card {
    width: min(100%, 700px);
    padding: 32px 32px 24px;
    min-height: auto;
  }

  .auth-card__logo {
    width: 64px;
    height: 64px;
    flex-basis: 64px;
  }

  .auth-card__brand strong {
    font-size: 24px;
  }

  .auth-card__brand span {
    font-size: 16px;
  }

  .auth-card__header h2 {
    font-size: 32px;
  }

  .auth-card__header p {
    font-size: 16px;
  }

  .auth-card__header--register h2,
  .auth-card__header--forgot h2 {
    font-size: 26px;
  }

  .auth-card__header--register p,
  .auth-card__header--forgot p {
    font-size: 14px;
  }

  .auth-field__box input,
  .auth-field__box select,
  .auth-check,
  .auth-link,
  .auth-footer,
  .auth-footer__link {
    font-size: 16px;
  }
}

@media (max-width: 1180px) {
  .auth-card {
    width: min(100%, 680px);
  }

  .auth-form--register {
    grid-template-columns: 1fr;
  }

  .auth-form--register .auth-submit {
    grid-column: auto;
  }

  .auth-scene__grid {
    width: 92%;
    opacity: 0.4;
  }
}

@media (max-width: 720px) {
  .auth-layout {
    padding: 18px;
  }

  .auth-card {
    padding: 24px 18px 20px;
    border-radius: 28px;
  }

  .auth-card__brand {
    gap: 12px;
    margin-bottom: 26px;
  }

  .auth-card__logo {
    width: 52px;
    height: 52px;
    flex-basis: 52px;
  }

  .auth-card__brand strong {
    font-size: 20px;
  }

  .auth-card__brand span {
    font-size: 14px;
  }

  .auth-card__header h2 {
    font-size: 26px;
  }

  .auth-card__header p {
    font-size: 14px;
    margin-bottom: 14px;
  }

  .auth-card__header--register h2,
  .auth-card__header--forgot h2 {
    font-size: 22px;
  }

  .auth-card__header--register p,
  .auth-card__header--forgot p {
    margin-bottom: 12px;
    font-size: 13px;
  }

  .auth-field__label {
    font-size: 14px;
  }

  .auth-field__box {
    min-height: 58px;
    padding: 0 14px;
    border-width: 2px;
    border-radius: 18px;
  }

  .auth-field__box input,
  .auth-field__box select,
  .auth-check,
  .auth-link,
  .auth-footer,
  .auth-footer__link {
    font-size: 14px;
  }

  .auth-submit {
    min-height: 56px;
    font-size: 16px;
    border-radius: 18px;
  }

  .auth-form--register .auth-field__label {
    font-size: 14px;
  }

  .auth-form--register .auth-field__box {
    min-height: 56px;
    padding: 0 14px;
  }

  .auth-form--register .auth-field__box input,
  .auth-form--register .auth-field__box select {
    font-size: 14px;
  }

  .auth-form--register .auth-submit {
    min-height: 56px;
    font-size: 15px;
  }

  .auth-row,
  .auth-footer {
    flex-direction: column;
    align-items: flex-start;
  }

  .auth-floating {
    display: none;
  }
}
</style>
