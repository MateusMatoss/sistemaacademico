# Histórico de Conversa e Principais Atualizações do Sistema

## Projeto
**Sistema Acadêmico - Espalhando a Palavra**

Stack principal:
- Backend: Spring Boot
- Frontend: Vue 3 + Vite + Vue Router + Axios
- Autenticação: JWT

Este documento resume as principais conversas, decisões e modificações aplicadas ao sistema durante a evolução do projeto.

---

## 1. Leitura inicial do projeto

No início, foi feita a leitura:
- do documento do projeto
- do backend existente

A partir disso, o sistema foi entendido como uma plataforma de gestão acadêmica para um seminário, cobrindo:
- alunos
- professores
- cursos
- disciplinas
- matrículas
- notas
- frequências
- usuários

Também foi identificado que havia diferenças entre o escopo descrito no documento e o que estava de fato implementado no backend.

---

## 2. Construção do frontend completo

Foi criado o frontend completo utilizando:
- Vue 3
- Vite
- JavaScript
- Vue Router
- Axios
- CSS puro com estilos organizados no próprio projeto

Estrutura criada no frontend:
- `components`
- `services`
- `router`
- `views`
- `App.vue`
- `main.js`

Funcionalidades base implementadas:
- login com JWT
- proteção de rotas
- layout com sidebar e header
- dashboard
- CRUDs principais
- tela de usuários
- tratamento de loading e mensagens de erro

---

## 3. Correções de integração entre frontend e backend

Durante os testes iniciais, surgiram problemas de login e conexão.

Foram tratados os seguintes pontos:
- backend inicialmente não estava rodando
- melhoria de mensagens de erro do frontend
- ajuste da URL base do Axios
- correções de CORS no backend

Depois disso, a comunicação entre:
- `http://localhost:5173`
- `http://localhost:8080`

ficou estável.

---

## 4. Ajustes de autenticação e usuários seed

Foram feitos ajustes no bootstrap do backend para garantir usuários de acesso padrão.

### Usuários seed criados/ajustados

#### Admin principal
- username: `admin`
- senha: `123`
- garantido como ativo
- garantido com perfil `ADMIN`

#### Admin alternativo
- username: `gestor.epalavra.2026`
- senha: `123456`
- perfil: `ADMIN`

#### Professor seed
- username: `professor@epalavra.edu`
- senha inicial: `123456`
- perfil: `PROFESSOR`
- marcado como senha temporária

---

## 5. Tela de usuários

Foi corrigido o problema de listagem de usuários inativos.

Melhorias aplicadas:
- filtro de ativos/inativos funcionando corretamente
- listagem estabilizada no frontend
- tratamento mais consistente de perfis e status

---

## 6. Frequência por aula

A tela de frequências foi remodelada para um fluxo mais rápido para professores.

Novo comportamento:
- seleção de disciplina
- seleção da data da aula
- carregamento automático de alunos matriculados
- todos já começam como presentes
- professor só desmarca os ausentes

Interpretação:
- `true` = presente
- `false` = falta

Isso substituiu o fluxo anterior mais lento.

---

## 7. Login, cadastro e recuperação

A tela de login foi expandida para trabalhar com três estados:
- login
- criar cadastro
- esqueci a senha

Depois, o fluxo de cadastro público foi melhorado para alunos:
- cria `Usuario`
- cria `Aluno`

Também foi deixado claro que o fluxo automático de recuperação de senha ainda não existe no backend.

---

## 8. Cadastro público de aluno simplificado

Foi simplificado o cadastro do aluno na tela de login.

Campos removidos da tela pública:
- `RA`
- `username`

Nova lógica:
- o e-mail passa a ser o login automaticamente
- o RA é gerado automaticamente no backend

Também foi criado endpoint público para a tela carregar cursos sem autenticação:
- `GET /auth/cursos`

Isso resolveu o problema do select de curso não carregar na área pública.

---

## 9. Controle de acesso por perfil

Foi implementado controle de rotas e abas por perfil.

Perfis tratados:
- `ADMIN`
- `PROFESSOR`
- `ALUNO`

### ADMIN
Tem acesso total ao sistema:
- dashboard
- alunos
- professores
- cursos
- disciplinas
- matrículas
- notas
- frequências
- usuários

### PROFESSOR
Ficou com acesso controlado:
- dashboard
- alunos
- cursos
- disciplinas
- notas
- frequências

Restrições:
- não vê a aba de professores
- não pode cadastrar, editar ou excluir alunos, cursos e disciplinas
- essas telas ficaram em modo leitura

### ALUNO
Passou a ter área própria:
- meus dados
- meu curso
- minhas disciplinas
- minhas notas
- minha frequência

