<template>
  <section class="dashboard-page">
    <section class="dashboard-hero">
      <div class="dashboard-hero__content">
        <span class="dashboard-hero__eyebrow">Visão institucional</span>
        <h1>Dashboard acadêmico</h1>
        <p>
          Acompanhe a base acadêmica em tempo real com foco em operação,
          volume de cadastros e atividade de ensino.
        </p>

        <div class="dashboard-hero__chips">
          <span>API Spring Boot</span>
          <span>Resumo operacional</span>
          <span>Atualização sob demanda</span>
        </div>

        <button
          v-if="!isAdmin"
          type="button"
          class="btn btn-primary dashboard-hero__action"
          @click="loadResumo"
          :disabled="loading"
        >
          {{ loading ? "Atualizando..." : "Atualizar painel" }}
        </button>
      </div>

      <div v-if="isAdmin" class="dashboard-hero__highlight">
        <span class="dashboard-hero__label">Base total monitorada</span>
        <strong>{{ totalRegistros }}</strong>
        <p>
          Soma de alunos, professores, cursos, disciplinas, matrículas, notas e
          frequências registradas no sistema.
        </p>
        <button
          type="button"
          class="btn btn-primary"
          @click="loadResumo"
          :disabled="loading"
        >
          {{ loading ? "Atualizando..." : "Atualizar painel" }}
        </button>
      </div>
    </section>

    <BaseAlert
      v-if="alert.message"
      :message="alert.message"
      :type="alert.type"
    />

    <div v-if="loading" class="loading-box">
      Carregando resumo geral...
    </div>

    <template v-else>
      <section class="dashboard-strip">
        <article class="dashboard-mini-card">
          <span>Alunos por curso</span>
          <strong>{{ averagePerCourse }}</strong>
          <p>Média estimada usando o total atual de alunos e cursos.</p>
        </article>

        <article class="dashboard-mini-card">
          <span>Matrículas por disciplina</span>
          <strong>{{ averageEnrollmentsPerDiscipline }}</strong>
          <p>Indica densidade média de vínculos acadêmicos por disciplina.</p>
        </article>

        <article class="dashboard-mini-card">
          <span>Notas por matrícula</span>
          <strong>{{ averageGradesPerEnrollment }}</strong>
          <p>Ajuda a visualizar o volume médio de avaliações lançadas.</p>
        </article>
      </section>

      <section :class="['dashboard-grid', { 'dashboard-grid--compact': !isAdmin }]">
        <div class="dashboard-main">
          <div class="grid-cards">
            <CardResumo
              v-for="card in visibleCards"
              :key="card.key"
              :titulo="card.label"
              :valor="resumo[card.key] ?? 0"
              :descricao="card.description"
              :badge="card.badge"
              :meta="card.meta"
              :variant="card.variant"
            />
          </div>
        </div>

        <aside v-if="isAdmin" class="dashboard-side">
          <article class="insight-panel">
            <div class="insight-panel__header">
              <span class="insight-panel__eyebrow">Leitura rápida</span>
              <h2>Pontos de atenção</h2>
            </div>

            <div class="insight-list">
              <div
                v-for="insight in insights"
                :key="insight.title"
                class="insight-item"
              >
                <strong>{{ insight.title }}</strong>
                <p>{{ insight.description }}</p>
              </div>
            </div>
          </article>

          <article class="distribution-panel">
            <div class="insight-panel__header">
              <span class="insight-panel__eyebrow">Composição da base</span>
              <h2>Participação relativa</h2>
            </div>

            <div
              v-for="item in composition"
              :key="item.label"
              class="distribution-row"
            >
              <div class="distribution-row__top">
                <strong>{{ item.label }}</strong>
                <span>{{ item.value }} • {{ item.percent }}%</span>
              </div>
              <div class="distribution-row__bar">
                <span :style="{ width: `${item.percent}%` }"></span>
              </div>
            </div>
          </article>
        </aside>
      </section>
    </template>
  </section>
</template>

<script>
import BaseAlert from "../components/BaseAlert.vue";
import CardResumo from "../components/CardResumo.vue";
import authService from "../services/authService";
import api, { getErrorMessage } from "../services/api";

