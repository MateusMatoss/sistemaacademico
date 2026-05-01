<template>
  <CrudPage :config="config" />
</template>

<script>
import CrudPage from "../components/CrudPage.vue";
import professorService from "../services/professorService";

export default {
  name: "ProfessoresView",
  components: {
    CrudPage,
  },
  data() {
    return {
      config: {
        name: "professores",
        title: "Gestão de professores",
        singular: "professor",
        description: "Gerencie os dados cadastrais, formação e titulação dos professores. Ao cadastrar, o sistema cria o usuário de acesso com o e-mail do professor e senha inicial igual aos 6 últimos dígitos do CPF.",
        tableDescription: "A busca por nome utiliza o endpoint /professores/buscar?nome=.",
        service: professorService,
        search: true,
        searchPlaceholder: "Buscar professor por nome",
        formDefaults: {
          nomePessoa: "",
          cpfPessoa: "",
          emailPessoa: "",
          formacaoProfessor: "",
          titulacaoProfessor: "",
        },
        fields: [
          { key: "nomePessoa", label: "Nome completo", required: true },
          { key: "cpfPessoa", label: "CPF", required: true },
          { key: "emailPessoa", label: "E-mail", required: true, type: "email" },
          { key: "formacaoProfessor", label: "Formação", required: true, placeholder: "Ex.: Teologia" },
          { key: "titulacaoProfessor", label: "Titulação", required: true, placeholder: "Ex.: Mestre" },
        ],
        columns: [
          { key: "idPessoa", label: "ID" },
          { key: "nomePessoa", label: "Nome" },
          { key: "cpfPessoa", label: "CPF" },
          { key: "emailPessoa", label: "E-mail" },
          { key: "formacaoProfessor", label: "Formação" },
          { key: "titulacaoProfessor", label: "Titulação" },
        ],
        idKey: "idPessoa",
        fromItem(item) {
          return {
            nomePessoa: item.nomePessoa,
            cpfPessoa: item.cpfPessoa,
            emailPessoa: item.emailPessoa,
            formacaoProfessor: item.formacaoProfessor,
            titulacaoProfessor: item.titulacaoProfessor,
          };
        },
      },
    };
  },
};
</script>
