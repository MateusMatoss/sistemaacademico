<template>
  <div v-if="message" :class="['alert', `alert-${type}`]" role="alert">
    {{ message }}
  </div>
</template>

<script>
export default {
  name: "BaseAlert",
  props: {
    type: {
      type: String,
      default: "info",
    },
    message: {
      type: String,
      default: "",
    },
  },
};
</script>
