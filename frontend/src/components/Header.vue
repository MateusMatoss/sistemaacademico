<template>
  <header class="header">
    <div class="header-main">
      <button type="button" class="header-menu" @click="$emit('toggle-sidebar')">
        ☰
      </button>

      <div>
        <span class="header-kicker">Painel administrativo</span>
        <h2>{{ title }}</h2>
      </div>
    </div>

    <div class="header-actions">
      <div class="header-user">
        <span>{{ currentDate }}</span>
        <strong>{{ perfil || "Usuário autenticado" }}</strong>
      </div>

      <button type="button" class="btn btn-primary" @click="$emit('logout')">
        Logout
      </button>
    </div>
  </header>
</template>

<script>
export default {
  name: "Header",
  props: {
    title: {
      type: String,
      default: "Sistema Acadêmico",
    },
    perfil: {
      type: String,
      default: "",
    },
  },
  computed: {
    currentDate() {
      return new Date().toLocaleDateString("pt-BR", {
        day: "2-digit",
        month: "long",
        year: "numeric",
      });
    },
  },
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 18px;
  padding: 22px 28px;
  background: rgba(255, 255, 255, 0.78);
  border-bottom: 1px solid rgba(214, 223, 236, 0.9);
  backdrop-filter: blur(16px);
  position: sticky;
  top: 0;
  z-index: 20;
}

.header-main,
.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-kicker {
  display: block;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  color: var(--text-soft);
  margin-bottom: 6px;
}

.header h2 {
  margin: 0;
  font-size: 24px;
}

.header-user {
  display: grid;
  text-align: right;
  color: var(--text-soft);
}

.header-user strong {
  color: var(--text);
}

.header-menu {
  display: none;
  border: 0;
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: var(--primary-dark);
  color: #fff;
  cursor: pointer;
}

@media (max-width: 1100px) {
  .header-menu {
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
}

@media (max-width: 720px) {
  .header {
    padding: 18px;
    flex-wrap: wrap;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;
  }

  .header-user {
    text-align: left;
  }
}
</style>
