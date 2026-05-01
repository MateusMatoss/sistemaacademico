<template>
  <section class="page-stack">
    <div class="page-intro">
      <div>
        <h1>Minhas notas</h1>
        <p>Média de notas por disciplina do aluno logado.</p>
      </div>
    </div>
    <BaseAlert v-if="alert.message" :message="alert.message" :type="alert.type" />
    <div v-if="loading" class="loading-box">Carregando notas...</div>
    <div v-else-if="!resumo.length" class="empty-box">Nenhuma nota encontrada.</div>
    <div v-else class="table-wrap panel">
      <div class="panel-body">
        <table>
          <thead>
            <tr>
              <th>Disciplina</th>
              <th>Curso</th>
              <th>Média</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in resumo" :key="`${item.idAluno}-${item.disciplina}`">
              <td>{{ item.disciplina }}</td>
              <td>{{ item.curso }}</td>
              <td>{{ item.mediaNotas }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script>
import BaseAlert from "../components/BaseAlert.vue";
import alunoService from "../services/alunoService";
import { getErrorMessage } from "../services/api";

export default {
  name: "MinhasNotasView",
  components: { BaseAlert },
  data() {
    return {
      loading: false,
      resumo: [],
      alert: { type: "info", message: "" },
    };
  },
  async created() {
    this.loading = true;
    try {
      this.resumo = await alunoService.getMySummary();
    } catch (error) {
      this.alert = { type: "error", message: getErrorMessage(error) };
    } finally {
      this.loading = false;
    }
  },
};
</script>
