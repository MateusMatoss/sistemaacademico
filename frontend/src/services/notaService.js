import api from "./api";

export default {
  async listAll() {
    const { data } = await api.get("/notas");
    return data;
  },
  async listByMatricula(idMatricula) {
    const { data } = await api.get(`/notas/matricula/${idMatricula}`);
    return data;
  },
  async create(payload) {
    const { data } = await api.post("/notas", payload);
    return data;
  },
  async update(id, payload) {
    const { data } = await api.put(`/notas/${id}`, payload);
    return data;
  },
  async remove(id) {
    const { data } = await api.delete(`/notas/${id}`);
    return data;
  },
};
