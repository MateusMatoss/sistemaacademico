<template>
  <CrudPage :config="config" />
</template>

<script>
import CrudPage from "../components/CrudPage.vue";
import alunoService from "../services/alunoService";
import authService from "../services/authService";
import cursoService from "../services/cursoService";

export default {
  name: "AlunosView",
  components: {
    CrudPage,
  },
  data() {
    const perfil = authService.getPerfil();
    const canManage = perfil === "ADMIN";

    return {
      config: {
        name: "alunos",
        title: "Gestão de alunos",
        singular: "aluno",
        description: "Cadastre, atualize, busque e exclua alunos vinculados aos cursos da instituição.",
        tableDescription: "A busca por nome utiliza o endpoint /alunos/buscar?nome=.",
        service: alunoService,
        permissions: {
          create: canManage,
          edit: canManage,
          delete: canManage,
        },
        search: true,
        searchPlaceholder: "Buscar aluno por nome",
        formDefaults: {
          nomePessoa: "",
          cpfPessoa: "",
          emailPessoa: "",
          raAluno: "",
          idCurso: "",
        },
        fields: [
          { key: "nomePessoa", label: "Nome completo", required: true, placeholder: "Nome do aluno" },
          { key: "cpfPessoa", label: "CPF", required: true, placeholder: "000.000.000-00" },
          { key: "emailPessoa", label: "E-mail", required: true, type: "email", placeholder: "email@exemplo.com" },
          { key: "raAluno", label: "RA", required: true, placeholder: "Registro acadêmico" },
          { key: "idCurso", label: "Curso", type: "select", optionsKey: "cursos", placeholder: "Selecione um curso" },
        ],
        columns: [
          { key: "idPessoa", label: "ID" },
          { key: "nomePessoa", label: "Nome" },
          { key: "cpfPessoa", label: "CPF" },
          { key: "emailPessoa", label: "E-mail" },
          { key: "raAluno", label: "RA" },
          { key: "curso.nomeCurso", label: "Curso" },
        ],
        idKey: "idPessoa",
        normalizeEmptyValues: ["idCurso"],
        async loadDependencies() {
          const cursos = await cursoService.listAll();
          return {
            cursos: cursos.map((curso) => ({
              value: curso.idCurso,
              label: curso.nomeCurso,
            })),
          };
        },
        fromItem(item) {
          return {
            nomePessoa: item.nomePessoa,
            cpfPessoa: item.cpfPessoa,
            emailPessoa: item.emailPessoa,
            raAluno: item.raAluno,
            idCurso: item.curso?.idCurso || "",
          };
        },
      },
    };
  },
};
</script>
