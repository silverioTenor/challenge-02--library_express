# Library Express — Backlog Ágil

> Projeto de estudo em Java, evoluído de forma incremental via sprints reais.
> PO/Scrum Master: Claude · Dev: Silvério
>
> Para a visão de produto de longo prazo (expansões futuras: marketplace, pagamentos, clube do livro, audiobook), ver `VISION.md`. Este arquivo trata só do que é executável.
>
> **Nota sobre idioma:** este documento está em português (língua de trabalho do planejamento). O `README.md` do repositório está em inglês, por ser a documentação voltada ao público — a divisão é intencional.
>
> **Nota sobre nível de detalhe:** épicos concluídos ficam registrados apenas como resumo (status, pontos, sprint). O detalhamento completo (Gherkin, tasks, desvios de implementação) vive no histórico do Git e nos comentários de encerramento das Issues no GitHub — não é duplicado aqui, para evitar duas fontes de verdade divergentes.

---

## Épicos

| ID | Épico | Status |
| --- | --- | --- |
| E0 | Organização e limpeza inicial | ✅ Concluído (Sprint 0 e 1) |
| E1 | Foundation — desacoplar I/O das Services, centralizar interação na CLI | ✅ Concluído |
| E2 | MVP — Ciclo de vida do empréstimo | ✅ Concluído (Sprint 2) |
| E3 | Inversão de dependência manual + padronização de repositórios + TD-01 | ✅ Concluído (Sprint 3) |
| E4 | Fundação de testes automatizados — JUnit 5 puro | 🟡 Em andamento (Sprint 4) |
| E5 | Containerização (Docker) | ⏳ Backlog |
| E6 | Persistência real (JDBC/JPA) | ⏳ Backlog |
| E7 | CI real — testes automatizados rodando no pipeline | ⏳ Backlog |
| E8 | CD — Pipeline de entrega + API mínima (**Marco 2 — Go Live**, com E5 + E6, na AWS) | ⏳ Backlog |
| E9 | Evolução arquitetural completa (migração da API mínima pra Spring) | ⏳ Backlog |
| E10 | Reputação do cliente e bloqueio por atraso | ⏳ Backlog (sem prioridade definida) |

> **E7 e E8 não são a mesma coisa.** E8 entrega o build automático + deploy — chamamos de "CD", não "CI/CD", porque sem testes rodando como gate não há integração *verificada*, só entrega automatizada. E7 é quando isso vira CI de verdade: testes (E4) passam a rodar a cada push, como gate do pipeline, antes do E8 existir.

> **E10** nasceu de uma discussão durante o Sprint 2, sobre o fluxo de devolução: quando um empréstimo está em atraso, o cliente perde pontos de score; após 3 atrasos é "marcado" (conceito ainda a refinar); após 5, é bloqueado por tempo determinado. Não é débito técnico — é escopo novo. Sem multa/dinheiro envolvido (alinhado ao sequenciamento de pagamentos do `VISION.md`). Sem prioridade definida ainda; será refinado em BDD quando entrar na fila, seguindo a regra de "um épico por vez". Questões técnicas já identificadas: (1) como detectar atraso sem scheduler — provavelmente cálculo *lazy* na devolução/validação, não job em background; (2) `Customer` vai precisar de campos novos (score, contagem de atrasos, `blockedUntil`).

---

| ID | Item | Descrição | Pontos | Status |
|---|---|---|---|---|
| TD04 | Registro retroativo da modularização Maven | Estrutura multi-módulo (domain/application/infrastructure) já implementada direto em `develop`; ADR e C4 criados retroativamente, BACKLOG realinhado aos novos caminhos de teste do E4 | 2 | ✅ Resolvido |
| TD05 | Empacotamento executável (fat jar) pendente | `mvn package` não gera jar autocontido — falta `Main-Class` no manifest e shade/assembly plugin para embutir dependências; necessário antes do E8 (Go Live) | 3 | 🔲 To Do (vinculado a E8) |

---

## Roadmap — Fases e Marcos

### 🌱 Fase 1 — Foundation

