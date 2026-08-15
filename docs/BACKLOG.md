📚 Backlog
# Library Express — Backlog Ágil

Projeto de estudo em Java, evoluído de forma incremental via sprints reais.
PO/Scrum Master: Claude · Dev: Silvério

Para a visão de produto de longo prazo (expansões futuras: marketplace, pagamentos, clube do livro, audiobook), ver `VISION.md`. Este arquivo trata só do que é executável.

**Nota sobre idioma:** este documento está em português (língua de trabalho do planejamento). O `README.md` do repositório está em inglês, por ser a documentação voltada ao público — a divisão é intencional.

**Nota sobre nível de detalhe:** épicos concluídos ficam registrados apenas como resumo (status, pontos, sprint). O detalhamento completo (Gherkin, tasks, desvios de implementação) vive no histórico do Git e nos comentários de encerramento das Issues no GitHub — não é duplicado aqui, para evitar duas fontes de verdade divergentes.

## Épicos

| ID | Épico | Status |
|----|-------|--------|
| E0 | Organização e limpeza inicial | ✅ Concluído (Sprint 0 e 1) |
| E1 | Foundation — desacoplar I/O das Services, centralizar interação na CLI | ✅ Concluído |
| E2 | MVP — Ciclo de vida do empréstimo | ✅ Concluído (Sprint 2) |
| E3 | Inversão de dependência manual + padronização de repositórios + TD01 | ✅ Concluído (Sprint 3) |
| E4 | Fundação de testes automatizados — JUnit 5 + Mockito | ✅ Concluído (Sprint 4), com ressalva — ver TD06 |
| E5 | ~~Containerização (Docker)~~ | ⛔ Descontinuado — escopo absorvido pelo E6 |
| E6 | Persistência Real (JDBC/PostgreSQL) + Containerização Docker | 🔵 Refinado, pronto para execução (Sprint 5) |
| E7 | CI real — testes automatizados rodando no pipeline | ⏳ Backlog (recebe TD06 — testes de infraestrutura com Testcontainers) |
| E8 | CD — Pipeline de entrega + API mínima (Marco 2 — Go Live, com E6, na AWS) | ⏳ Backlog |
| E9 | Evolução arquitetural completa (migração da API mínima pra Spring) | ⏳ Backlog |
| E10 | Reputação do cliente e bloqueio por atraso | ⏳ Backlog (sem prioridade definida) |

E7 e E8 não são a mesma coisa. E8 entrega o build automático + deploy — chamamos de "CD", não "CI/CD", porque sem testes rodando como gate não há integração verificada, só entrega automatizada. E7 é quando isso vira CI de verdade: testes (E4) passam a rodar a cada push, como gate do pipeline, antes do E8 existir. É também em E7 que o débito técnico TD06 (testes de infraestrutura com Testcontainers) será resolvido, aproveitando o JDBC já entregue em E6.

E10 nasceu de uma discussão durante o Sprint 2, sobre o fluxo de devolução: quando um empréstimo está em atraso, o cliente perde pontos de score; após 3 atrasos é "marcado" (conceito ainda a refinar); após 5, é bloqueado por tempo determinado. Não é débito técnico — é escopo novo. Sem multa/dinheiro envolvido (alinhado ao sequenciamento de pagamentos do `VISION.md`). Sem prioridade definida ainda; será refinado em BDD quando entrar na fila, seguindo a regra de "um épico por vez". Questões técnicas já identificadas: (1) como detectar atraso sem scheduler — provavelmente cálculo lazy na devolução/validação, não job em background; (2) `Customer` vai precisar de campos novos (score, contagem de atrasos, `blockedUntil`).

## Roadmap — Fases e Marcos

### 🌱 Fase 1 — Foundation

Objetivo: construir uma base sólida, consolidando o domínio e eliminando problemas arquiteturais antes de introduzir novas tecnologias.

Escopo: E0, E1, E2, E3.

### 🚀 Marco 1 — MVP ✅ Alcançado

O sistema atende aos requisitos funcionais essenciais de uma biblioteca, via CLI. Fecha com E2. Tag `v0.1.1`.

