import api from "./api";

export default {
  async listAll() {
    const { data } = await api.get("/frequencias");
    return data;
  },
  async listByMatricula(idMatricula) {
    const { data } = await api.get(`/frequencias/matricula/${idMatricula}`);
    return data;
  },
  async create(payload) {
    const { data } = await api.post("/frequencias", payload);
    return data;
  },
  async update(id, payload) {
    const { data } = await api.put(`/frequencias/${id}`, payload);
    return data;
  },
  async remove(id) {
    const { data } = await api.delete(`/frequencias/${id}`);
    return data;
  },
};
