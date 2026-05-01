import api from "./api";

export default {
  async listAll() {
    const { data } = await api.get("/matriculas");
    return data;
  },
  async listByDisciplina(idDisciplina) {
    const { data } = await api.get(`/matriculas/disciplina/${idDisciplina}`);
    return data;
  },
  async create(payload) {
    const { data } = await api.post("/matriculas", payload);
    return data;
  },
  async update(id, payload) {
    const { data } = await api.put(`/matriculas/${id}`, payload);
    return data;
  },
  async remove(id) {
    const { data } = await api.delete(`/matriculas/${id}`);
    return data;
  },
};