### 🏗 Fase 2 — Software Maturity

Objetivo: aumentar qualidade e confiabilidade do sistema, guiado pelas necessidades reais do projeto — agora também calibrado para gerar valor de portfólio em processos seletivos internacionais (EUA/Canadá).

Sequência de temas:

1. Fundação de testes automatizados — JUnit 5 + Mockito (E4) ✅ concluído
2. Persistência real (JDBC/PostgreSQL) + Containerização Docker (E6) 🔵 atual
3. CI real — testes como gate do pipeline, incluindo testes de infraestrutura via Testcontainers/TD06 (E7)
4. Marco 2 — Go Live (E8, empacotando CD + API mínima, já com Docker e JDBC prontos)
5. Evolução arquitetural — migração pra Spring (E9)

**Fusão E5 + E6 (decisão registrada):** o Épico E5 (Docker isolado) foi descontinuado como bloco próprio. Justificativa: containerização só gera valor de negócio real quando integrada à persistência de verdade — "containerizar um CLI com repositório em memória" é fraco como narrativa de portfólio comparado a "containerizar uma aplicação com PostgreSQL real, HikariCP e migrations versionadas". O E5 permanece visível na tabela de Épicos (não removido do mapa), marcado como descontinuado, para preservar rastreabilidade. Todo o escopo de containerização foi absorvido pelo E6, que assume o nome **Persistência Real (JDBC/PostgreSQL) + Containerização Docker**.

Banco definido: **PostgreSQL** (via JDBC puro, sem ORM), alinhado ao RDS free tier da AWS.

### 🚀 Marco 2 — Go Live

Primeira subida real pra produção — na AWS (free tier: ECS/Fargate ou Elastic Beanstalk com Docker; substituiu o plano original de Heroku, que tem pouca relevância no mercado que estamos mirando). Fecha junto: pipeline de CD (E8), persistência real via JDBC + imagem Docker (E6, escopo fundido) e API mínima sem framework (`com.sun.net.httpserver.HttpServer`, sem Spring ainda — evita reescrever esforço quando a migração acontecer em E9). O deploy só "vale" quando há um serviço HTTP de verdade recebendo tráfego, com dado persistido de verdade.

Por que Go Live vem depois de testes/Docker/persistência/CI? A sequência conta uma narrativa forte de portfólio: testei → containerizei → persisti → automatizei → só então fui pra produção — como equipes reais operam.

Por que testes e persistência crus antes do Spring? `@SpringBootTest`/Mockito e Spring Data JPA são abstrações sobre JUnit puro e JDBC puro. Fazer o caminho manual primeiro é deliberado: força entender o mecanismo por baixo antes da conveniência do framework escondê-lo. Refazer com Spring depois em E9 não é retrabalho desperdiçado — é o próprio exercício que revela o que a abstração compra.

### ⚙️ Fase 3 — Professional Software Engineering

Objetivo: aprofundar práticas de engenharia em um sistema que já está em produção desde o Marco 2 — segurança, observabilidade, performance, escalabilidade, documentação.

Escopo: ainda sem épicos formais (backlog futuro).

## Princípios

- O domínio é sempre a prioridade.
- Novas tecnologias são introduzidas apenas quando resolvem problemas reais.
- Cada Sprint deve gerar uma entrega funcional.
- A arquitetura evolui junto com o sistema.
- O aprendizado acontece através da prática.

**Regra de trabalho:** um épico por vez, refinado em detalhe (BDD + tasks) apenas quando entra em andamento. Épicos futuros ficam só com título até chegar a vez deles (backlog grooming just-in-time).

**Regra de processo (a partir do E4):** antes de gerar qualquer artefato formal de backlog (quebra de épico, User Story, tasks) para uma nova decisão de implementação ou mudança de arquitetura, o alinhamento com o Dev deve ser debatido e fechado em conversa primeiro. A geração de Markdown formal (pontos, Gherkin, tasks, commits) só acontece depois do alinhamento — nunca antes. Evita retrabalho por scope drift descoberto depois do fato.

## Débito Técnico

