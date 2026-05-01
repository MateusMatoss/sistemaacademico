import api from "./api";

export default {
  async listAll() {
    const { data } = await api.get("/professores");
    return data;
  },
  async searchByName(nome) {
    const { data } = await api.get("/professores/buscar", { params: { nome } });
    return data;
  },
  async create(payload) {
    const { data } = await api.post("/professores", payload);
    return data;
  },
  async update(id, payload) {
    const { data } = await api.put(`/professores/${id}`, payload);
    return data;
  },
  async remove(id) {
    const { data } = await api.delete(`/professores/${id}`);
    return data;
  },
};