**Objetivo:** construir uma base sólida, consolidando o domínio e eliminando problemas arquiteturais antes de introduzir novas tecnologias.
**Escopo:** E0, E1, E2, E3.

### 🚀 Marco 1 — MVP ✅ Alcançado

O sistema atende aos requisitos funcionais essenciais de uma biblioteca, via CLI. Fecha com E2. Tag `v0.1.1`.

### 🏗️ Fase 2 — Software Maturity

**Objetivo:** aumentar qualidade e confiabilidade do sistema, guiado pelas necessidades reais do projeto — agora também calibrado para gerar valor de portfólio em processos seletivos internacionais (EUA/Canadá).

**Sequência de temas:**

1. Fundação de testes automatizados — JUnit puro (E4) 🟡 *atual*
2. Containerização — Docker (E5)
3. Persistência real — JDBC puro (E6)
4. CI real — testes como gate do pipeline (E7)
5. **Marco 2 — Go Live** (E8, empacotando CD + API mínima, já com Docker e JDBC prontos)
6. Evolução arquitetural — migração pra Spring (E9)

> **Mudança de escopo do Marco 2:** com a Persistência real (E6) antes do Go Live, o sistema sobe pra produção **já com banco real**, não mais com repositórios em memória como estava planejado antes. Isso adia uma decisão que precisa ser tomada quando o E6 entrar em detalhamento: qual banco usar no free tier da AWS (RDS free tier vs. algo mais leve embarcado).

### 🚀 Marco 2 — Go Live

Primeira subida real pra produção — na **AWS** (free tier: ECS/Fargate ou Elastic Beanstalk com Docker; substituiu o plano original de Heroku, que tem pouca relevância no mercado que estamos mirando). Fecha **junto**: pipeline de CD (E8), imagem Docker (E5), persistência real via JDBC (E6) e API mínima sem framework (`com.sun.net.httpserver.HttpServer`, sem Spring ainda — evita reescrever esforço quando a migração acontecer em E9). O deploy só "vale" quando há um serviço HTTP de verdade recebendo tráfego, com dado persistido de verdade.

> **Por que Go Live vem depois de testes/Docker/persistência/CI?** A sequência conta uma narrativa forte de portfólio: testei → containerizei → persisti → automatizei → só então fui pra produção — como equipes reais operam.

> **Por que testes e persistência crus antes do Spring?** `@SpringBootTest`/Mockito e Spring Data JPA são abstrações sobre JUnit puro e JDBC puro. Fazer o caminho manual primeiro é deliberado: força entender o mecanismo por baixo antes da conveniência do framework escondê-lo. Refazer com Spring depois em E9 não é retrabalho desperdiçado — é o próprio exercício que revela o que a abstração compra.

### ⚙️ Fase 3 — Professional Software Engineering

**Objetivo:** aprofundar práticas de engenharia em um sistema que já está em produção desde o Marco 2 — segurança, observabilidade, performance, escalabilidade, documentação.
**Escopo:** ainda sem épicos formais (backlog futuro).

### Princípios

- O domínio é sempre a prioridade.
- Novas tecnologias são introduzidas apenas quando resolvem problemas reais.
- Cada Sprint deve gerar uma entrega funcional.
- A arquitetura evolui junto com o sistema.
- O aprendizado acontece através da prática.

**Regra de trabalho:** um épico por vez, refinado em detalhe (BDD + tasks) apenas quando entra em andamento. Épicos futuros ficam só com título até chegar a vez deles (backlog grooming just-in-time).

---

# Histórico de épicos concluídos

## E0 — Organização e limpeza inicial
✅ Concluído · Iteration 1 (Jul 07–Jul 20)

## E1 — Foundation
✅ Concluído
Objetivo: preparar a base da aplicação para suportar novas interfaces sem alterar as regras de negócio — Services desacopladas de I/O, interação centralizada na CLI.

## E2 — MVP: Ciclo de vida do empréstimo
✅ Concluído · Sprint 2 · **17 pontos** (US-201 a US-207)
Entregou o fluxo completo via CLI: cadastro de cliente/livro, empréstimo, devolução, respeitando regras de negócio (disponibilidade, limite de empréstimos ativos). Marco 1 (MVP) alcançado. Tag `v0.1.1`.
Detalhe completo (Gherkin, tasks, desvios de implementação): ver Issues #13–#18 no GitHub Projects.

