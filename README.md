# 🎓 Sistema Acadêmico - Espalhando a Palavra

API REST desenvolvida em **Java + Spring Boot** para gerenciamento acadêmico de alunos, professores, cursos, disciplinas, matrículas, notas, frequências e usuários.

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- PostgreSQL
- Spring Security + JWT
- Maven
- Lombok
- Swagger (OpenAPI)

---

## 🧠 Arquitetura

O projeto segue o padrão **MVC em camadas**:

controller → service → repository → model

Separando responsabilidades e facilitando manutenção e escalabilidade.

---

## 🗄️ Banco de Dados

Banco utilizado:

bd_sistemaacademico


---

## ⚙️ Configuração (`application.yaml`)

Crie o arquivo:

src/main/resources/application.yaml

E configure:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bd_sistemaacademico
    username: postgres
    password: sua_senha

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

  jackson:
    time-zone: America/Sao_Paulo

server:
  port: 8080

```
---

## ▶️ Como Rodar o Projeto

1. Criar o banco no PostgreSQL: bd_sistemaacademico
2. Configurar o arquivo `application.yaml`
3. Rodar o projeto pelo IntelliJ IDEA
4. Acessar: http://localhost:8080

---

## 📄 Swagger (Documentação da API)

Acesse: http://localhost:8080/swagger-ui/index.html


---

## 🔐 Autenticação (JWT)

### Login

```http
POST /auth/login
{
  "username": "admin",
  "password": "123"
}
```
---
Cadastro de usuário
```http
POST /auth/registrar
{
"username": "admin",
"password": "123",
"perfil": "ADMIN"
}
```
---
Uso do Token

Adicione no header:  
Authorization: Bearer seu_token_aqui

---

# 📚 Endpoints Principais
## 👨‍🎓 Alunos  
GET /alunos  
GET /alunos/paginado  
GET /alunos/buscar?nome=...  
GET /alunos/{id}  
POST /alunos  
PUT /alunos/{id}  
DELETE /alunos/{id}  

---

## 👨‍🏫 Professores
GET /professores  
GET /professores/paginado  
GET /professores/buscar?nome=...  
POST /professores  
PUT /professores/{id}  
DELETE /professores/{id}  

---

## 📘 Cursos
GET /cursos  
GET /cursos/paginado  
GET /cursos/buscar?nome=...  
POST /cursos  
PUT /cursos/{id}  
DELETE /cursos/{id}  

---

## 📖 Disciplinas
GET /disciplinas  
GET /disciplinas/paginado  
GET /disciplinas/buscar?nome=...  
GET /disciplinas/curso/{id}  
GET /disciplinas/professor/{id}  
POST /disciplinas  
PUT /disciplinas/{id}  
DELETE /disciplinas/{id}  

---

## 📝 Matrículas
GET /matriculas  
GET /matriculas/paginado  
POST /matriculas  
PUT /matriculas/{id}  
DELETE /matriculas/{id}  

---

## 📊 Notas
GET /notas  
GET /notas/paginado  
GET /notas/matricula/{id}  
POST /notas  
PUT /notas/{id}  
DELETE /notas/{id}  

---

## 📅 Frequências
GET /frequencias  
GET /frequencias/paginado  
GET /frequencias/matricula/{id}  
POST /frequencias  
PUT /frequencias/{id}  
DELETE /frequencias/{id}  

---

## 👤 Usuários
GET /usuarios  
GET /usuarios/paginado  
GET /usuarios/ativos  
GET /usuarios/inativos  
GET /usuarios/perfil/{perfil}  
GET /usuarios/username/{username}  

PUT /usuarios/{id}  
PUT /usuarios/{id}/alterar-senha  
PUT /usuarios/{id}/alterar-perfil  
PUT /usuarios/{id}/ativar  
PUT /usuarios/{id}/desativar  

DELETE /usuarios/{id}  

---

## 📊 Logs de Acesso
GET /logs-acesso  
GET /logs-acesso/paginado  
GET /logs-acesso/usuario/{username}  
GET /logs-acesso/perfil/{perfil}  
GET /logs-acesso/periodo?inicio=...&fim=...  

---

# 🛡️ Segurança
- Autenticação com JWT
- Senhas criptografadas (BCrypt)
- Controle de acesso por perfil:
  - ADMIN
  - PROFESSOR
  - ALUNO

---

# 📦 Funcionalidades Implementadas
- ✔ CRUD completo
- ✔ Paginação
- ✔ Filtros por nome, CPF e perfil
- ✔ Autenticação JWT
- ✔ Controle de usuários (ativo/inativo)
- ✔ Logs de acesso
- ✔ DTOs para segurança
- ✔ Respostas padronizadas

--- 

# 👨‍💻 Autores

## Mateus Silva de Matos
## Theo Pinheiro de Almeida Fernades Botelho da Ponte

Projeto desenvolvido para fins acadêmicos (TCC / ADS - FATEC)