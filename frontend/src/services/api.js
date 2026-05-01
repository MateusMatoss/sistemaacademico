import axios from "axios";
import authService from "./authService";

let unauthorizedHandler = null;

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://127.0.0.1:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config) => {
  const token = authService.getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const requestUrl = error.config?.url || "";

    if (status === 401 && !requestUrl.includes("/auth/login")) {
      authService.logout();

      if (typeof unauthorizedHandler === "function") {
        unauthorizedHandler();
      }
    }

    return Promise.reject(error);
  },
);

export function setUnauthorizedHandler(handler) {
  unauthorizedHandler = handler;
}

export function getErrorMessage(error) {
  const responseData = error?.response?.data;

  if (!error?.response) {
    if (error?.code === "ERR_NETWORK") {
      return `Não foi possível conectar ao backend em ${api.defaults.baseURL}. Verifique se a API Spring Boot está em execução e se o navegador consegue acessar esse endereço.`;
    }

    return "Falha de conexão com o servidor. Verifique se o backend está ativo.";
  }

  if (typeof responseData === "string") {
    return responseData;
  }

  if (responseData?.message) {
    return responseData.message;
  }

  if (responseData?.mensagem) {
    return responseData.mensagem;
  }

  if (responseData?.erro && responseData?.mensagem) {
    return `${responseData.erro}: ${responseData.mensagem}`;
  }

  if (responseData?.error) {
    return responseData.error;
  }

  if (responseData?.errors && typeof responseData.errors === "object") {
    const messages = Object.values(responseData.errors);
    if (messages.length) {
      return messages.join(" | ");
    }
  }

  return "Não foi possível concluir a operação. Tente novamente.";
}

export default api;
