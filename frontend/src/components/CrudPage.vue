<template>
  <section class="page-stack">
    <div class="page-intro">
      <div>
        <h1>{{ config.title }}</h1>
        <p>{{ config.description }}</p>
      </div>
    </div>

    <BaseAlert
      v-if="alert.message"
      :message="alert.message"
      :type="alert.type"
    />

    <div :class="showFormPanel ? 'split-layout' : 'page-stack'">
      <div v-if="showFormPanel" class="panel">
        <div class="panel-header">
          <h3>{{ isEditing ? `Editar ${config.singular}` : `Cadastrar ${config.singular}` }}</h3>
          <p>Preencha os campos e envie os dados para a API.</p>
        </div>

        <div class="panel-body">
          <form class="form-grid" @submit.prevent="submitForm">
            <div
              v-for="field in config.fields"
              :key="field.key"
              class="field"
            >
              <label :for="field.key">{{ field.label }}</label>

              <textarea
                v-if="field.type === 'textarea'"
                :id="field.key"
                v-model="form[field.key]"
                :placeholder="field.placeholder || ''"
                :required="field.required"
              />

              <select
                v-else-if="field.type === 'select'"
                :id="field.key"
                v-model="form[field.key]"
                :required="field.required"
              >
                <option value="">
                  {{ field.placeholder || "Selecione uma opção" }}
                </option>
                <option
                  v-for="option in getFieldOptions(field)"
                  :key="option.value"
                  :value="option.value"
                >
                  {{ option.label }}
                </option>
              </select>

              <select
                v-else-if="field.type === 'boolean'"
                :id="field.key"
                v-model="form[field.key]"
                :required="field.required"
              >
                <option value="">Selecione</option>
                <option :value="true">Sim</option>
                <option :value="false">Não</option>
              </select>

              <input
                v-else
                :id="field.key"
                v-model="form[field.key]"
                :type="field.type || 'text'"
                :placeholder="field.placeholder || ''"
                :required="field.required"
                :min="field.min"
                :step="field.step"
              />
            </div>

            <div class="form-actions">
              <button type="submit" class="btn btn-primary" :disabled="submitting">
                {{ submitting ? "Salvando..." : isEditing ? "Atualizar" : "Salvar" }}
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
              <h3>Listagem</h3>
              <p>{{ config.tableDescription || "Gerencie os registros cadastrados no sistema." }}</p>
            </div>

            <div class="toolbar-group">
              <div v-if="config.search" class="field">
                <label :for="`${config.name}-search`">Buscar por nome</label>
                <input
                  :id="`${config.name}-search`"
                  v-model="searchTerm"
                  type="text"
                  :placeholder="config.searchPlaceholder || 'Digite para buscar'"
                  @keyup.enter="loadItems"
                />
              </div>

              <div v-if="config.filter" class="field">
                <label :for="`${config.name}-filter`">{{ config.filter.label }}</label>
                <input
                  :id="`${config.name}-filter`"
                  v-model="filterValue"
                  :type="config.filter.type || 'text'"
                  :placeholder="config.filter.placeholder || ''"
                  @keyup.enter="loadItems"
                />
              </div>

              <button type="button" class="btn btn-ghost" @click="loadItems" :disabled="loading">
                {{ loading ? "Atualizando..." : "Atualizar" }}
              </button>
            </div>
          </div>
        </div>

        <div class="panel-body">
          <div v-if="loading" class="loading-box">
            Carregando dados...
          </div>

          <div v-else-if="!items.length" class="empty-box">
            Nenhum registro encontrado.
          </div>

          <div v-else class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th v-for="column in config.columns" :key="column.key">
                    {{ column.label }}
                  </th>
                  <th v-if="showActionsColumn">Ações</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in items" :key="getItemId(item)">
                  <td v-for="column in config.columns" :key="`${getItemId(item)}-${column.key}`">
                    {{ formatCell(item, column) }}
                  </td>
                  <td v-if="showActionsColumn">
                    <div class="table-actions">
                      <button v-if="canEdit" type="button" class="btn btn-ghost" @click="startEdit(item)">
                        Editar
                      </button>
                      <button v-if="canDelete" type="button" class="btn btn-danger" @click="openDeleteDialog(item)">
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

    <ConfirmDialog
      :open="Boolean(itemToDelete)"
      :title="`Excluir ${config.singular}`"
      message="Esta ação remove o registro da base de dados. Deseja continuar?"
      @cancel="itemToDelete = null"
      @confirm="confirmDelete"
    />
  </section>
</template>

