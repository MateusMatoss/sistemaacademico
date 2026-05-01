<template>
  <section class="page-stack">
    <div class="page-intro">
      <div>
        <h1>Meu curso</h1>
        <p>Visualização do curso vinculado ao seu cadastro de aluno.</p>
      </div>
    </div>
    <BaseAlert v-if="alert.message" :message="alert.message" :type="alert.type" />
    <div v-if="loading" class="loading-box">Carregando curso...</div>
    <div v-else class="panel">
      <div class="panel-body details-grid">
        <div class="details-item"><span>Curso</span><strong>{{ curso?.nomeCurso || "Sem curso vinculado" }}</strong></div>
        <div class="details-item"><span>Descrição</span><strong>{{ curso?.descricaoCurso || "-" }}</strong></div>
      </div>
    </div>
  </section>
</template>

<script>
import BaseAlert from "../components/BaseAlert.vue";
import alunoService from "../services/alunoService";
import { getErrorMessage } from "../services/api";

export default {
  name: "MeuCursoView",
  components: { BaseAlert },
  data() {
    return {
      loading: false,
      curso: null,
      alert: { type: "info", message: "" },
    };
  },
  async created() {
    this.loading = true;
    try {
      this.curso = await alunoService.getMyCourse();
    } catch (error) {
      this.alert = { type: "error", message: getErrorMessage(error) };
    } finally {
      this.loading = false;
    }
  },
};
</script>

<style scoped>
.details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 18px;
}
.details-item {
  display: grid;
  gap: 8px;
  padding: 18px;
  border-radius: 16px;
  background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
  border: 1px solid rgba(214, 223, 236, 0.88);
}
.details-item span {
  color: var(--text-soft);
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}
</style>