| ID | Descrição | Pontos | Status |
|----|-----------|--------|--------|
| TD01 | Contrato `equals`/`hashCode` de Book, Customer e Loan | 3 | ✅ Resolvido (US-304, E3) |
| TD05 | Empacotamento em Fat JAR (`maven-shade-plugin`, manifest com `Main-Class`) — congelado desde o E3, com resolução originalmente prevista só para o E8. **Decisão revisada:** a necessidade de Docker antecipa a razão de produção para um artefato único executável — congelar até E8 deixou de fazer sentido. Destravado e realocado para o E6. | 3 | 🔵 Destravado — resolução em US-503 (E6) |
| TD06 | Camada de infraestrutura (repositórios in-memory) sem cobertura de teste automatizada desde o fechamento do E4. Adiamento intencional: testes de contrato e concorrência (originalmente US-404) serão reescritos usando Testcontainers com banco real (Postgres), após o E6 (JDBC) entregar a implementação definitiva — evita esforço duplicado em uma implementação in-memory que será substituída. Resolução alocada no E7. | — (a estimar no refinamento do E7) | 🟡 Aceito, aguardando E6 |

## 🔵 Épico E6 — Persistência Real (JDBC/PostgreSQL) + Containerização Docker

**Sprint:** 5
**Pontos totais:** 18 (2 + 8 + 3 + 5)
**Status:** 🔵 Refinado, pronto para execução

### Decisões registradas nesta fusão
- **E5 descontinuado como bloco isolado**, escopo absorvido integralmente pelo E6. Linha mantida na tabela de Épicos com status `Descontinuado — escopo absorvido pelo E6`, para preservar rastreabilidade histórica.
- **TD05 destravado** e realocado para este épico (US-503). Justificativa atualizada: o congelamento original (até E8) partia da premissa de que só haveria razão de produção para empacotamento em artefato único no Go Live. A necessidade de containerizar via Docker antecipa essa razão.
- **Docker Compose incluído no escopo**, tanto para ambiente de desenvolvimento local (Postgres solto, US-501) quanto para orquestração da aplicação completa no estágio final (US-504).
- **Ordem de execução interna:** C (persistência JDBC) → A (Fat JAR) → B (Docker multi-stage) — a aplicação funciona com banco real localmente antes de qualquer esforço de empacotamento/containerização.
- **Escopo de rede confirmado:** a aplicação continua rodando como **CLI batch/interativa** dentro do container, sem servidor HTTP — a API mínima fica reservada para o E8, mesmo sendo tecnicamente possível antecipá-la aqui.
- **Versão do PostgreSQL:** `postgres:17-alpine`, acompanhando a versão estável mais recente disponível.

### Objetivo do épico
Migrar o armazenamento em memória para PostgreSQL real via JDBC puro, com pool de conexões gerenciado (HikariCP) e versionamento de schema (Flyway), entregando o ambiente 100% replicável via Docker — aplicação e banco.

### Valor de negócio
Sem persistência real, o sistema não sobrevive a um restart e não pode ir para produção (Marco 2 depende disso). Sem containerização, o ambiente não é replicável entre máquinas nem implantável na AWS. A combinação PostgreSQL + JDBC puro + HikariCP + Flyway + Docker multi-stage é o conjunto de competências mais cobrado em avaliação técnica de backend Java sênior no mercado internacional.

### Definition of Done do Épico E6
- [ ] `docker-compose.dev.yml` sobe Postgres local, pronto para os repositórios JDBC (US-501)
- [ ] Flyway aplica migrations versionadas e HikariCP gerencia o pool de conexões na inicialização (US-502)
- [ ] `BookDbRepository`, `LoanDbRepository`, `CustomerDbRepository` implementam as interfaces de domínio via SQL puro, substituindo as implementações in-memory na Composition Root (US-502)
- [ ] Fat JAR único e executável gerado via `maven-shade-plugin`, resolvendo TD05 (US-503)
- [ ] Dockerfile multi-stage builda o Fat JAR e roda em imagem `eclipse-temurin:21-jre-alpine` (US-504)
- [ ] `docker-compose.yml` de produção/integração sobe aplicação (CLI batch) + Postgres juntos, com a aplicação conectando ao banco via variáveis de ambiente (US-504)
- [ ] Todas as 4 US em status Done