<script>
import BaseAlert from "./BaseAlert.vue";
import ConfirmDialog from "./ConfirmDialog.vue";
import { getErrorMessage } from "../services/api";

export default {
  name: "CrudPage",
  components: {
    BaseAlert,
    ConfirmDialog,
  },
  props: {
    config: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      items: [],
      loading: false,
      submitting: false,
      searchTerm: "",
      filterValue: "",
      isEditing: false,
      editingId: null,
      form: {},
      dependencies: {},
      alert: {
        type: "info",
        message: "",
      },
      itemToDelete: null,
    };
  },
  computed: {
    permissions() {
      return this.config.permissions || {};
    },
    canCreate() {
      return this.permissions.create !== false;
    },
    canEdit() {
      return this.permissions.edit !== false;
    },
    canDelete() {
      return this.permissions.delete !== false;
    },
    showFormPanel() {
      return this.canCreate || (this.canEdit && this.isEditing);
    },
    showActionsColumn() {
      return this.canEdit || this.canDelete;
    },
  },
  async created() {
    this.resetForm();
    await this.initializePage();
  },
  methods: {
    async initializePage() {
      if (this.config.loadDependencies && this.showFormPanel) {
        try {
          this.dependencies = await this.config.loadDependencies();
        } catch (error) {
          this.showAlert("error", getErrorMessage(error));
        }
      }

      await this.loadItems();
    },
    createDefaultForm() {
      return JSON.parse(JSON.stringify(this.config.formDefaults || {}));
    },
    resetForm() {
      this.form = this.createDefaultForm();
      this.isEditing = false;
      this.editingId = null;
    },
    showAlert(type, message) {
      this.alert = { type, message };
    },
    clearAlert() {
      this.alert = { type: "info", message: "" };
    },
    async loadItems() {
      this.loading = true;
      this.clearAlert();

      try {
        if (this.config.filter && this.filterValue) {
          this.items = await this.config.service[this.config.filter.method](this.filterValue);
        } else if (this.config.search && this.searchTerm.trim()) {
          this.items = await this.config.service[this.config.searchMethod || "searchByName"](this.searchTerm.trim());
        } else {
          this.items = await this.config.service.listAll();
        }
      } catch (error) {
        this.items = [];
        this.showAlert("error", getErrorMessage(error));
      } finally {
        this.loading = false;
      }
    },
    getItemId(item) {
      const key = this.config.idKey || "id";
      return item[key];
    },
    getFieldOptions(field) {
      return this.dependencies[field.optionsKey] || [];
    },
    formatCell(item, column) {
      if (typeof column.format === "function") {
        return column.format(item);
      }

      return column.key.split(".").reduce((current, part) => current?.[part], item) ?? "-";
    },
    startEdit(item) {
      if (!this.canEdit) {
        return;
      }

      const nextForm = this.config.fromItem
        ? this.config.fromItem(item)
        : { ...item };

      this.form = {
        ...this.createDefaultForm(),
        ...nextForm,
      };
      this.isEditing = true;
      this.editingId = this.getItemId(item);
      window.scrollTo({ top: 0, behavior: "smooth" });
    },
    async submitForm() {
      if (!this.canCreate && !this.canEdit) {
        return;
      }

      this.submitting = true;
      this.clearAlert();

      try {
        const payload = this.config.toPayload
          ? this.config.toPayload(this.form)
          : { ...this.form };

        if (this.config.normalizeEmptyValues) {
          this.config.normalizeEmptyValues.forEach((key) => {
            if (payload[key] === "") {
              payload[key] = null;
            }
          });
        }

        if (this.isEditing) {
          await this.config.service.update(this.editingId, payload);
          this.showAlert("success", `${this.config.singular} atualizado com sucesso.`);
        } else {
          await this.config.service.create(payload);
          this.showAlert("success", `${this.config.singular} cadastrado com sucesso.`);
        }

        this.resetForm();
        await this.loadItems();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      } finally {
        this.submitting = false;
      }
    },
    openDeleteDialog(item) {
      if (!this.canDelete) {
        return;
      }

      this.itemToDelete = item;
    },
    async confirmDelete() {
      if (!this.itemToDelete || !this.canDelete) {
        return;
      }

      try {
        await this.config.service.remove(this.getItemId(this.itemToDelete));
        this.showAlert("success", `${this.config.singular} excluído com sucesso.`);

        if (this.editingId === this.getItemId(this.itemToDelete)) {
          this.resetForm();
        }

        await this.loadItems();
      } catch (error) {
        this.showAlert("error", getErrorMessage(error));
      } finally {
        this.itemToDelete = null;
      }
    },
  },
};
</script>
