<template>
    <div>
        <h1>Dashboard</h1>
        <p>Login realizado com sucesso.</p>

        <button @click="sair">Sair</button>
    </div>
</template>

<script>
export default {
    methods: {
        sair() {
            localStorage.removeItem("token");
            this.$router.push("/");
        }
    }
};
</script>