## E3 — Inversão de dependência manual + padronização de repositórios
✅ Concluído · Sprint 3 · **19 pontos**

| US | Descrição | Pontos | Status |
| --- | --- | --- | --- |
| US-301 | Injetar `InMemoryBookRepository` via construtor nos usecases de Book | 3 | ✅ Done |
| US-302 | Injetar `InMemoryLoanRepository` via construtor nos usecases de Loan | 3 | ✅ Done |
| US-303 | Composition Root para montagem manual dos usecases | 5 | ✅ Done |
| US-304 | Corrigir contrato `equals`/`hashCode` de Book, Customer e Loan (TD-01) | 3 | ✅ Done |
| US-305 | Padronizar nomenclatura de repositórios (remover prefixo `I`), converter enum→classe, reorganizar pastas para JDBC | 5 | ✅ Done |

Detalhe completo: ver Issues correspondentes no GitHub Projects.

---

# 🔵 Épico E4 — Fundação de Testes Automatizados (JUnit 5)

### Objetivo do épico

Introduzir JUnit 5 no projeto e cobrir com testes automatizados as camadas de domínio (entidades) e aplicação (usecases, validators), aproveitando a DI manual entregue no E3 para isolar dependências com test doubles. Estabelecer a convenção de testes que vai servir de base para o gate de CI do E7.

### Valor de negócio

Hoje toda validação é manual via terminal — não escala, não é repetível, e não protege contra regressão silenciosa. Sem essa fundação, o E7 (CI) não tem o que rodar como gate, e a suíte de portfólio não tem prova de qualidade nenhuma pra mostrar em processo seletivo internacional.

### Sprint

Sprint 4 · Capacidade estimada: ~20h/semana · Total: **18 pontos**

### Ordem de dependência

```
US-401 (setup JUnit 5 + convenção) ── bloqueia tudo abaixo
US-402 (testes de domínio: Book, Customer, Loan) ── independente após US-401
US-406 (test doubles / fakes de repository) ──┐
                                                 ├──> US-403 (testes usecases Book)
                                                 ├──> US-404 (testes usecases Loan)
US-405 (testes de validators) ── depende de US-401, independente de US-406
```

### Backlog do épico

| US | Descrição | Pontos | Status |
| --- | --- | --- | --- |
| US-401 | Configurar JUnit 5 e convenção de testes | 2 | 🔲 To Do |
| US-402 | Testes unitários das entidades de domínio (Book, Customer, Loan) | 3 | 🔲 To Do |
| US-403 | Testes unitários dos usecases de Book | 3 | 🔲 To Do |
| US-404 | Testes unitários dos usecases de Loan | 5 | 🔲 To Do |
| US-405 | Testes unitários dos validators | 2 | 🔲 To Do |
| US-406 | Test doubles (fakes) para repositórios | 3 | 🔲 To Do |

## US-401 — Configurar JUnit 5 e convenção de testes

**Story:** Como *desenvolvedor*, preciso da infraestrutura de testes configurada no projeto, para que toda US subsequente do épico tenha onde e como escrever testes de forma consistente.

### Cenários (BDD)

```gherkin
Scenario: Dependência JUnit 5 disponível no build
  Given o pom.xml não possui dependência de testes
  When a dependência junit-jupiter é adicionada com escopo test
  Then o comando mvn test deve executar sem erros de configuração

Scenario: Estrutura de pastas espelha o código de produção
  Given as classes de produção residem em src/main/java
  When a estrutura de testes é criada
  Then as classes de teste devem residir em src/test/java, no mesmo pacote da classe testada

Scenario: Teste de exemplo valida o pipeline de execução
  Given a configuração do JUnit 5 foi concluída
  When um teste trivial (ex: assertTrue(true)) é executado via mvn test
  Then o resultado deve reportar 1 teste executado com sucesso
```

### Tasks

