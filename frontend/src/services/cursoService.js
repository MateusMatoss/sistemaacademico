import api from "./api";

export default {
  async listAll() {
    const { data } = await api.get("/cursos");
    return data;
  },
  async searchByName(nome) {
    const { data } = await api.get("/cursos/buscar", { params: { nome } });
    return data;
  },
  async create(payload) {
    const { data } = await api.post("/cursos", payload);
    return data;
  },
  async update(id, payload) {
    const { data } = await api.put(`/cursos/${id}`, payload);
    return data;
  },
  async remove(id) {
    const { data } = await api.delete(`/cursos/${id}`);
    return data;
  },
};
