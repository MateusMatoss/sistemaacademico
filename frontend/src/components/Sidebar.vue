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
        <span class="sidebar-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
            <path v-for="(path, index) in item.icon" :key="`${item.to}-icon-${index}`" :d="path" />
          </svg>
        </span>
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
        {
          label: "Dashboard",
          to: "/dashboard",
          icon: ["M4 13h7V4H4z", "M13 20h7v-9h-7z", "M13 11h7V4h-7z", "M4 20h7v-5H4z"],
          allowedPerfis: ["ADMIN", "PROFESSOR"],
        },
        {
          label: "Alunos",
          to: "/alunos",
          icon: ["M16 21v-2a4 4 0 0 0-4-4H7a4 4 0 0 0-4 4v2", "M9.5 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8", "M20 21v-2a4 4 0 0 0-3-3.87", "M16 3.13a4 4 0 0 1 0 7.75"],
          allowedPerfis: ["ADMIN", "PROFESSOR"],
        },
        {
          label: "Professores",
          to: "/professores",
          icon: ["M12 3 2 8l10 5 10-5-10-5Z", "M6 10.5V16c0 1.7 2.7 3 6 3s6-1.3 6-3v-5.5", "M22 8v6"],
          allowedPerfis: ["ADMIN"],
        },
        {
          label: "Cursos",
          to: "/cursos",
          icon: ["M4 19.5A2.5 2.5 0 0 1 6.5 17H20", "M6.5 3H20v18H6.5A2.5 2.5 0 0 1 4 18.5v-13A2.5 2.5 0 0 1 6.5 3z"],
          allowedPerfis: ["ADMIN", "PROFESSOR"],
        },
        {
          label: "Disciplinas",
          to: "/disciplinas",
          icon: ["M5 4h11l3 3v13H5z", "M14 4v4h4", "M8 13h8", "M8 17h5"],
          allowedPerfis: ["ADMIN", "PROFESSOR"],
        },
        {
          label: "Matrículas",
          to: "/matriculas",
          icon: ["M8 7h8", "M8 12h8", "M8 17h5", "M5 3h14a2 2 0 0 1 2 2v14l-4-3-4 3-4-3-4 3V5a2 2 0 0 1 2-2Z"],
          allowedPerfis: ["ADMIN"],
        },
        {
          label: "Notas",
          to: "/notas",
          icon: ["M9 11l3 3L22 4", "M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"],
          allowedPerfis: ["ADMIN", "PROFESSOR"],
        },
        {
          label: "Frequências",
          to: "/frequencias",
          icon: ["M8 2v4", "M16 2v4", "M3 10h18", "M5 5h14a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2Z", "M9 15l2 2 4-4"],
          allowedPerfis: ["ADMIN", "PROFESSOR"],
        },
        {
          label: "Usuários",
          to: "/usuarios",
          icon: ["M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2", "M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8", "M23 21v-2a4 4 0 0 0-3-3.87", "M16 3.13a4 4 0 0 1 0 7.75"],
          allowedPerfis: ["ADMIN"],
        },
        {
          label: "Meus Dados",
          to: "/meus-dados",
          icon: ["M20 21a8 8 0 1 0-16 0", "M12 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8"],
          allowedPerfis: ["ALUNO"],
        },
        {
          label: "Meu Curso",
          to: "/meu-curso",
          icon: ["M12 3 2 8l10 5 10-5-10-5Z", "M6 10.5V16c0 1.7 2.7 3 6 3s6-1.3 6-3v-5.5"],
          allowedPerfis: ["ALUNO"],
        },
        {
          label: "Minhas Disciplinas",
          to: "/minhas-disciplinas",
          icon: ["M4 19.5A2.5 2.5 0 0 1 6.5 17H20", "M6.5 3H20v18H6.5A2.5 2.5 0 0 1 4 18.5v-13A2.5 2.5 0 0 1 6.5 3z"],
          allowedPerfis: ["ALUNO"],
        },
        {
          label: "Minhas Notas",
          to: "/minhas-notas",
          icon: ["M9 11l3 3L22 4", "M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"],
          allowedPerfis: ["ALUNO"],
        },
        {
          label: "Minha Frequência",
          to: "/minha-frequencia",
          icon: ["M8 2v4", "M16 2v4", "M3 10h18", "M5 5h14a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2Z", "M9 15l2 2 4-4"],
          allowedPerfis: ["ALUNO"],
        },
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
  width: 34px;
  height: 34px;
  flex: 0 0 34px;
  border-radius: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.08);
  color: rgba(255, 255, 255, 0.92);
}

.sidebar-icon svg {
  width: 18px;
  height: 18px;
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
