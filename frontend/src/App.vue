<template>
  <div v-if="isAuthLayout" class="app-layout">
    <Sidebar
      :open="sidebarOpen"
      :perfil="perfil"
      @close="sidebarOpen = false"
      @logout="handleLogout"
    />

    <div class="app-main">
      <Header
        :title="routeTitle"
        :perfil="perfil"
        @toggle-sidebar="sidebarOpen = !sidebarOpen"
        @logout="handleLogout"
      />

      <main class="content-area">
        <router-view />
      </main>
    </div>
  </div>

  <router-view v-else />
</template>

<script>
import Header from "./components/Header.vue";
import Sidebar from "./components/Sidebar.vue";
import authService from "./services/authService";

export default {
  name: "App",
  components: {
    Header,
    Sidebar,
  },
  data() {
    return {
      sidebarOpen: false,
      perfilUsuario: authService.getPerfil(),
    };
  },
  computed: {
    isAuthLayout() {
      return this.$route.meta.requiresAuth && !this.$route.meta.standalone;
    },
    routeTitle() {
      return this.$route.meta.title || "Sistema Acadêmico";
    },
    perfil() {
      return this.perfilUsuario;
    },
  },
  watch: {
    $route() {
      this.sidebarOpen = false;
      this.refreshPerfil();
    },
  },
  created() {
    this.refreshPerfil();
  },
  methods: {
    refreshPerfil() {
      this.perfilUsuario = authService.getPerfil();
    },
    handleLogout() {
      authService.logout();
      this.refreshPerfil();
      this.$router.push("/login");
    },
  },
};
</script>
