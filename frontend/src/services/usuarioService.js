import api from "./api";

export default {
  async listAll() {
    const { data } = await api.get("/usuarios");
    return data;
  },
  async listAtivos() {
    const { data } = await api.get("/usuarios/ativos");
    return data;
  },
  async listInativos() {
    const { data } = await api.get("/usuarios/inativos");
    return data;
  },
  async listByPerfil(perfil) {
    const { data } = await api.get(`/usuarios/perfil/${perfil}`);
    return data;
  },
  async update(id, payload) {
    const { data } = await api.put(`/usuarios/${id}`, payload);
    return data;
  },
  async alterarPerfil(id, perfil) {
    const { data } = await api.put(`/usuarios/${id}/alterar-perfil`, { perfil });
    return data;
  },
  async alterarSenha(id, payload) {
    const { data } = await api.put(`/usuarios/${id}/alterar-senha`, payload);
    return data;
  },
  async ativar(id) {
    const { data } = await api.put(`/usuarios/${id}/ativar`);
    return data;
  },
  async desativar(id) {
    const { data } = await api.put(`/usuarios/${id}/desativar`);
    return data;
  },
  async remove(id) {
    const { data } = await api.delete(`/usuarios/${id}`);
    return data;
  },
};
