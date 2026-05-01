<template>
  <div v-if="open" class="modal-overlay" role="dialog" aria-modal="true">
    <div class="modal">
      <h3>{{ title }}</h3>
      <p>{{ message }}</p>
      <div class="modal-actions">
        <button type="button" class="btn btn-muted" @click="$emit('cancel')">
          Cancelar
        </button>
        <button type="button" class="btn btn-danger" @click="$emit('confirm')">
          Confirmar
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ConfirmDialog",
  props: {
    open: { type: Boolean, default: false },
    title: { type: String, default: "Confirmar ação" },
    message: { type: String, default: "Deseja continuar?" },
  },
};
</script>
