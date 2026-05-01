<template>
  <CrudPage :config="config" />
</template>

<script>
import CrudPage from "../components/CrudPage.vue";
import matriculaService from "../services/matriculaService";
import alunoService from "../services/alunoService";
import disciplinaService from "../services/disciplinaService";

export default {
  name: "MatriculasView",
  components: {
    CrudPage,
  },
  data() {
    return {
      config: {
        name: "matriculas",
        title: "Gestão de matrículas",
        singular: "matrícula",
        description: "Relacione alunos às disciplinas com data de matrícula controlada pela API.",
        service: matriculaService,
        formDefaults: {
          dataMatricula: new Date().toISOString().slice(0, 10),
          idAluno: "",
          idDisciplina: "",
        },
        fields: [
          { key: "dataMatricula", label: "Data da matrícula", type: "date", required: true },
          { key: "idAluno", label: "Aluno", type: "select", optionsKey: "alunos", required: true, placeholder: "Selecione um aluno" },
          { key: "idDisciplina", label: "Disciplina", type: "select", optionsKey: "disciplinas", required: true, placeholder: "Selecione uma disciplina" },
        ],
        columns: [
          { key: "idMatricula", label: "ID" },
          { key: "dataMatricula", label: "Data" },
          { key: "aluno.nomePessoa", label: "Aluno" },
          { key: "disciplina.nomeDisciplina", label: "Disciplina" },
          { key: "disciplina.curso.nomeCurso", label: "Curso" },
        ],
        idKey: "idMatricula",
        async loadDependencies() {
          const [alunos, disciplinas] = await Promise.all([
            alunoService.listAll(),
            disciplinaService.listAll(),
          ]);

          return {
            alunos: alunos.map((aluno) => ({
              value: aluno.idPessoa,
              label: `${aluno.nomePessoa} (${aluno.raAluno})`,
            })),
            disciplinas: disciplinas.map((disciplina) => ({
              value: disciplina.idDisciplina,
              label: `${disciplina.nomeDisciplina} - ${disciplina.curso?.nomeCurso || "Sem curso"}`,
            })),
          };
        },
        fromItem(item) {
          return {
            dataMatricula: item.dataMatricula,
            idAluno: item.aluno?.idPessoa || "",
            idDisciplina: item.disciplina?.idDisciplina || "",
          };
        },
      },
    };
  },
};
</script>