---

## 10. Área específica do aluno

Foram criadas views e endpoints próprios para o aluno autenticado.

### Novas telas do aluno
- `MeusDadosView.vue`
- `MeuCursoView.vue`
- `MinhasDisciplinasView.vue`
- `MinhasNotasView.vue`
- `MinhaFrequenciaView.vue`

### Endpoints criados
- `GET /alunos/me`
- `GET /alunos/me/resumo`
- `GET /alunos/me/disciplinas`
- `GET /alunos/me/curso`

Regra atual de vínculo do login com o aluno:
- RA
- CPF
- e-mail

Foi observado que o ideal futuro é criar um vínculo explícito entre `Usuario` e `Aluno`.

---

## 11. Notas e frequências para professor

Houve um problema em que o professor era redirecionado para login ao acessar algumas áreas.

Causa:
- o frontend fazia logout em qualquer `403`
- algumas telas dependiam de endpoints auxiliares que o professor não podia acessar

Correções feitas:
- o frontend passou a derrubar sessão apenas em `401`
- `403` passou a ser tratado como erro normal
- foi criado endpoint de matrículas por disciplina:
  - `GET /matriculas/disciplina/{idDisciplina}`

Depois disso:
- notas do professor passaram a funcionar por disciplina
- frequência do professor passou a funcionar por disciplina

---

## 12. Cadastro de professor com criação de usuário

Foi identificado que cadastrar professor não criava automaticamente um usuário de acesso.

O backend foi ajustado para:
- criar `Usuario` junto com `Professor`
- usar o e-mail do professor como login
- usar os 6 últimos dígitos do CPF como senha inicial
- atribuir perfil `PROFESSOR`

Também foi adicionada sincronização:
- na atualização do professor
- na exclusão do professor

---

## 13. Primeiro acesso obrigatório para professor

Foi implementado um fluxo de troca obrigatória de senha no primeiro acesso do professor.

### Backend
Foram adicionados:
- campo `senhaTemporaria` em `UsuarioModel`
- DTO de resposta de login com:
  - token
  - perfil
  - indicador de senha temporária
- endpoint:
  - `PUT /auth/primeiro-acesso`

### Frontend
Foi criada:
- `PrimeiroAcessoView.vue`

Comportamento:
- professor loga com senha provisória
- sistema redireciona para tela obrigatória de troca de senha
- só depois da troca ele acessa o restante do sistema

---

## 14. Dashboard redesenhado

O dashboard foi melhorado visualmente.

Elementos adicionados:
- hero principal
- bloco de destaque
- indicadores rápidos
- cards de resumo mais ricos visualmente
- leitura rápida
- composição da base

Também foi reformulado o componente:
- `CardResumo.vue`

Ele passou a aceitar:
- variantes de cor
- badges/ícones
- meta-informação

---

## 15. Restrições do dashboard por perfil

Depois do redesign, foram aplicadas regras específicas para o professor.

Itens mantidos apenas para `ADMIN`:
- base total monitorada
- composição da base
- leitura rápida
- card de professores

Para `PROFESSOR`, o dashboard ficou focado em visão operacional.

---

## 16. Troca de ícones da sidebar e dashboard

Os ícones antigos em formato de sigla foram substituídos.

### Sidebar
As siglas como:
- `DB`
- `AL`
- `PR`

foram trocadas por ícones SVG inline mais consistentes.

### Dashboard
Os badges em texto dos cards também foram trocados por ícones SVG inline.

---

## 17. Evolução da tela de login

A tela de login passou por várias fases.

### Fase 1
Tela funcional com login, cadastro e recuperação.

### Fase 2
Refino visual com aparência mais institucional.

### Fase 3
Remoção de:
- textos de teste
- credenciais pré-preenchidas

### Fase 4
Reconstrução da tela com base na imagem de referência:
- arquivo utilizado: `imagemteladelogin.png`

Foi refeita a tela com:
- card branco centralizado
- fundo azul suave
- campos contornados
- visual mais próximo do design solicitado

### Fase 5
Centralização completa:
- card centralizado horizontalmente
- card centralizado verticalmente
- remoção de textos soltos fora do card
- fundo ocupando 100% da altura da tela

### Fase 6
Integração do logo:
- arquivo utilizado: `logosistema.png`
- o logo foi copiado para `frontend/public/logosistema.png`
- o topo do card passou a usar a imagem real do sistema

### Fase 7
Ajustes de proporção:
- redução do tamanho do card
- redução de tipografia
- redução de campos e espaçamentos
- ajustes específicos na aba de cadastro
- ajustes específicos na aba de recuperação