export default {
  name: "DashboardView",
  components: {
    BaseAlert,
    CardResumo,
  },
  data() {
    return {
      loading: false,
      resumo: {},
      alert: {
        type: "info",
        message: "",
      },
      cards: [
        {
          key: "totalAlunos",
          label: "Alunos",
          badge: "AL",
          variant: "blue",
          description: "Volume total de alunos cadastrados na instituição.",
          meta: "Base principal de acompanhamento acadêmico.",
        },
        {
          key: "totalProfessores",
          label: "Professores",
          badge: "PR",
          variant: "navy",
          description: "Corpo docente registrado para oferta e condução das aulas.",
          meta: "Equipe responsável por disciplinas, notas e frequências.",
        },
        {
          key: "totalCursos",
          label: "Cursos",
          badge: "CU",
          variant: "green",
          description: "Estrutura curricular disponível no sistema acadêmico.",
          meta: "Serve de base para alunos e disciplinas vinculadas.",
        },
        {
          key: "totalDisciplinas",
          label: "Disciplinas",
          badge: "DI",
          variant: "indigo",
          description: "Componentes acadêmicos ativos para matrícula e ensino.",
          meta: "Distribuem a carga letiva por curso.",
        },
        {
          key: "totalMatriculas",
          label: "Matrículas",
          badge: "MT",
          variant: "amber",
          description: "Relações entre alunos e disciplinas atualmente registradas.",
          meta: "Indicador direto de movimentação acadêmica.",
        },
        {
          key: "totalNotas",
          label: "Notas",
          badge: "NT",
          variant: "cyan",
          description: "Lançamentos de avaliação efetuados pelos professores.",
          meta: "Reflete o ritmo de registro pedagógico.",
        },
        {
          key: "totalFrequencias",
          label: "Frequências",
          badge: "FR",
          variant: "red",
          description: "Registros de presença e ausência por aula lançados no sistema.",
          meta: "Mostra o acompanhamento diário da turma.",
        },
      ],
    };
  },
  computed: {
    isAdmin() {
      return authService.getPerfil() === "ADMIN";
    },
    visibleCards() {
      if (this.isAdmin) {
        return this.cards;
      }

      return this.cards.filter((card) => card.key !== "totalProfessores");
    },
    totalRegistros() {
      return this.cards.reduce(
        (sum, card) => sum + Number(this.resumo[card.key] ?? 0),
        0
      );
    },
    averagePerCourse() {
      return this.safeAverage(this.resumo.totalAlunos, this.resumo.totalCursos);
    },
    averageEnrollmentsPerDiscipline() {
      return this.safeAverage(
        this.resumo.totalMatriculas,
        this.resumo.totalDisciplinas
      );
    },
    averageGradesPerEnrollment() {
      return this.safeAverage(this.resumo.totalNotas, this.resumo.totalMatriculas);
    },
    insights() {
      return [
        {
          title: "Cobertura de oferta",
          description: `${this.resumo.totalDisciplinas ?? 0} disciplinas distribuídas em ${this.resumo.totalCursos ?? 0} cursos cadastrados.`,
        },
        {
          title: "Capacidade docente",
          description: `${this.resumo.totalProfessores ?? 0} professores para atender ${this.resumo.totalMatriculas ?? 0} matrículas registradas.`,
        },
        {
          title: "Ritmo de lançamento",
          description: `${this.resumo.totalNotas ?? 0} notas e ${this.resumo.totalFrequencias ?? 0} frequências já foram informadas.`,
        },
      ];
    },
    composition() {
      if (!this.totalRegistros) {
        return [];
      }

      return this.cards.map((card) => {
        const value = Number(this.resumo[card.key] ?? 0);
        const percent = Math.round((value / this.totalRegistros) * 100);

        return {
          label: card.label,
          value,
          percent,
        };
      });
    },
  },
  async created() {
    await this.loadResumo();
  },
  methods: {
    safeAverage(total, divisor) {
      const parsedTotal = Number(total ?? 0);
      const parsedDivisor = Number(divisor ?? 0);

      if (!parsedDivisor) {
        return "0,0";
      }

      return (parsedTotal / parsedDivisor).toFixed(1).replace(".", ",");
    },
    async loadResumo() {
      this.loading = true;
      this.alert.message = "";

      try {
        const { data } = await api.get("/dashboard/resumo");
        this.resumo = data;
      } catch (error) {
        this.alert = {
          type: "error",
          message: getErrorMessage(error),
        };
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.dashboard-page {
  display: grid;
  gap: 24px;
}

.dashboard-hero {
  display: grid;
  grid-template-columns: minmax(0, 1.55fr) minmax(280px, 0.85fr);
  gap: 22px;
}

.dashboard-hero__content,
.dashboard-hero__highlight,
.dashboard-mini-card,
.insight-panel,
.distribution-panel {
  border-radius: 26px;
  overflow: hidden;
  position: relative;
}

.dashboard-hero__content {
  padding: 30px;
  background:
    radial-gradient(circle at top right, rgba(255, 255, 255, 0.18), transparent 28%),
    linear-gradient(145deg, #0f172a 0%, #163d86 52%, #2563eb 100%);
  color: #ffffff;
  box-shadow: var(--shadow);
}

.dashboard-hero__content h1 {
  margin: 0;
  font-size: 38px;
  line-height: 1.02;
}

.dashboard-hero__content p {
  margin: 14px 0 0;
  max-width: 620px;
  color: rgba(255, 255, 255, 0.82);
  line-height: 1.7;
}

.dashboard-hero__eyebrow,
.insight-panel__eyebrow {
  display: inline-block;
  margin-bottom: 12px;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.12em;
  text-transform: uppercase;
}

.dashboard-hero__eyebrow {
  color: rgba(255, 255, 255, 0.64);
}

.dashboard-hero__chips {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-top: 24px;
}

.dashboard-hero__action {
  margin-top: 24px;
}

.dashboard-hero__chips span {
  padding: 10px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.14);
  font-size: 13px;
  font-weight: 700;
}

.dashboard-hero__highlight {
  padding: 28px;
  background: linear-gradient(180deg, #ffffff 0%, #eef5ff 100%);
  border: 1px solid rgba(214, 223, 236, 0.9);
  box-shadow: var(--shadow-sm);
}

.dashboard-hero__label {
  display: block;
  color: var(--text-soft);
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.dashboard-hero__highlight strong {
  display: block;
  margin-top: 12px;
  font-size: 56px;
  line-height: 0.95;
  color: #10213a;
}

.dashboard-hero__highlight p {
  margin: 14px 0 24px;
  color: var(--text-soft);
  line-height: 1.6;
}

.dashboard-strip {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
}

.dashboard-mini-card {
  padding: 22px 24px;
  background: rgba(255, 255, 255, 0.82);
  border: 1px solid rgba(214, 223, 236, 0.9);
  box-shadow: var(--shadow-sm);
  backdrop-filter: blur(14px);
}

.dashboard-mini-card span {
  display: block;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: var(--text-soft);
}

.dashboard-mini-card strong {
  display: block;
  margin: 12px 0 8px;
  font-size: 34px;
  color: #10213a;
}

.dashboard-mini-card p {
  margin: 0;
  color: var(--text-soft);
  line-height: 1.55;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.55fr) minmax(300px, 0.85fr);
  gap: 22px;
  align-items: start;
}

.dashboard-grid--compact {
  grid-template-columns: 1fr;
}

.dashboard-main {
  min-width: 0;
}

.dashboard-side {
  display: grid;
  gap: 22px;
}

.insight-panel,
.distribution-panel {
  padding: 24px;
  background: #ffffff;
  border: 1px solid rgba(214, 223, 236, 0.9);
  box-shadow: var(--shadow-sm);
}

.insight-panel__eyebrow {
  color: var(--primary);
}

.insight-panel__header h2 {
  margin: 0;
  font-size: 24px;
  color: #10213a;
}

.insight-list {
  display: grid;
  gap: 14px;
  margin-top: 20px;
}

.insight-item {
  padding: 16px 18px;
  border-radius: 18px;
  background: linear-gradient(180deg, #f8fbff 0%, #f1f6fd 100%);
  border: 1px solid #dbe7f6;
}

.insight-item strong {
  display: block;
  margin-bottom: 6px;
  color: #12315d;
}

.insight-item p {
  margin: 0;
  color: var(--text-soft);
  line-height: 1.6;
}

.distribution-panel {
  display: grid;
  gap: 18px;
}

.distribution-row {
  display: grid;
  gap: 10px;
}

.distribution-row__top {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
  font-size: 14px;
}

.distribution-row__top strong {
  color: #10213a;
}

.distribution-row__top span {
  color: var(--text-soft);
}

.distribution-row__bar {
  height: 12px;
  border-radius: 999px;
  background: #e7edf7;
  overflow: hidden;
}

.distribution-row__bar span {
  display: block;
  height: 100%;
  min-width: 8px;
  border-radius: inherit;
  background: linear-gradient(90deg, #2563eb 0%, #0f172a 100%);
}

@media (max-width: 1120px) {
  .dashboard-hero,
  .dashboard-grid,
  .dashboard-strip {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 720px) {
  .dashboard-hero__content,
  .dashboard-hero__highlight,
  .dashboard-mini-card,
  .insight-panel,
  .distribution-panel {
    border-radius: 22px;
  }

  .dashboard-hero__content,
  .dashboard-hero__highlight,
  .insight-panel,
  .distribution-panel {
    padding: 22px;
  }

  .dashboard-hero__content h1 {
    font-size: 32px;
  }

  .dashboard-hero__highlight strong {
    font-size: 46px;
  }
}
</style>
