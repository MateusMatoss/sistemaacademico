<template>
  <section class="first-access-shell">
    <div class="first-access-card">
      <span class="first-access-kicker">Primeiro acesso</span>
      <h1>Troque sua senha inicial</h1>
      <p>
        Seu usuário foi criado pelo administrador com uma senha provisória.
        Defina uma nova senha para continuar.
      </p>

      <BaseAlert v-if="message" :message="message" :type="messageType" />

      <form class="form-grid" @submit.prevent="submitPasswordChange">
        <div class="field">
          <label for="nova-senha">Nova senha</label>
          <input
            id="nova-senha"
            v-model="form.novaSenha"
            type="password"
            placeholder="Digite a nova senha"
            required
          />
        </div>

        <div class="field">
          <label for="confirmar-senha">Confirmar nova senha</label>
          <input
            id="confirmar-senha"
            v-model="form.confirmarSenha"
            type="password"
            placeholder="Repita a nova senha"
            required
          />
        </div>

        <button type="submit" class="btn btn-primary" :disabled="loading">
          {{ loading ? "Salvando..." : "Salvar nova senha" }}
        </button>
      </form>
    </div>
  </section>
</template>

<script>
import BaseAlert from "../components/BaseAlert.vue";
import authService from "../services/authService";
import { getErrorMessage } from "../services/api";

export default {
  name: "PrimeiroAcessoView",
  components: {
    BaseAlert,
  },
  data() {
    return {
      loading: false,
      message: "",
      messageType: "info",
      form: {
        novaSenha: "",
        confirmarSenha: "",
      },
    };
  },
  methods: {
    async submitPasswordChange() {
      this.loading = true;
      this.message = "";

      try {
        await authService.changeTemporaryPassword(this.form);
        this.$router.push("/");
      } catch (error) {
        this.message = getErrorMessage(error);
        this.messageType = "error";
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.first-access-shell {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 32px 16px;
  background:
    radial-gradient(circle at top, rgba(37, 99, 235, 0.16), transparent 32%),
    linear-gradient(135deg, #f3f7fd 0%, #dfeafb 100%);
}

.first-access-card {
  width: min(100%, 460px);
  padding: 32px;
  border-radius: 24px;
  background: #ffffff;
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.14);
}

.first-access-kicker {
  display: inline-block;
  margin-bottom: 10px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: #1d4ed8;
}

.first-access-card h1 {
  margin: 0 0 12px;
  font-size: 30px;
  color: #10213a;
}

.first-access-card p {
  margin: 0 0 24px;
  color: #5b6b83;
  line-height: 1.6;
}
</style>
