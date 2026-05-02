<template>
  <article :class="['summary-card', `summary-card--${variant}`]">
    <div class="summary-card__top">
      <div>
        <span class="summary-card__label">{{ titulo }}</span>
        <strong class="summary-card__value">{{ valor }}</strong>
      </div>

      <span class="summary-card__badge" aria-hidden="true">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
          <path v-for="(path, index) in badge" :key="`badge-${index}`" :d="path" />
        </svg>
      </span>
    </div>

    <p class="summary-card__hint">{{ descricao }}</p>

    <div v-if="meta" class="summary-card__meta">
      {{ meta }}
    </div>
  </article>
</template>

<script>
export default {
  name: "CardResumo",
  props: {
    titulo: {
      type: String,
      required: true,
    },
    valor: {
      type: [String, Number],
      required: true,
    },
    descricao: {
      type: String,
      default: "Indicador atualizado pela API.",
    },
    badge: {
      type: Array,
      default: () => ["M12 5v14", "M5 12h14"],
    },
    meta: {
      type: String,
      default: "",
    },
    variant: {
      type: String,
      default: "blue",
    },
  },
};
</script>

<style scoped>
.summary-card {
  position: relative;
  overflow: hidden;
  min-height: 190px;
  background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
  border-radius: 22px;
  border: 1px solid rgba(214, 223, 236, 0.88);
  padding: 22px;
  box-shadow: var(--shadow-sm);
}

.summary-card::before {
  content: "";
  position: absolute;
  inset: 0 auto auto 0;
  width: 100%;
  height: 4px;
  background: var(--card-accent, #2563eb);
}

.summary-card__top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.summary-card__label {
  display: block;
  color: var(--text-soft);
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  margin-bottom: 14px;
}

.summary-card__value {
  display: block;
  font-size: 42px;
  line-height: 0.95;
  margin-bottom: 12px;
  color: #10213a;
}

.summary-card__badge {
  flex: 0 0 auto;
  width: 48px;
  height: 48px;
  border-radius: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.08em;
  color: var(--card-accent, #2563eb);
  background: var(--card-soft, #dbeafe);
}

.summary-card__badge svg {
  width: 20px;
  height: 20px;
}

.summary-card__hint {
  margin: 0;
  color: var(--text-soft);
  font-size: 14px;
  line-height: 1.6;
}

.summary-card__meta {
  margin-top: 18px;
  padding-top: 14px;
  border-top: 1px solid rgba(214, 223, 236, 0.75);
  font-size: 13px;
  font-weight: 700;
  color: #30425f;
}

.summary-card--blue {
  --card-accent: #2563eb;
  --card-soft: #dbeafe;
}

.summary-card--navy {
  --card-accent: #0f172a;
  --card-soft: #e2e8f0;
}

.summary-card--green {
  --card-accent: #15803d;
  --card-soft: #dcfce7;
}

.summary-card--amber {
  --card-accent: #d97706;
  --card-soft: #fef3c7;
}

.summary-card--red {
  --card-accent: #dc2626;
  --card-soft: #fee2e2;
}

.summary-card--cyan {
  --card-accent: #0891b2;
  --card-soft: #cffafe;
}

.summary-card--indigo {
  --card-accent: #4f46e5;
  --card-soft: #e0e7ff;
}
</style>