- [ ] Adicionar `junit-jupiter` (escopo `test`) e `maven-surefire-plugin` ao `pom.xml`
- [ ] Confirmar Java 21 + Maven reconhecem `src/test/java` sem configuração adicional
- [ ] Documentar convenção de nomenclatura: `NomeDaClasseTest`, métodos em inglês (alinhado ao domínio, que já é em inglês)
- [ ] Escrever teste trivial de smoke-test pra validar o pipeline (`mvn test` verde)
- [ ] Atualizar `README.md` com seção "Running tests" (`mvn test`)

### Commits

```
build(pom): US-401 adiciona dependencia junit-jupiter e surefire
test(smoke): US-401 adiciona teste trivial para validar pipeline de execucao
docs(readme): US-401 documenta comando de execucao de testes
```

---

## US-402 — Testes unitários das entidades de domínio

**Story:** Como *desenvolvedor*, preciso de testes automatizados para `Book`, `Customer` e `Loan`, para garantir que as regras já embutidas nas entidades (contrato equals/hashCode, `isAvailable()`, `isOverdue()`) continuem corretas conforme o código evolui.

**Depende de:** US-401

### Cenários (BDD)

```gherkin
Scenario: Book — contrato equals/hashCode por ISBN
  Given dois Book com mesmo ISBN e atributos diferentes
  When comparados via equals() e hashCode()
  Then devem ser considerados iguais e ter hashCode idêntico

Scenario: Customer — contrato equals/hashCode por ID, incluindo reconstituição
  Given um Customer construído com um ID explícito via Builder.setID()
  When comparado com outro Customer com o mesmo ID mas dados diferentes
  Then devem ser considerados iguais

Scenario: Loan — isOverdue() calcula corretamente com base no Clock injetado
  Given um Loan com startDate há 20 dias, considerando o limite de 15 dias
  When isOverdue(clock) é chamado com um Clock fixo simulando a data atual
  Then o resultado deve ser true

Scenario: Loan — isOverdue() retorna false dentro do prazo
  Given um Loan com startDate há 5 dias
  When isOverdue(clock) é chamado
  Then o resultado deve ser false
```

### Tasks

- [ ] `BookTest`: equals/hashCode, `compareTo` por título, `changeStatus`
- [ ] `CustomerTest`: equals/hashCode (incluindo cenário de reconstituição via `setID`), `compareTo` por nome
- [ ] `LoanTest`: equals/hashCode por `id`, `isOverdue()` com `Clock.fixed(...)` (nunca `Clock.systemDefaultZone()` em teste — não determinístico), cálculo de `dueDate()` via `endDate` nulo no builder
- [ ] Cobrir os cenários de `HashSet` (dedup por identidade) para as três entidades

### Commits

```
test(book): US-402 cobre contrato equals/hashCode e compareTo
test(customer): US-402 cobre contrato equals/hashCode com reconstituicao de id
test(loan): US-402 cobre contrato equals/hashCode e calculo de isOverdue com clock fixo
```

---

## US-406 — Test doubles (fakes) para repositórios

**Story:** Como *desenvolvedor*, preciso de implementações fake das interfaces de repositório dedicadas a teste, para isolar os usecases de qualquer estado real ou estrutura concorrente ao escrever testes unitários.

**Depende de:** US-401 e do E3/US-305 (interfaces já sem prefixo `I`)

### Cenários (BDD)

```gherkin
Scenario: Fake de LoanRepository permite popular estado manualmente
  Given um FakeLoanRepository vazio
  When um Loan é adicionado diretamente via método de apoio do fake
  Then uma busca subsequente deve retornar esse Loan

Scenario: Fake de BookRepository isola o teste de qualquer estado global
  Given dois testes distintos instanciam FakeBookRepository separadamente
  When cada um popula seu próprio estado
  Then o estado de um teste não deve vazar para o outro
```

### Tasks

- [ ] Criar `FakeLoanRepository`, `FakeBookRepository`, `FakeCustomerRepository` em `src/test/java/.../infrastructure/repository/fake` (usando `HashMap`/`ArrayList` simples — sem `ConcurrentHashMap`, teste não precisa de thread-safety)
- [ ] Adicionar métodos de apoio só de teste (ex: `seed(Loan loan)`) sem poluir a interface de produção
- [ ] Javadoc curto em cada fake explicando que é exclusivo de teste, não usar em produção

