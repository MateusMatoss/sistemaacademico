<template>
  <section class="page-stack">
    <div class="page-intro">
      <div>
        <h1>Minhas disciplinas</h1>
        <p>Disciplinas vinculadas às suas matrículas.</p>
      </div>
    </div>
    <BaseAlert v-if="alert.message" :message="alert.message" :type="alert.type" />
    <div v-if="loading" class="loading-box">Carregando disciplinas...</div>
    <div v-else-if="!disciplinas.length" class="empty-box">Nenhuma disciplina encontrada.</div>
    <div v-else class="table-wrap panel">
      <div class="panel-body">
        <table>
          <thead>
            <tr>
              <th>Disciplina</th>
              <th>Curso</th>
              <th>Carga Horária</th>
              <th>Professor</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="disciplina in disciplinas" :key="disciplina.idDisciplina">
              <td>{{ disciplina.nomeDisciplina }}</td>
              <td>{{ disciplina.curso?.nomeCurso || "-" }}</td>
              <td>{{ disciplina.cargaHoraria }}</td>
              <td>{{ disciplina.professor?.nomePessoa || "-" }}</td>
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
  name: "MinhasDisciplinasView",
  components: { BaseAlert },
  data() {
    return {
      loading: false,
      disciplinas: [],
      alert: { type: "info", message: "" },
    };
  },
  async created() {
    this.loading = true;
    try {
      this.disciplinas = await alunoService.getMyDisciplines();
    } catch (error) {
      this.alert = { type: "error", message: getErrorMessage(error) };
    } finally {
      this.loading = false;
    }
  },
};
</script>
