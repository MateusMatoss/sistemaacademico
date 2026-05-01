<template>
  <aside :class="['sidebar', { open }]">
    <div class="sidebar-brand">
      <div>
        <span class="sidebar-kicker">Sistema Acadêmico</span>
        <h1>Espalhando a Palavra</h1>
      </div>

      <button type="button" class="sidebar-close" @click="$emit('close')">
        ✕
      </button>
    </div>

    <nav class="sidebar-nav">
      <RouterLink
        v-for="item in visibleNavigationItems"
        :key="item.to"
        :to="item.to"
        class="sidebar-link"
        @click="$emit('close')"
      >
        <span class="sidebar-icon">{{ item.icon }}</span>
        <span>{{ item.label }}</span>
      </RouterLink>
    </nav>

    <div class="sidebar-footer">
      <div class="sidebar-user">
        <span class="sidebar-kicker">Perfil ativo</span>
        <strong>{{ perfil || "Sem perfil" }}</strong>
      </div>

      <button type="button" class="btn btn-outline sidebar-logout" @click="$emit('logout')">
        Sair do sistema
      </button>
    </div>
  </aside>
</template>

<script>
export default {
  name: "Sidebar",
  props: {
    open: {
      type: Boolean,
      default: false,
    },
    perfil: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      navigationItems: [
        { label: "Dashboard", to: "/dashboard", icon: "DB", allowedPerfis: ["ADMIN", "PROFESSOR"] },
        { label: "Alunos", to: "/alunos", icon: "AL", allowedPerfis: ["ADMIN", "PROFESSOR"] },
        { label: "Professores", to: "/professores", icon: "PR", allowedPerfis: ["ADMIN"] },
        { label: "Cursos", to: "/cursos", icon: "CU", allowedPerfis: ["ADMIN", "PROFESSOR"] },
        { label: "Disciplinas", to: "/disciplinas", icon: "DI", allowedPerfis: ["ADMIN", "PROFESSOR"] },
        { label: "Matrículas", to: "/matriculas", icon: "MT", allowedPerfis: ["ADMIN"] },
        { label: "Notas", to: "/notas", icon: "NT", allowedPerfis: ["ADMIN", "PROFESSOR"] },
        { label: "Frequências", to: "/frequencias", icon: "FR", allowedPerfis: ["ADMIN", "PROFESSOR"] },
        { label: "Usuários", to: "/usuarios", icon: "US", allowedPerfis: ["ADMIN"] },
        { label: "Meus Dados", to: "/meus-dados", icon: "MD", allowedPerfis: ["ALUNO"] },
        { label: "Meu Curso", to: "/meu-curso", icon: "MC", allowedPerfis: ["ALUNO"] },
        { label: "Minhas Disciplinas", to: "/minhas-disciplinas", icon: "DS", allowedPerfis: ["ALUNO"] },
        { label: "Minhas Notas", to: "/minhas-notas", icon: "NT", allowedPerfis: ["ALUNO"] },
        { label: "Minha Frequência", to: "/minha-frequencia", icon: "FR", allowedPerfis: ["ALUNO"] },
      ],
    };
  },
  computed: {
    visibleNavigationItems() {
      return this.navigationItems.filter((item) =>
        item.allowedPerfis.includes(this.perfil),
      );
    },
  },
};
</script>

<style scoped>
.sidebar {
  width: 280px;
  background:
    linear-gradient(180deg, rgba(15, 23, 42, 0.98), rgba(30, 64, 175, 0.95)),
    #0f172a;
  color: #fff;
  min-height: 100vh;
  position: fixed;
  inset: 0 auto 0 0;
  display: flex;
  flex-direction: column;
  padding: 28px 20px;
  z-index: 30;
}

.sidebar-brand {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 26px;
}

.sidebar-kicker {
  display: block;
  font-size: 12px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.58);
  margin-bottom: 8px;
}

.sidebar-brand h1 {
  margin: 0;
  font-size: 22px;
  line-height: 1.2;
}

.sidebar-close {
  display: none;
  border: 0;
  background: rgba(255, 255, 255, 0.12);
  color: #fff;
  border-radius: 12px;
  width: 38px;
  height: 38px;
  cursor: pointer;
}

.sidebar-nav {
  display: grid;
  gap: 8px;
  flex: 1;
}

.sidebar-link {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 14px;
  border-radius: 16px;
  color: rgba(255, 255, 255, 0.82);
  transition: background 0.2s ease, color 0.2s ease, transform 0.2s ease;
}

.sidebar-link:hover,
.sidebar-link.router-link-active {
  background: rgba(255, 255, 255, 0.12);
  color: #fff;
  transform: translateX(2px);
}

.sidebar-icon {
  width: 24px;
  text-align: center;
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 0.08em;
}

.sidebar-footer {
  display: grid;
  gap: 14px;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-user strong {
  font-size: 16px;
}

.sidebar-logout {
  color: #fff;
  border-color: rgba(255, 255, 255, 0.18);
}

@media (max-width: 1100px) {
  .sidebar {
    transform: translateX(-100%);
    transition: transform 0.25s ease;
  }

  .sidebar.open {
    transform: translateX(0);
  }

  .sidebar-close {
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
