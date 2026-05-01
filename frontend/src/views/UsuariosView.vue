<template>
  <section class="page-stack">
    <div class="page-intro">
      <div>
        <h1>Gestão de usuários</h1>
        <p>
          Consulte usuários, filtre por status ou perfil e execute ações administrativas
          como alteração de perfil, ativação, desativação e exclusão.
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
        <div class="toolbar">
          <div>
            <h3>Filtros disponíveis</h3>
            <p>Os filtros usam os endpoints dedicados do backend.</p>
          </div>

          <div class="toolbar-group">
            <button type="button" class="btn btn-ghost" @click="applyFilter('todos')" :disabled="loading">
              Todos
            </button>
            <button type="button" class="btn btn-ghost" @click="applyFilter('ativos')" :disabled="loading">
              Ativos
            </button>
            <button type="button" class="btn btn-ghost" @click="applyFilter('inativos')" :disabled="loading">
              Inativos
            </button>
            <div class="field">
              <label for="perfilFiltro">Filtrar por perfil</label>
              <select id="perfilFiltro" v-model="perfilFiltro" @change="applyFilter('perfil')">
                <option value="">Selecione</option>
                <option value="ADMIN">ADMIN</option>
                <option value="PROFESSOR">PROFESSOR</option>
                <option value="ALUNO">ALUNO</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      <div class="panel-body">
        <div v-if="loading" class="loading-box">
          Carregando usuários...
        </div>

        <div v-else-if="!usuarios.length" class="empty-box">
          Nenhum usuário encontrado para o filtro selecionado.
        </div>

        <div v-else class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Perfil atual</th>
                <th>Status</th>
                <th>Alterar perfil</th>
                <th>Criado em</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="usuario in usuarios" :key="usuario.idUsuario">
                <td>{{ usuario.idUsuario }}</td>
                <td>{{ usuario.username }}</td>
                <td>{{ usuario.perfil }}</td>
                <td>
                  <span :class="['status-chip', usuario.ativo ? 'active' : 'inactive']">
                    {{ usuario.ativo ? "Ativo" : "Inativo" }}
                  </span>
                </td>
                <td>
                  <div class="toolbar-group">
                    <select v-model="perfilEdicao[usuario.idUsuario]">
                      <option value="ADMIN">ADMIN</option>
                      <option value="PROFESSOR">PROFESSOR</option>
                      <option value="ALUNO">ALUNO</option>
                    </select>
                    <button
                      type="button"
                      class="btn btn-primary"
                      @click="salvarPerfil(usuario)"
                    >
                      Salvar
                    </button>
                  </div>
                </td>
                <td>{{ formatDate(usuario.dataCriacao) }}</td>
                <td>
                  <div class="table-actions">
                    <button
                      v-if="usuario.ativo"
                      type="button"
                      class="btn btn-muted"
                      @click="toggleStatus(usuario)"
                    >
                      Desativar
                    </button>
                    <button
                      v-else
                      type="button"
                      class="btn btn-ghost"
                      @click="toggleStatus(usuario)"
                    >
                      Ativar
                    </button>
                    <button
                      type="button"
                      class="btn btn-danger"
                      @click="confirmDelete(usuario)"
                    >
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
  </section>
</template>

<script>
import BaseAlert from "../components/BaseAlert.vue";
import usuarioService from "../services/usuarioService";
import { getErrorMessage } from "../services/api";

export default {
  name: "UsuariosView",
  components: {
    BaseAlert,
  },
  data() {
    return {
      todosUsuarios: [],
      usuarios: [],
      loading: false,
      perfilFiltro: "",
      filtroAtual: "todos",
      perfilEdicao: {},
      alert: {
        type: "info",
        message: "",
      },
    };
  },
  async created() {
    await this.loadUsuarios();
  },
  methods: {
    formatDate(value) {
      if (!value) {
        return "-";
      }

      return new Date(value).toLocaleString("pt-BR");
    },
    syncPerfilEdicao() {
      this.perfilEdicao = this.todosUsuarios.reduce((acc, usuario) => {
        acc[usuario.idUsuario] = usuario.perfil;
        return acc;
      }, {});
    },
    applyCurrentFilter() {
      if (this.filtroAtual === "ativos") {
        this.usuarios = this.todosUsuarios.filter((usuario) => usuario.ativo === true);
        return;
      }

      if (this.filtroAtual === "inativos") {
        this.usuarios = this.todosUsuarios.filter((usuario) => usuario.ativo !== true);
        return;
      }

      if (this.filtroAtual === "perfil" && this.perfilFiltro) {
        this.usuarios = this.todosUsuarios.filter((usuario) => usuario.perfil === this.perfilFiltro);
        return;
      }

      this.usuarios = [...this.todosUsuarios];
    },
    showAlert(type, message) {
      this.alert = { type, message };
    },
    async loadUsuarios() {
      this.loading = true;

      try {
        this.todosUsuarios = await usuarioService.listAll();
        this.applyCurrentFilter();
        this.syncPerfilEdicao();
      } catch (error) {
        this.todosUsuarios = [];
        this.usuarios = [];
        this.showAlert("error", getErrorMessage(error));
      } finally {
        this.loading = false;
      }
    },
    async applyFilter(filter) {
      this.filtroAtual = filter;

      if (filter !== "perfil") {
        this.perfilFiltro = "";
      }

      this.applyCurrentFilter();
    },
    async salvarPerfil(usuario) {
      try {
        await usuarioService.alterarPerfil(usuario.idUsuario, this.perfilEdicao[usuario.idUsuario]);
        this.showAlert("success", "Perfil alterado com sucesso.");
        await this.loadUsuarios();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      }
    },
    async toggleStatus(usuario) {
      try {
        if (usuario.ativo) {
          await usuarioService.desativar(usuario.idUsuario);
          this.showAlert("success", "Usuário desativado com sucesso.");
        } else {
          await usuarioService.ativar(usuario.idUsuario);
          this.showAlert("success", "Usuário ativado com sucesso.");
        }

        await this.loadUsuarios();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      }
    },
    async confirmDelete(usuario) {
      const confirmed = window.confirm(`Excluir o usuário ${usuario.username}?`);

      if (!confirmed) {
        return;
      }

      try {
        await usuarioService.remove(usuario.idUsuario);
        this.showAlert("success", "Usuário excluído com sucesso.");
        await this.loadUsuarios();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      }
    },
  },
};
</script>
