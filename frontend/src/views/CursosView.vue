<template>
  <CrudPage :config="config" />
</template>

<script>
import CrudPage from "../components/CrudPage.vue";
import authService from "../services/authService";
import cursoService from "../services/cursoService";

export default {
  name: "CursosView",
  components: {
    CrudPage,
  },
  data() {
    const perfil = authService.getPerfil();
    const canManage = perfil === "ADMIN";

    return {
      config: {
        name: "cursos",
        title: "Gestão de cursos",
        singular: "curso",
        description: "Cadastre e mantenha os cursos oferecidos pela instituição.",
        tableDescription: "A busca por nome utiliza o endpoint /cursos/buscar?nome=.",
        service: cursoService,
        permissions: {
          create: canManage,
          edit: canManage,
          delete: canManage,
        },
        search: true,
        searchPlaceholder: "Buscar curso por nome",
        formDefaults: {
          nomeCurso: "",
          descricaoCurso: "",
        },
        fields: [
          { key: "nomeCurso", label: "Nome do curso", required: true, placeholder: "Nome do curso" },
          { key: "descricaoCurso", label: "Descrição", type: "textarea", placeholder: "Descrição opcional do curso" },
        ],
        columns: [
          { key: "idCurso", label: "ID" },
          { key: "nomeCurso", label: "Curso" },
          { key: "descricaoCurso", label: "Descrição" },
        ],
        idKey: "idCurso",
        fromItem(item) {
          return {
            nomeCurso: item.nomeCurso,
            descricaoCurso: item.descricaoCurso,
          };
        },
      },
    };
  },
};
</script>