---

### US-501 — Ambiente de desenvolvimento local com Docker Compose (Postgres)

**Pontos:** 2
**Depende de:** —

**Story:** Como desenvolvedor, preciso de um `docker-compose.dev.yml` que suba um PostgreSQL local isolado, para poder implementar e testar manualmente os repositórios JDBC sem depender de instalação local do banco.

**Cenários (BDD):**

```gherkin
Scenario: Postgres sobe via Docker Compose de desenvolvimento
  Given o arquivo docker-compose.dev.yml na raiz do projeto
  When o comando "docker compose -f docker-compose.dev.yml up -d" e executado
  Then um container Postgres 17-alpine deve subir na porta configurada
  And o banco deve aceitar conexoes com as credenciais definidas no compose

Scenario: Dados do Postgres de desenvolvimento persistem entre restarts
  Given o container Postgres de desenvolvimento em execucao com dados gravados
  When o container e reiniciado (docker compose restart)
  Then os dados gravados anteriormente devem continuar disponiveis
```

**Tasks:**
- Criar `docker-compose.dev.yml` com serviço `postgres:17-alpine`
- Configurar volume nomeado para persistência de dados entre restarts
- Configurar variáveis de ambiente via `.env` local (`.env.example` versionado, `.env` no `.gitignore`)
- Documentar no README a seção "Local development"

**Commits:**
```
build(docker): US-501 cria docker-compose de desenvolvimento com postgres 17-alpine
docs(readme): US-501 documenta subida do ambiente de desenvolvimento local
```

---

### US-502 — Persistência JDBC com HikariCP e Flyway

**Pontos:** 8
**Depende de:** US-501

**Story:** Como desenvolvedor, preciso substituir os repositórios in-memory por implementações reais em PostgreSQL, usando JDBC puro, HikariCP para pool de conexões e Flyway para versionar o schema — plugando tudo nas interfaces de repositório já existentes no domínio, sem alterar contratos.

**Cenários (BDD):**

```gherkin
Scenario: Flyway aplica migrations na inicializacao da aplicacao
  Given scripts de migration em src/main/resources/db/migration (V1__..., V2__...)
  When a aplicacao inicializa
  Then o Flyway deve aplicar as migrations pendentes automaticamente
  And o schema resultante deve refletir as tabelas book, customer e loan

Scenario: HikariCP gerencia o pool de conexoes
  Given o HikariDataSource configurado no arranque da aplicacao
  When multiplas operacoes de repositorio ocorrem em sequencia
  Then as conexoes devem ser reaproveitadas do pool, sem esgotamento sob carga normal

Scenario: BookDbRepository persiste e recupera livro corretamente
  Given o banco Postgres com schema aplicado
  When um Book e salvo via BookDbRepository.create()
  Then getByIsbn() deve retornar o mesmo livro com todos os atributos integros

Scenario: Constraint UNIQUE de email e respeitada pelo banco real
  Given um Customer ja persistido com um email
  When um segundo Customer e criado com o mesmo email
  Then o banco deve rejeitar a operacao via constraint UNIQUE
  And a excecao deve ser traduzida para excecao de dominio, nao vazar SQLException

Scenario: LoanDbRepository.update() aplica mudanca de status via id
  Given um Loan ja persistido no banco
  When update() e chamado com o mesmo id e novo status
  Then o registro persistido deve refletir o novo status
  And nenhuma outra linha da tabela deve ser afetada

Scenario: search() de Loan filtra corretamente via query SQL combinada
  Given multiplos Loans persistidos com combinacoes distintas de customerId, ISBN e status
  When search() e chamado com um subconjunto desses criterios
  Then apenas os Loans que atendem a TODOS os criterios informados devem retornar

Scenario: Composition Root troca repositorios in-memory por JDBC sem alterar usecases
  Given o AppContext configurado para usar as implementacoes JDBC
  When qualquer usecase de Book, Customer ou Loan e executado
  Then o comportamento observavel deve ser identico ao das implementacoes in-memory
```