### Commits

```
test(fakes): US-406 adiciona FakeLoanRepository para uso em testes de usecase
test(fakes): US-406 adiciona FakeBookRepository e FakeCustomerRepository
```

---

## US-403 — Testes unitários dos usecases de Book

**Depende de:** US-406

### Cenários (BDD)

```gherkin
Scenario: CreateBook persiste um livro válido
  Given um FakeBookRepository vazio
  When o usecase CreateBook é executado com dados válidos
  Then o repositório deve conter exatamente 1 livro com status AVAILABLE

Scenario: SearchBooks filtra corretamente por ISBN e status
  Given um FakeBookRepository com 2 livros de status diferentes
  When SearchBooks é executado filtrando por status AVAILABLE
  Then o resultado deve conter apenas o livro com esse status
```

### Tasks

- [ ] `CreateBookTest`, `SearchBooksTest` (e demais usecases de Book existentes) usando `FakeBookRepository` injetado via construtor
- [ ] Cobrir fluxo feliz e pelo menos um cenário de erro/edge por usecase

### Commits

```
test(create-book): US-403 cobre fluxo de criacao de livro com fake repository
test(search-books): US-403 cobre filtros de busca com fake repository
```

---

## US-404 — Testes unitários dos usecases de Loan

**Depende de:** US-406

> Maior pontuação do épico: `Loan` concentra a maior parte das regras de negócio do MVP (elegibilidade, sincronização de status do livro, devolução, reversão de OVERDUE).

### Cenários (BDD)

```gherkin
Scenario: CreateLoan bloqueia cliente que atingiu o limite de empréstimos ativos
  Given um FakeLoanRepository com 2 empréstimos ACTIVE para o cliente "C1"
  When CreateLoan é executado para o cliente "C1"
  Then deve lançar RuleViolationException

Scenario: ReturnLoan finaliza empréstimo dentro do prazo
  Given um Loan ACTIVE dentro do prazo no FakeLoanRepository
  When ReturnLoan é executado para esse loan
  Then o status deve mudar para FINISHED
  And o BookRepository fake deve refletir o livro como AVAILABLE

Scenario: ReturnLoan marca como OVERDUE quando fora do prazo
  Given um Loan ACTIVE com startDate anterior ao limite, e um Clock fixo simulando a data atual
  When ReturnLoan é executado
  Then o status deve mudar para OVERDUE

Scenario: CloseOverdueLoan reverte apenas empréstimos OVERDUE
  Given um Loan com status ACTIVE
  When CloseOverdueLoan é executado para esse loan
  Then deve rejeitar a operação, informando que só se aplica a OVERDUE
```

### Tasks

- [ ] `CreateLoanTest`: cobrir `LoanEligibilityValidator` integrado (limite de 2, bloqueio por OVERDUE)
- [ ] `ReturnLoanTest`: cobrir os dois ramos (`FINISHED` vs `OVERDUE`), usando `Clock.fixed(...)` injetado — nunca `Clock` real em teste
- [ ] `SearchLoansTest`: cobrir critério isolado, status nulo, nenhum critério
- [ ] `CloseOverdueLoanTest`: cobrir reversão restrita a status OVERDUE

### Commits

```
test(create-loan): US-404 cobre elegibilidade e limite de emprestimos ativos
test(return-loan): US-404 cobre finalizacao no prazo e marcacao overdue com clock fixo
test(search-loans): US-404 cobre criterios isolados e status nulo
test(close-overdue-loan): US-404 cobre reversao restrita a status overdue
```

---

## US-405 — Testes unitários dos validators

**Depende de:** US-401

### Cenários (BDD)

```gherkin
Scenario: LoanEligibilityValidator aprova cliente elegível
  Given um cliente sem empréstimos ACTIVE ou OVERDUE
  When a validação é executada
  Then nenhuma exceção deve ser lançada

Scenario: SearchLoanValidator rejeita busca sem nenhum critério
  Given um filtro de busca totalmente vazio
  When a validação é executada
  Then deve lançar IllegalArgumentException
```