### Fase 8
Otimização para evitar rolagem:
- login compactado
- cadastro compactado
- recuperação compactada
- formulário de cadastro organizado em 2 colunas no desktop
- retorno para 1 coluna em telas menores

Objetivo atingido:
- não precisar rolar a tela para visualizar o conteúdo do login e da solicitação de acesso

---

## 18. Arquivos importantes alterados no backend

- `backend/src/main/java/br/com/sistemaacademico/config/SecurityConfig.java`
- `backend/src/main/java/br/com/sistemaacademico/config/CorsConfig.java`
- `backend/src/main/java/br/com/sistemaacademico/config/DataInitializer.java`
- `backend/src/main/java/br/com/sistemaacademico/config/JwtAuthenticationFilter.java`
- `backend/src/main/java/br/com/sistemaacademico/controller/AuthController.java`
- `backend/src/main/java/br/com/sistemaacademico/controller/AlunoController.java`
- `backend/src/main/java/br/com/sistemaacademico/controller/MatriculaController.java`
- `backend/src/main/java/br/com/sistemaacademico/dto/LoginResponseDTO.java`
- `backend/src/main/java/br/com/sistemaacademico/dto/PrimeiroAcessoSenhaDTO.java`
- `backend/src/main/java/br/com/sistemaacademico/dto/UsuarioRegistroDTO.java`
- `backend/src/main/java/br/com/sistemaacademico/model/UsuarioModel.java`
- `backend/src/main/java/br/com/sistemaacademico/repository/AlunoRepository.java`
- `backend/src/main/java/br/com/sistemaacademico/repository/MatriculaRepository.java`
- `backend/src/main/java/br/com/sistemaacademico/service/AlunoService.java`
- `backend/src/main/java/br/com/sistemaacademico/service/AuthService.java`
- `backend/src/main/java/br/com/sistemaacademico/service/MatriculaService.java`
- `backend/src/main/java/br/com/sistemaacademico/service/ProfessorService.java`

---

## 19. Arquivos importantes alterados no frontend

- `frontend/src/App.vue`
- `frontend/src/router/index.js`
- `frontend/src/services/api.js`
- `frontend/src/services/authService.js`
- `frontend/src/services/matriculaService.js`
- `frontend/src/components/CardResumo.vue`
- `frontend/src/components/CrudPage.vue`
- `frontend/src/components/Sidebar.vue`
- `frontend/src/views/LoginView.vue`
- `frontend/src/views/DashboardView.vue`
- `frontend/src/views/AlunosView.vue`
- `frontend/src/views/CursosView.vue`
- `frontend/src/views/DisciplinasView.vue`
- `frontend/src/views/FrequenciasView.vue`
- `frontend/src/views/NotasView.vue`
- `frontend/src/views/PrimeiroAcessoView.vue`
- `frontend/src/views/UsuariosView.vue`
- `frontend/src/views/MeusDadosView.vue`
- `frontend/src/views/MeuCursoView.vue`
- `frontend/src/views/MinhasDisciplinasView.vue`
- `frontend/src/views/MinhasNotasView.vue`
- `frontend/src/views/MinhaFrequenciaView.vue`

---

## 20. Assets de referência usados

- `imagemteladelogin.png`
- `logosistema.png`

---

## 21. Fluxos que ficaram implementados

- login com JWT
- cadastro público de aluno
- recuperação orientada de acesso
- primeiro acesso obrigatório para professor
- CRUDs principais
- dashboard redesenhado
- dashboard com restrições por perfil
- sidebar com ícones novos
- cards do dashboard com ícones novos
- área específica do aluno
- notas e frequência por disciplina para professor
- chamada/frequência por aula

---

## 22. Pontos futuros sugeridos

- implementar recuperação real de senha
- criar vínculo explícito entre `Usuario` e `Aluno`
- criar vínculo explícito entre `Usuario` e `Professor`
- aplicar no backend a mesma restrição de dados do dashboard usada no frontend
- adicionar mensagens mais orientadas ao cadastrar professor com senha temporária
- evoluir o dashboard com gráficos reais
- validar a tela de login com screenshots finais em diferentes resoluções

---

## 23. Estado geral do sistema

Ao longo da conversa, o sistema evoluiu de um backend já existente para uma aplicação com:
- frontend completo
- autenticação integrada
- regras de acesso por perfil
- fluxo de primeiro acesso para professores
- área exclusiva para alunos
- dashboard mais maduro
- identidade visual mais consistente
- tela de login redesenhada com base em referência visual

O projeto ficou muito mais próximo de um sistema utilizável em cenário real, tanto funcionalmente quanto visualmente.