**Tasks:**
- Adicionar dependências `org.postgresql:postgresql`, `com.zaxxer:HikariCP`, `org.flywaydb:flyway-core` (+ `flyway-database-postgresql`) ao módulo `infrastructure`
- Criar scripts de migration Flyway (`V1__create_book_table.sql`, `V2__create_customer_table.sql`, `V3__create_loan_table.sql`), incluindo constraint `UNIQUE` em `customer.email`
- Configurar `HikariDataSource` no arranque da aplicação (pool size e timeout conservadores para free tier)
- Implementar `BookDbRepository`, `CustomerDbRepository`, `LoanDbRepository` no módulo `infrastructure`
- Traduzir exceções de SQL (ex: violação de constraint) para exceções de domínio já existentes
- Atualizar `AppContext` (Composition Root) para injetar as implementações JDBC no lugar das in-memory
- Manter as implementações in-memory (uso futuro em testes), sem uso em produção
- Atualizar README com variáveis de ambiente de conexão

**Commits:**
```
build(pom): US-502 adiciona postgresql, hikaricp e flyway ao modulo infrastructure
build(flyway): US-502 cria migrations iniciais de book customer e loan
feat(datasource): US-502 configura hikaricp no arranque da aplicacao
feat(book-repository): US-502 implementa bookdbrepository via jdbc puro
feat(customer-repository): US-502 implementa customerdbrepository via jdbc puro
feat(loan-repository): US-502 implementa loandbrepository via jdbc puro
fix(repositories): US-502 traduz sqlexception para excecoes de dominio
refactor(composition-root): US-502 troca repositorios in-memory por jdbc no appcontext
docs(readme): US-502 documenta variaveis de ambiente de conexao com o banco
```

---

### US-503 — Empacotamento em Fat JAR (destrava TD05)

**Pontos:** 3
**Depende de:** US-502

**Story:** Como desenvolvedor, preciso de um artefato único e executável (Fat JAR) consolidando os módulos `domain`, `application` e `infrastructure`, para que a aplicação rode fora da IDE e possa ser empacotada em uma imagem Docker.

**Cenários (BDD):**

```gherkin
Scenario: Fat JAR e gerado com sucesso via Maven
  Given o maven-shade-plugin configurado no pom.xml do modulo infrastructure
  When o comando "mvn clean package" e executado na raiz
  Then um jar unico executavel deve ser gerado em infrastructure/target/

Scenario: Fat JAR executa a aplicacao standalone
  Given o fat jar gerado pelo build
  When o comando "java -jar library-express.jar" e executado
  Then a aplicacao deve iniciar corretamente, aplicando migrations e conectando ao banco
  And nenhum erro de ClassNotFoundException ou NoClassDefFoundError deve ocorrer

Scenario: Manifest aponta para a classe principal correta
  Given o fat jar gerado
  When o manifesto MANIFEST.MF e inspecionado
  Then o atributo Main-Class deve apontar para a classe Application
```

**Tasks:**
- Configurar `maven-shade-plugin` no `infrastructure/pom.xml`, consolidando classes dos módulos irmãos e dependências externas
- Configurar `Main-Class` no manifest
- Resolver conflitos de merge de recursos (ex: `META-INF/services`) via `ServicesResourceTransformer`, caso ocorram
- Atualizar o registro do TD05: status "Destravado" → "Resolvido — US-503 (E6)"
- Documentar comando de build e execução via `java -jar` no README

**Commits:**
```
build(shade): US-503 configura maven-shade-plugin no modulo infrastructure
build(manifest): US-503 aponta main-class para a classe application
docs(td05): US-503 resolve TD05 registrando decisao atualizada no backlog
docs(readme): US-503 documenta build e execucao via fat jar
```

---

### US-504 — Containerização via Docker Multi-stage + Compose de Aplicação

**Pontos:** 5
**Depende de:** US-503

**Story:** Como desenvolvedor, preciso de uma imagem Docker leve, construída em múltiplos estágios, e de um `docker-compose.yml` que suba a aplicação (CLI batch) e o PostgreSQL juntos, para entregar o ambiente 100% replicável necessário ao Marco 2.

