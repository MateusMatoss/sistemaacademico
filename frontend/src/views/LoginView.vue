<template>
    <div>
        <h2>Login</h2>

        <input v-model="username" placeholder="Usuário" />
        <input v-model="password" type="password" placeholder="Senha" />

        <button @click="fazerLogin">Entrar</button>

        <p v-if="erro">{{ erro }}</p>
    </div>
</template>

<script>
import { login } from "../services/auth";

export default {
    data() {
        return {
            username: "",
            password: "",
            erro: ""
        };
    },
    methods: {
        async fazerLogin() {
            try {
                const response = await login({
                    username: this.username,
                    password: this.password
                });

                localStorage.setItem("token", response.token);

                alert("Login realizado!");
            } catch (e) {
                this.erro = "Erro no login";
            }
        }
    }
};
</script>