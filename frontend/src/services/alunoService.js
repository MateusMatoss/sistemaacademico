import api from "./api";

export default {
  async listAll() {
    const { data } = await api.get("/alunos");
    return data;
  },
  async getMe() {
    const { data } = await api.get("/alunos/me");
    return data;
  },
  async getMySummary() {
    const { data } = await api.get("/alunos/me/resumo");
    return data;
  },
  async getMyDisciplines() {
    const { data } = await api.get("/alunos/me/disciplinas");
    return data;
  },
  async getMyCourse() {
    const { data } = await api.get("/alunos/me/curso");
    return data;
  },
  async searchByName(nome) {
    const { data } = await api.get("/alunos/buscar", { params: { nome } });
    return data;
  },
  async create(payload) {
    const { data } = await api.post("/alunos", payload);
    return data;
  },
  async update(id, payload) {
    const { data } = await api.put(`/alunos/${id}`, payload);
    return data;
  },
  async remove(id) {
    const { data } = await api.delete(`/alunos/${id}`);
    return data;
  },
};
