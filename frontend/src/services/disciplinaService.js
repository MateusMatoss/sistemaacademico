import api from "./api";

export default {
  async listAll() {
    const { data } = await api.get("/disciplinas");
    return data;
  },
  async searchByName(nome) {
    const { data } = await api.get("/disciplinas/buscar", { params: { nome } });
    return data;
  },
  async create(payload) {
    const { data } = await api.post("/disciplinas", payload);
    return data;
  },
  async update(id, payload) {
    const { data } = await api.put(`/disciplinas/${id}`, payload);
    return data;
  },
  async remove(id) {
    const { data } = await api.delete(`/disciplinas/${id}`);
    return data;
  },
};
