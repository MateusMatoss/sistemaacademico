<template>
  <CrudPage :config="config" />
</template>

<script>
import CrudPage from "../components/CrudPage.vue";
import authService from "../services/authService";
import disciplinaService from "../services/disciplinaService";
import cursoService from "../services/cursoService";
import professorService from "../services/professorService";

export default {
  name: "DisciplinasView",
  components: {
    CrudPage,
  },
  data() {
    const perfil = authService.getPerfil();
    const canManage = perfil === "ADMIN";

    return {
      config: {
        name: "disciplinas",
        title: "Gestão de disciplinas",
        singular: "disciplina",
        description: "Associe disciplinas aos cursos e professores responsáveis.",
        tableDescription: "A busca por nome utiliza o endpoint /disciplinas/buscar?nome=.",
        service: disciplinaService,
        permissions: {
          create: canManage,
          edit: canManage,
          delete: canManage,
        },
        search: true,
        searchPlaceholder: "Buscar disciplina por nome",
        formDefaults: {
          nomeDisciplina: "",
          cargaHoraria: 80,
          idCurso: "",
          idProfessor: "",
        },
        fields: [
          { key: "nomeDisciplina", label: "Nome da disciplina", required: true },
          { key: "cargaHoraria", label: "Carga horária", required: true, type: "number", min: 1 },
          { key: "idCurso", label: "Curso", type: "select", optionsKey: "cursos", placeholder: "Selecione um curso" },
          { key: "idProfessor", label: "Professor", type: "select", optionsKey: "professores", placeholder: "Selecione um professor" },
        ],
        columns: [
          { key: "idDisciplina", label: "ID" },
          { key: "nomeDisciplina", label: "Disciplina" },
          { key: "cargaHoraria", label: "Carga horária" },
          { key: "curso.nomeCurso", label: "Curso" },
          { key: "professor.nomePessoa", label: "Professor" },
        ],
        idKey: "idDisciplina",
        normalizeEmptyValues: ["idCurso", "idProfessor"],
        async loadDependencies() {
          const [cursos, professores] = await Promise.all([
            cursoService.listAll(),
            professorService.listAll(),
          ]);

          return {
            cursos: cursos.map((curso) => ({
              value: curso.idCurso,
              label: curso.nomeCurso,
            })),
            professores: professores.map((professor) => ({
              value: professor.idPessoa,
              label: professor.nomePessoa,
            })),
          };
        },
        fromItem(item) {
          return {
            nomeDisciplina: item.nomeDisciplina,
            cargaHoraria: item.cargaHoraria,
            idCurso: item.curso?.idCurso || "",
            idProfessor: item.professor?.idPessoa || "",
          };
        },
      },
    };
  },
};
</script>
