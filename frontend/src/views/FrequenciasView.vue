<template>
  <section class="page-stack">
    <div class="page-intro">
      <div>
        <h1>Chamada por aula</h1>
        <p>
          Todos os alunos da disciplina começam como presentes. O professor só
          precisa desmarcar os ausentes e salvar a chamada do dia.
        </p>
      </div>
    </div>

    <BaseAlert
      v-if="alert.message"
      :message="alert.message"
      :type="alert.type"
    />

    <div class="panel">
      <div class="panel-header">
        <h3>Configurar aula</h3>
        <p>Escolha a disciplina e a data para montar automaticamente a lista de presença.</p>
      </div>

      <div class="panel-body">
        <div class="toolbar">
          <div class="toolbar-group">
            <div class="field">
              <label for="disciplina">Disciplina</label>
              <select id="disciplina" v-model="selectedDisciplinaId" @change="buildAttendanceList">
                <option value="">Selecione uma disciplina</option>
                <option
                  v-for="disciplina in disciplinasDisponiveis"
                  :key="disciplina.idDisciplina"
                  :value="disciplina.idDisciplina"
                >
                  {{ disciplina.nomeDisciplina }} - {{ disciplina.cursoNome }}
                </option>
              </select>
            </div>

            <div class="field">
              <label for="dataAula">Data da aula</label>
              <input id="dataAula" v-model="dataAula" type="date" @change="buildAttendanceList" />
            </div>
          </div>

          <div class="toolbar-group">
            <button type="button" class="btn btn-ghost" @click="reloadData" :disabled="loading">
              {{ loading ? "Atualizando..." : "Recarregar dados" }}
            </button>
            <button
              type="button"
              class="btn btn-primary"
              @click="saveAttendance"
              :disabled="saving || !attendanceList.length"
            >
              {{ saving ? "Salvando..." : "Salvar chamada" }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="panel">
      <div class="panel-header">
        <h3>Lista de presença</h3>
        <p>
          {{ attendanceSummary }}
        </p>
      </div>

      <div class="panel-body">
        <div v-if="loading" class="loading-box">
          Carregando matrículas e frequências...
        </div>

        <div v-else-if="!selectedDisciplinaId" class="empty-box">
          Selecione uma disciplina para gerar a chamada.
        </div>

        <div v-else-if="!attendanceList.length" class="empty-box">
          Nenhum aluno matriculado encontrado para esta disciplina.
        </div>

        <div v-else class="attendance-list">
          <div class="attendance-toolbar">
            <button type="button" class="btn btn-ghost" @click="markAll(true)">
              Marcar todos como presentes
            </button>
            <button type="button" class="btn btn-muted" @click="markAll(false)">
              Desmarcar todos
            </button>
          </div>

          <label
            v-for="entry in attendanceList"
            :key="entry.idMatricula"
            class="attendance-item"
          >
            <div class="attendance-item__info">
              <strong>{{ entry.nomeAluno }}</strong>
              <span>Matrícula #{{ entry.idMatricula }}</span>
            </div>

            <div class="attendance-item__check">
              <input
                :id="`frequencia-${entry.idMatricula}`"
                v-model="entry.presente"
                type="checkbox"
              />
              <span>{{ entry.presente ? "Presente" : "Falta" }}</span>
            </div>
          </label>
        </div>
      </div>
    </div>

    <div v-if="selectedDisciplinaId && attendanceList.length" class="panel">
      <div class="panel-header">
        <h3>Resumo da aula</h3>
        <p>Conferência rápida antes de salvar.</p>
      </div>

      <div class="panel-body">
        <div class="grid-cards">
          <article class="summary-inline">
            <span>Total de alunos</span>
            <strong>{{ attendanceList.length }}</strong>
          </article>
          <article class="summary-inline">
            <span>Presentes</span>
            <strong>{{ totalPresentes }}</strong>
          </article>
          <article class="summary-inline">
            <span>Ausentes</span>
            <strong>{{ totalAusentes }}</strong>
          </article>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import BaseAlert from "../components/BaseAlert.vue";
import disciplinaService from "../services/disciplinaService";
import frequenciaService from "../services/frequenciaService";
import matriculaService from "../services/matriculaService";
import { getErrorMessage } from "../services/api";

export default {
  name: "FrequenciasView",
  components: {
    BaseAlert,
  },
  data() {
    return {
      loading: false,
      saving: false,
      dataAula: new Date().toISOString().slice(0, 10),
      selectedDisciplinaId: "",
      disciplinas: [],
      matriculas: [],
      frequencias: [],
      attendanceList: [],
      alert: {
        type: "info",
        message: "",
      },
    };
  },
  computed: {
    disciplinasDisponiveis() {
      return [...this.disciplinas].sort((a, b) =>
        `${a.nomeDisciplina}${a.cursoNome}`.localeCompare(`${b.nomeDisciplina}${b.cursoNome}`),
      );
    },
    totalPresentes() {
      return this.attendanceList.filter((entry) => entry.presente).length;
    },
    totalAusentes() {
      return this.attendanceList.filter((entry) => !entry.presente).length;
    },
    attendanceSummary() {
      if (!this.selectedDisciplinaId) {
        return "Selecione uma disciplina e a data da aula para gerar a chamada.";
      }

      if (!this.attendanceList.length) {
        return "Nenhuma matrícula encontrada para a disciplina selecionada.";
      }

      return `${this.attendanceList.length} alunos carregados. Todos iniciam como presentes.`;
    },
  },
  async created() {
    await this.reloadData();
  },
  methods: {
    showAlert(type, message) {
      this.alert = { type, message };
    },
    clearAlert() {
      this.alert = { type: "info", message: "" };
    },
    async reloadData() {
      this.loading = true;
      this.clearAlert();

      try {
        const [disciplinas, frequencias] = await Promise.all([
          disciplinaService.listAll(),
          frequenciaService.listAll(),
        ]);

        this.disciplinas = disciplinas.map((disciplina) => ({
          idDisciplina: disciplina.idDisciplina,
          nomeDisciplina: disciplina.nomeDisciplina,
          cursoNome: disciplina.curso?.nomeCurso || "Sem curso",
        }));
        this.frequencias = frequencias;

        if (!this.selectedDisciplinaId && this.disciplinasDisponiveis.length) {
          this.selectedDisciplinaId = String(this.disciplinasDisponiveis[0].idDisciplina);
        }

        await this.buildAttendanceList();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      } finally {
        this.loading = false;
      }
    },
    async buildAttendanceList() {
      if (!this.selectedDisciplinaId) {
        this.attendanceList = [];
        this.matriculas = [];
        return;
      }

      const disciplinaId = Number(this.selectedDisciplinaId);
      this.matriculas = await matriculaService.listByDisciplina(disciplinaId);

      const matriculasDaDisciplina = this.matriculas
        .sort((a, b) =>
          (a.aluno?.nomePessoa || "").localeCompare(b.aluno?.nomePessoa || ""),
        );

      this.attendanceList = matriculasDaDisciplina.map((matricula) => {
        const frequenciaExistente = this.findExistingFrequency(matricula.idMatricula, this.dataAula);

        return {
          idMatricula: matricula.idMatricula,
          nomeAluno: matricula.aluno?.nomePessoa || "Aluno sem nome",
          presente: frequenciaExistente ? frequenciaExistente.presente : true,
          idFrequencia: frequenciaExistente?.idFrequencia || null,
        };
      });
    },
    findExistingFrequency(idMatricula, dataAula) {
      return this.frequencias.find(
        (frequencia) =>
          frequencia.matricula?.idMatricula === idMatricula &&
          frequencia.dataAula === dataAula,
      );
    },
    markAll(value) {
      this.attendanceList = this.attendanceList.map((entry) => ({
        ...entry,
        presente: value,
      }));
    },
    async saveAttendance() {
      if (!this.attendanceList.length) {
        return;
      }

      this.saving = true;
      this.clearAlert();

      try {
        for (const entry of this.attendanceList) {
          const payload = {
            dataAula: this.dataAula,
            presente: entry.presente,
            idMatricula: entry.idMatricula,
          };

          if (entry.idFrequencia) {
            await frequenciaService.update(entry.idFrequencia, payload);
          } else {
            await frequenciaService.create(payload);
          }
        }

        this.showAlert("success", "Chamada salva com sucesso.");
        await this.reloadData();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      } finally {
        this.saving = false;
      }
    },
  },
};
</script>

<style scoped>
.attendance-list {
  display: grid;
  gap: 12px;
}

.attendance-toolbar {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-bottom: 6px;
}

.attendance-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  padding: 16px 18px;
  border: 1px solid var(--border);
  border-radius: 16px;
  background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
}

.attendance-item__info {
  display: grid;
  gap: 4px;
}

.attendance-item__info strong {
  font-size: 16px;
}

.attendance-item__info span {
  color: var(--text-soft);
  font-size: 13px;
}

.attendance-item__check {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-weight: 700;
}

.attendance-item__check input {
  width: 20px;
  height: 20px;
  accent-color: var(--primary);
}

.summary-inline {
  background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
  border-radius: 18px;
  border: 1px solid rgba(214, 223, 236, 0.88);
  padding: 18px;
  box-shadow: var(--shadow-sm);
}

.summary-inline span {
  display: block;
  color: var(--text-soft);
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-bottom: 10px;
}

.summary-inline strong {
  font-size: 30px;
}

@media (max-width: 720px) {
  .attendance-item {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