### Tasks

- [ ] `LoanEligibilityValidatorTest`: cobrir limite ativo e bloqueio por OVERDUE
- [ ] `SearchLoanValidatorTest`: cobrir `hasAnyCriteria` com `||`

### Commits

```
test(loan-eligibility-validator): US-405 cobre limite ativo e bloqueio por overdue
test(search-loan-validator): US-405 cobre criterio ausente e criterio isolado
```

---

## Definition of Done do épico E4

- [ ] JUnit 5 configurado e `mvn test` roda via CLI padrão
- [ ] Entidades de domínio (`Book`, `Customer`, `Loan`) com cobertura de testes de contrato e regras próprias
- [ ] Fakes de repositório disponíveis e usados por todos os testes de usecase
- [ ] Usecases de `Book` e `Loan` cobertos, incluindo fluxo feliz e principais exceções de regra de negócio
- [ ] Validators (`LoanEligibilityValidator`, `SearchLoanValidator`) cobertos
- [ ] Nenhum teste depende de `Clock` real, estado global, ou ordem de execução entre testes
- [ ] Todas as 6 sub-tasks em status Done

---

## Backlog de Débito Técnico

| ID | Item | Descrição | Pontos | Status |
| --- | --- | --- | --- | --- |
| TD-01 | Contrato `equals`/`hashCode` quebrado | `Book`, `Customer`, `Loan` — resolvido via US-304 | 3 | ✅ Resolvido (E3) |
| TD-02 | `WaitingList` é código morto | Entidade existe, mas sem repository, usecase ou CLI — decidir: implementar a feature ou remover | 3 | ✅ Resolvido (E3) |
| TD-03 | `BookUnavailableException` não utilizada | Existe mas `RuleViolationException` já cobre esse caso — avaliar remoção ou uso correto | 1 | ✅ Resolvido (E3) |

---

## Convenção de commits

Segue Conventional Commits, commits de linha única (sem corpo/rodapé — fluxo via terminal), com o ID da US logo após os dois-pontos:

```
<tipo>(<escopo>): <ID> <descrição no imperativo, minúsculo, sem ponto final>
```

- Sem fluxo de Pull Request ainda (ver E8 no roadmap), o commit vai direto pra `develop` — não há auto-close de Issue. Ao concluir uma US, feche a Issue manualmente no board.
- Múltiplos commits na mesma US: todos repetem o mesmo ID (`US-XXX`) no início da descrição.

---

## Convenção de versionamento

Segue SemVer (`MAJOR.MINOR.PATCH`):

- **`0.y.z`** enquanto o projeto está em desenvolvimento inicial — contratos internos (arquitetura, persistência, framework) ainda podem mudar sem aviso. `1.0.0` fica reservado pra quando o sistema estabilizar (por volta da Fase 3).
- **Sufixos `alpha`/`beta`/`rc`** só fazem sentido a partir do **Marco 2** (quando a API REST existir).
- **`SNAPSHOT`** no `pom.xml` durante desenvolvimento contínuo; a tag/release usa a versão limpa.

**Tags:**

| Tag | Marco | Data |
| --- | --- | --- |
| `v0.1.1` | Marco 1 — MVP (Épico E2 concluído) | ver histórico do Git |

---

## Convenções do board

- **Pontos:** escala Fibonacci simplificada (1, 2, 3, 5, 8)
- **Status:** 🔲 To Do · 🟡 In Progress · 🔵 In Review · ✅ Done
- **Numeração de história:** `US-{sprint}{sequencial}` (ex: US-401 = Sprint 4, item 1)
- **Numeração de débito técnico:** `TD-{sequencial}`, sem vínculo fixo a sprint até ser priorizado
- **Cenários BDD:** formato Gherkin (Given/When/Then), usados como critério de aceite formal de cada história

---

*Última atualização: Épico E3 concluído (19 pts, incluindo US-305 e TD-01). Épico E4 detalhado e iniciado (18 pts, Sprint 4). Detalhamento completo de épicos concluídos (E0–E3) removido deste arquivo — histórico vive nas Issues do GitHub Projects, para evitar duas fontes de verdade.*