**Cenários (BDD):**

```gherkin
Scenario: Imagem Docker builda a aplicacao em estagio separado
  Given o Dockerfile multi-stage na raiz do projeto
  When "docker build" e executado
  Then o estagio de build deve compilar os submodulos e gerar o fat jar
  And o estagio de runtime deve conter apenas o jre e o jar, sem ferramentas de build

Scenario: Imagem final e leve e nao contem Maven nem JDK completo
  Given a imagem final gerada pelo multi-stage build
  When o tamanho e conteudo da imagem sao inspecionados
  Then a imagem deve ser baseada em eclipse-temurin:21-jre-alpine
  And nao deve conter o Maven nem o JDK completo do estagio de build

Scenario: Aplicacao conecta ao Postgres via variaveis de ambiente no container
  Given a aplicacao rodando em container com variaveis de ambiente de conexao configuradas
  When o container inicializa
  Then a aplicacao deve conectar ao Postgres apontado pelas variaveis, sem valores hardcoded

Scenario: Docker Compose sobe aplicacao e banco juntos
  Given o docker-compose.yml na raiz do projeto com os servicos app e postgres
  When "docker compose up" e executado
  Then ambos os containers devem subir
  And a aplicacao deve aguardar o banco estar saudavel antes de conectar (healthcheck/depends_on)
  And a aplicacao deve executar corretamente como CLI batch apos a subida completa
```

**Tasks:**
- Criar `Dockerfile` multi-stage: estágio de build (Maven + Java 21) e estágio de runtime (`eclipse-temurin:21-jre-alpine`)
- Parametrizar conexão com banco via variáveis de ambiente
- Criar `docker-compose.yml` (produção/integração, distinto do `docker-compose.dev.yml` da US-501) com serviços `app` e `postgres:17-alpine`
- Configurar `healthcheck` no serviço Postgres e `depends_on` com condição de saúde no serviço `app`
- Validar tamanho final da imagem e ausência de ferramentas de build no runtime
- Documentar seção "Running with Docker" no README

**Commits:**
```
build(docker): US-504 cria dockerfile multi-stage para build e runtime
build(docker): US-504 cria docker-compose de aplicacao com app e postgres 17-alpine
feat(config): US-504 parametriza conexao com banco via variaveis de ambiente
docs(readme): US-504 documenta build e execucao via docker compose
```

## Histórico de épicos concluídos

### E0 — Organização e limpeza inicial

✅ Concluído · Iteration 1 (Jul 07–Jul 20)

### E1 — Foundation

✅ Concluído

Objetivo: preparar a base da aplicação para suportar novas interfaces sem alterar as regras de negócio — Services desacopladas de I/O, interação centralizada na CLI.

### E2 — MVP: Ciclo de vida do empréstimo

✅ Concluído · Sprint 2 · 17 pontos (US-201 a US-207)

Entregou o fluxo completo via CLI: cadastro de cliente/livro, empréstimo, devolução, respeitando regras de negócio (disponibilidade, limite de empréstimos ativos). Marco 1 (MVP) alcançado. Tag `v0.1.1`.

Detalhe completo (Gherkin, tasks, desvios de implementação): ver Issues #13–#18 no GitHub Projects.

### E3 — Inversão de dependência manual + padronização de repositórios

✅ Concluído · Sprint 3 · 19 pontos

| US | Descrição | Pontos | Status |
|----|-----------|--------|--------|
| US-301 | Injetar `InMemoryBookRepository` via construtor nos usecases de Book | 3 | ✅ Done |
| US-302 | Injetar `InMemoryLoanRepository` via construtor nos usecases de Loan | 3 | ✅ Done |
| US-303 | Composition Root para montagem manual dos usecases | 5 | ✅ Done |
| US-304 | Corrigir contrato `equals`/`hashCode` de Book, Customer e Loan (TD01) | 3 | ✅ Done |
| US-305 | Padronizar nomenclatura de repositórios (remover prefixo `I`), converter enum→classe, reorganizar pastas para JDBC | 5 | ✅ Done |

