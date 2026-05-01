<template>
  <section class="page-stack">
    <div class="page-intro">
      <div>
        <h1>Lançamento de notas</h1>
        <p>
          Selecione a disciplina, escolha a matrícula do aluno e registre a avaliação.
          Professores acessam apenas as matrículas carregadas pela disciplina selecionada.
        </p>
      </div>
    </div>

    <BaseAlert
      v-if="alert.message"
      :message="alert.message"
      :type="alert.type"
    />

    <div class="split-layout">
      <div class="panel">
        <div class="panel-header">
          <h3>{{ editingId ? "Editar nota" : "Cadastrar nota" }}</h3>
          <p>Preencha a disciplina, matrícula, valor e tipo da avaliação.</p>
        </div>

        <div class="panel-body">
          <form class="form-grid" @submit.prevent="submitForm">
            <div class="field">
              <label for="disciplina">Disciplina</label>
              <select id="disciplina" v-model="selectedDisciplinaId" @change="handleDisciplinaChange" required>
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
              <label for="idMatricula">Matrícula</label>
              <select id="idMatricula" v-model="form.idMatricula" required>
                <option value="">Selecione uma matrícula</option>
                <option
                  v-for="matricula in matriculasDaDisciplina"
                  :key="matricula.idMatricula"
                  :value="matricula.idMatricula"
                >
                  #{{ matricula.idMatricula }} - {{ matricula.aluno?.nomePessoa }} / {{ matricula.disciplina?.nomeDisciplina }}
                </option>
              </select>
            </div>

            <div class="field">
              <label for="valorNota">Valor da nota</label>
              <input id="valorNota" v-model="form.valorNota" type="number" min="0" step="0.1" required />
            </div>

            <div class="field">
              <label for="tipoAvaliacao">Tipo de avaliação</label>
              <input id="tipoAvaliacao" v-model="form.tipoAvaliacao" type="text" placeholder="Ex.: Prova 1" required />
            </div>

            <div class="form-actions">
              <button type="submit" class="btn btn-primary" :disabled="saving">
                {{ saving ? "Salvando..." : editingId ? "Atualizar" : "Salvar" }}
              </button>
              <button type="button" class="btn btn-muted" @click="resetForm">
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>

      <div class="panel">
        <div class="panel-header">
          <div class="toolbar">
            <div>
              <h3>Notas lançadas</h3>
              <p>Use a disciplina e a matrícula para localizar rapidamente os lançamentos.</p>
            </div>

            <div class="toolbar-group">
              <div class="field">
                <label for="filtroMatricula">Filtrar por matrícula</label>
                <select id="filtroMatricula" v-model="filterMatriculaId" @change="applyFilters">
                  <option value="">Todas da disciplina</option>
                  <option
                    v-for="matricula in matriculasDaDisciplina"
                    :key="`filtro-${matricula.idMatricula}`"
                    :value="matricula.idMatricula"
                  >
                    #{{ matricula.idMatricula }} - {{ matricula.aluno?.nomePessoa }}
                  </option>
                </select>
              </div>

              <button type="button" class="btn btn-ghost" @click="reloadData" :disabled="loading">
                {{ loading ? "Atualizando..." : "Recarregar dados" }}
              </button>
            </div>
          </div>
        </div>

        <div class="panel-body">
          <div v-if="loading" class="loading-box">
            Carregando notas...
          </div>

          <div v-else-if="!selectedDisciplinaId" class="empty-box">
            Selecione uma disciplina para visualizar e cadastrar notas.
          </div>

          <div v-else-if="!filteredNotas.length" class="empty-box">
            Nenhuma nota encontrada para o filtro atual.
          </div>

          <div v-else class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nota</th>
                  <th>Avaliação</th>
                  <th>Matrícula</th>
                  <th>Aluno</th>
                  <th>Disciplina</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="nota in filteredNotas" :key="nota.idNota">
                  <td>{{ nota.idNota }}</td>
                  <td>{{ nota.valorNota }}</td>
                  <td>{{ nota.tipoAvaliacao }}</td>
                  <td>{{ nota.matricula?.idMatricula }}</td>
                  <td>{{ nota.matricula?.aluno?.nomePessoa || "-" }}</td>
                  <td>{{ nota.matricula?.disciplina?.nomeDisciplina || "-" }}</td>
                  <td>
                    <div class="table-actions">
                      <button type="button" class="btn btn-ghost" @click="startEdit(nota)">
                        Editar
                      </button>
                      <button type="button" class="btn btn-danger" @click="removeNota(nota.idNota)">
                        Excluir
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import BaseAlert from "../components/BaseAlert.vue";
import disciplinaService from "../services/disciplinaService";
import matriculaService from "../services/matriculaService";
import notaService from "../services/notaService";
import { getErrorMessage } from "../services/api";