Detalhe completo: ver Issues correspondentes no GitHub Projects.

### E4 — Fundação de Testes Automatizados (JUnit 5 + Mockito)

✅ Concluído · Sprint 4 · **15 pontos** (2 + 5 + 8) — revisado de 20 pts após realocação de escopo

| US | Descrição | Pontos | Status |
|----|-----------|--------|--------|
| US-401 | Configuração do ambiente de testes: JUnit 5, Mockito, coverage-report (JaCoCo aggregate) | 2 | ✅ Done |
| US-402 | Testes da camada Domain: Value Objects e entidades, zero dependência de mock/infra | 5 | ✅ Done |
| US-403 | Testes de camada Application via Mockito: usecases de Book/Loan e validators, substituindo integralmente os Fakes manuais | 8 | ✅ Done |

**Mudança de escopo registrada:**
- Fakes manuais (`FakeBookRepository`, `FakeLoanRepository`, `FakeCustomerRepository`) substituídos por Mockito nativo (`@Mock` + `@InjectMocks`). Justificativa técnica em ADR (task da US-401).
- US-404 (Testes de camada Infrastructure, 5 pts originais) **removida do E4 e realocada para o E7**, para execução com Testcontainers + banco real, alinhada à chegada do JDBC (E6) — ver TD06.

Detalhe completo (Gherkin, tasks, commits): ver Issues correspondentes no GitHub Projects.

## Convenção de commits

Segue Conventional Commits, commits de linha única (sem corpo/rodapé — fluxo via terminal), com o ID da US logo após os dois-pontos:

```
<tipo>(<escopo>): <ID> <descrição no imperativo, minúsculo, sem ponto final>
```

Sem fluxo de Pull Request ainda (ver E8 no roadmap), o commit vai direto pra `develop` — não há auto-close de Issue. Ao concluir uma US, feche a Issue manualmente no board.

Múltiplos commits na mesma US: todos repetem o mesmo ID (`US-XXX`) no início da descrição.

## Convenção de versionamento

Segue SemVer (`MAJOR.MINOR.PATCH`):

- `0.y.z` enquanto o projeto está em desenvolvimento inicial — contratos internos (arquitetura, persistência, framework) ainda podem mudar sem aviso. `1.0.0` fica reservado pra quando o sistema estabilizar (por volta da Fase 3).
- Sufixos `alpha`/`beta`/`rc` só fazem sentido a partir do Marco 2 (quando a API REST existir).
- `SNAPSHOT` no `pom.xml` durante desenvolvimento contínuo; a tag/release usa a versão limpa.

**Tags:**

| Tag | Marco | Data |
|-----|-------|------|
| v0.1.1 | Marco 1 — MVP (Épico E2 concluído) | ver histórico do Git |

## Convenções do board

- **Pontos:** escala Fibonacci simplificada (1, 2, 3, 5, 8)
- **Status:** 🔲 To Do · 🟡 In Progress · 🔵 In Review · ✅ Done
- **Numeração de história:** `US-{sprint}{sequencial}` (ex: US-401 = Sprint 4, item 1)
- **Numeração de débito técnico:** `TD-{sequencial}`, sem vínculo fixo a sprint até ser priorizado
- **Cenários BDD:** formato Gherkin (Given/When/Then), usados como critério de aceite formal de cada história

---

**Última atualização:** Épico E6 (fusão E5+E6 — Persistência Real JDBC/PostgreSQL + Containerização Docker) refinado e pronto para execução: 18 pts (2+8+3+5), Sprint 5, US-501 a US-504. E5 descontinuado como bloco isolado, escopo absorvido pelo E6 — linha mantida na tabela de Épicos para rastreabilidade. TD05 (Fat JAR) destravado e realocado para US-503, revertendo o congelamento até E8 registrado no E3. Postgres fixado em `17-alpine`; aplicação permanece CLI batch neste épico (servidor HTTP fica reservado para o E8). Épico E4 concluído (15 pts — 2+5+8; US-401 a US-403), histórico completo movido para as Issues do GitHub Projects.