export default {
  name: "NotasView",
  components: {
    BaseAlert,
  },
  data() {
    return {
      loading: false,
      saving: false,
      editingId: null,
      selectedDisciplinaId: "",
      filterMatriculaId: "",
      disciplinasDisponiveis: [],
      matriculasDaDisciplina: [],
      notas: [],
      filteredNotas: [],
      form: {
        idMatricula: "",
        valorNota: "",
        tipoAvaliacao: "",
      },
      alert: {
        type: "info",
        message: "",
      },
    };
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
    resetForm() {
      this.editingId = null;
      this.form = {
        idMatricula: "",
        valorNota: "",
        tipoAvaliacao: "",
      };
    },
    async reloadData() {
      this.loading = true;
      this.clearAlert();

      try {
        this.disciplinasDisponiveis = (await disciplinaService.listAll()).map((disciplina) => ({
          idDisciplina: disciplina.idDisciplina,
          nomeDisciplina: disciplina.nomeDisciplina,
          cursoNome: disciplina.curso?.nomeCurso || "Sem curso",
        }));

        this.notas = await notaService.listAll();

        if (!this.selectedDisciplinaId && this.disciplinasDisponiveis.length) {
          this.selectedDisciplinaId = String(this.disciplinasDisponiveis[0].idDisciplina);
        }

        await this.loadMatriculasDaDisciplina();
        this.applyFilters();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      } finally {
        this.loading = false;
      }
    },
    async loadMatriculasDaDisciplina() {
      if (!this.selectedDisciplinaId) {
        this.matriculasDaDisciplina = [];
        this.filteredNotas = [];
        return;
      }

      this.matriculasDaDisciplina = await matriculaService.listByDisciplina(this.selectedDisciplinaId);

      if (
        this.form.idMatricula &&
        !this.matriculasDaDisciplina.some((matricula) => matricula.idMatricula === Number(this.form.idMatricula))
      ) {
        this.form.idMatricula = "";
      }

      if (
        this.filterMatriculaId &&
        !this.matriculasDaDisciplina.some((matricula) => matricula.idMatricula === Number(this.filterMatriculaId))
      ) {
        this.filterMatriculaId = "";
      }
    },
    async handleDisciplinaChange() {
      this.filterMatriculaId = "";
      this.resetForm();
      await this.loadMatriculasDaDisciplina();
      this.applyFilters();
    },
    applyFilters() {
      const disciplinaId = Number(this.selectedDisciplinaId);

      this.filteredNotas = this.notas.filter((nota) => {
        const sameDisciplina = nota.matricula?.disciplina?.idDisciplina === disciplinaId;

        if (!sameDisciplina) {
          return false;
        }

        if (!this.filterMatriculaId) {
          return true;
        }

        return nota.matricula?.idMatricula === Number(this.filterMatriculaId);
      });
    },
    async submitForm() {
      this.saving = true;
      this.clearAlert();

      try {
        const payload = {
          idMatricula: Number(this.form.idMatricula),
          valorNota: Number(this.form.valorNota),
          tipoAvaliacao: this.form.tipoAvaliacao,
        };

        if (this.editingId) {
          await notaService.update(this.editingId, payload);
          this.showAlert("success", "Nota atualizada com sucesso.");
        } else {
          await notaService.create(payload);
          this.showAlert("success", "Nota cadastrada com sucesso.");
        }

        this.resetForm();
        await this.reloadData();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      } finally {
        this.saving = false;
      }
    },
    startEdit(nota) {
      this.editingId = nota.idNota;
      this.selectedDisciplinaId = String(nota.matricula?.disciplina?.idDisciplina || "");
      this.form = {
        idMatricula: nota.matricula?.idMatricula || "",
        valorNota: nota.valorNota,
        tipoAvaliacao: nota.tipoAvaliacao,
      };
      this.loadMatriculasDaDisciplina();
      window.scrollTo({ top: 0, behavior: "smooth" });
    },
    async removeNota(idNota) {
      const confirmed = window.confirm("Deseja excluir esta nota?");

      if (!confirmed) {
        return;
      }

      try {
        await notaService.remove(idNota);
        this.showAlert("success", "Nota excluída com sucesso.");
        await this.reloadData();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      }
    },
  },
};
</script>
