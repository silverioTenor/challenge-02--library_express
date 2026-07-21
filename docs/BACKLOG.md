# Library Express — Backlog Ágil

> Projeto de estudo em Java, evoluído de forma incremental via sprints reais.
> PO/Scrum Master: Claude · Dev: Silvério
>
> Para a visão de produto de longo prazo (expansões futuras: marketplace, pagamentos, clube do livro, audiobook), ver `VISION.md`. Este arquivo trata só do que é executável.
>
> **Nota sobre idioma:** este documento está em português (língua de trabalho do planejamento). O `../README.md` do repositório está em inglês, por ser a documentação voltada ao público — a divisão é intencional.

---

## Épicos

| ID | Épico | Status |
|----|-------|--------|
| E0 | Organização e limpeza inicial | ✅ Concluído (Sprint 0 e 1) |
| E1 | MVP — Ciclo de vida do empréstimo | 🔵 Em andamento (Sprint 2) |
| E2 | Débito técnico e qualidade | ⏳ Backlog (pós-MVP) |
| E3 | Inversão de dependência manual | ⏳ Backlog |
| E6 | CD — Pipeline de entrega + API mínima (**Marco 2 — Go Live**) | ⏳ Próximo após E1 |
| E7 | CI real — testes automatizados rodando no pipeline | ⏳ Backlog |
| E4 | Persistência real (JDBC/JPA) | ⏳ Backlog |
| E5 | Evolução arquitetural completa (migração da API mínima pra Spring) | ⏳ Backlog |
| E8 | Reputação do cliente e bloqueio por atraso | ⏳ Backlog (sem prioridade definida) |

> **E6 e E7 não são a mesma coisa.** E6 entrega só build automático + deploy — chamamos de "CD", não "CI/CD", porque sem testes não há integração *verificada*, só entrega automatizada. E7 é quando isso vira CI de verdade: testes (TD-05) passam a rodar a cada push, como gate do pipeline.

> **E8** nasceu de uma discussão durante o Sprint 2, sobre o fluxo de devolução (US-203): quando um empréstimo está em atraso, o cliente perde pontos de score; após 3 atrasos é "marcado" (conceito ainda a refinar); após 5, é bloqueado por tempo determinado. Não é débito técnico — é escopo novo. Sem multa/dinheiro envolvido (alinhado ao sequenciamento de pagamentos do `VISION.md`). Sem prioridade definida ainda; será refinado em BDD quando entrar na fila, seguindo a regra de "um épico por vez". Questões técnicas já identificadas: (1) como detectar atraso sem scheduler — provavelmente cálculo *lazy* na devolução/validação, não job em background; (2) `Customer` vai precisar de campos novos (score, contagem de atrasos, `blockedUntil`).

---

## Roadmap — Fases e Marcos

### 🌱 Fase 1 — Foundation
**Objetivo:** construir uma base sólida, consolidando o domínio e eliminando problemas arquiteturais antes de introduzir novas tecnologias.
**Escopo:** E0, E1, E3.

### 🚀 Marco 1 — MVP
O sistema atende aos requisitos funcionais essenciais de uma biblioteca, via CLI. Fecha com E1.

### 🚀 Marco 2 — Go Live
Primeira subida real pra produção (Heroku). Fecha **junto**: pipeline de CD (E6) + API mínima sem framework (`com.sun.net.httpserver.HttpServer`, sem Spring ainda — evita reescrever esforço quando a migração acontecer em E5). O deploy só "vale" quando há um serviço HTTP de verdade recebendo tráfego — antes disso, CD sozinho seria só teatro de automação sem produto real por trás.

### 🏗️ Fase 2 — Software Maturity
**Objetivo:** aumentar qualidade e confiabilidade do sistema, guiado pelas necessidades reais do projeto.
**Sequência de temas:**
1. Marco 2 — Go Live (E6)
2. Testes automatizados (TD-05)
3. CI real — testes como gate do pipeline (E7)
4. Persistência real (E4)
5. Evolução arquitetural — migração pra Spring (E5)

> **Por que testes e persistência crus antes do Spring, e não o contrário?** `@SpringBootTest`/Mockito e Spring Data JPA são abstrações sobre JUnit puro e JDBC puro. Fazer o caminho manual primeiro é deliberado: força entender o mecanismo por baixo (DI, transação, `Connection`/`PreparedStatement`) antes da conveniência do framework escondê-lo. Refazer com Spring depois em E5 não é retrabalho desperdiçado — é o próprio exercício que revela o que a abstração compra. Essa é uma escolha pedagógica, não a única correta; um time sob prazo real provavelmente iria direto pro Spring Data e aprenderia os internals sob demanda.

### ⚙️ Fase 3 — Professional Software Engineering
**Objetivo:** aprofundar práticas de engenharia em um sistema que já está em produção desde o Marco 2 — segurança, observabilidade, performance, escalabilidade, documentação.
**Escopo:** ainda sem épicos formais (backlog futuro).

### Princípios (mantidos do roadmap original)
- O domínio é sempre a prioridade.
- Novas tecnologias são introduzidas apenas quando resolvem problemas reais.
- Cada Sprint deve gerar uma entrega funcional.
- A arquitetura evolui junto com o sistema.
- O aprendizado acontece através da prática.

**Regra de trabalho:** um épico por vez, refinado em detalhe (BDD + tasks) apenas quando entra em andamento. Épicos futuros ficam só com título até chegar a vez deles (backlog grooming just-in-time).

---

# 🔵 Épico E1 — MVP: Ciclo de vida do empréstimo

### Objetivo do épico
Entregar a primeira versão **funcional e utilizável** do sistema: um usuário consegue, via CLI, cadastrar clientes e livros, realizar um empréstimo e devolvê-lo — respeitando as regras de negócio — sem bugs bloqueantes e sem exceções não tratadas.

### Valor de negócio
Sem isso, o sistema não é uma biblioteca funcional: hoje um livro pode ser emprestado infinitas vezes em paralelo e não existe forma de encerrar um empréstimo. Este é o núcleo mínimo que torna o projeto "real".

### Definition of Done do épico
- [ ] Fluxo completo executável via CLI: cadastrar cliente → cadastrar livro → emprestar → devolver → emprestar novamente
- [ ] Regras de negócio (disponibilidade do livro, limite de empréstimos ativos) aplicadas corretamente
- [ ] Nenhuma exceção não tratada (`NullPointerException`, etc.) durante o fluxo feliz ou os fluxos de erro esperados
- [ ] Todas as histórias abaixo em status Done

### Sprint
Sprint 2 · Capacidade: ~20h/semana · Total: **13 pontos**

### Ordem de dependência
```
US-201 (busca de livro)
   └──> US-202 (sincronizar status do livro)
          └──> US-203 (devolução)

US-204 (busca de empréstimo) ── independente
US-205 (limite de empréstimos) ── independente
```

---

## US-201 — Corrigir busca de livros disponíveis

**Story:** Como *sistema*, preciso localizar livros corretamente na busca, para que a validação de disponibilidade funcione e empréstimos possam ser criados.

**Pontos:** 2 · **Status:** 🔲 To Do

### Cenários (BDD)

```gherkin
Scenario: Buscar livro existente por ISBN, sem filtro de status
  Given um livro com ISBN "123-45-67890-12-3" e status AVAILABLE está cadastrado
  When o sistema busca livros pelo ISBN "123-45-67890-12-3" sem filtro de status
  Then o resultado deve conter exatamente esse livro

Scenario: Buscar por ISBN filtrando por status que não corresponde
  Given um livro com ISBN "X" está cadastrado com status UNAVAILABLE
  When o sistema busca esse ISBN filtrando por status AVAILABLE
  Then o resultado deve vir vazio

Scenario: Buscar apenas por status, sem informar ISBN
  Given existem livros com status AVAILABLE e status UNAVAILABLE cadastrados
  When o sistema busca sem ISBN, filtrando por status AVAILABLE
  Then o resultado deve conter apenas os livros com status AVAILABLE

Scenario: Buscar ISBN inexistente
  Given nenhum livro com ISBN "000-00-00000-00-0" está cadastrado
  When o sistema busca por esse ISBN
  Then o resultado deve vir vazio, sem lançar exceção
```

### Tasks
- [ ] Implementar composição de `Predicate<Book>` em `BookRepository.search()`, seguindo o mesmo padrão já usado em `LoanRepository.search()`
- [ ] Tratar `ISBN` nulo/em branco como "sem filtro de ISBN"
- [ ] Tratar `statuses` nulo ou vazio como "sem filtro de status"
- [ ] Validar manualmente via CLI: cadastrar 2 livros com status diferentes, buscar por ISBN, buscar só por status
- [ ] Documentar checklist de validação manual (ainda sem JUnit — isso é TD-05)

---

## US-202 — Sincronizar status do livro com o empréstimo

**Story:** Como *sistema*, preciso sincronizar o status do livro com o ciclo do empréstimo, para impedir que o mesmo exemplar seja emprestado simultaneamente para mais de um cliente.

**Pontos:** 3 · **Status:** 🔲 To Do
**Depende de:** US-201

### Cenários (BDD)

```gherkin
Scenario: Emprestar um livro disponível
  Given um livro está com status AVAILABLE
  When um empréstimo é criado para esse livro
  Then o status do livro deve mudar para UNAVAILABLE

Scenario: Tentar emprestar um livro já emprestado
  Given um livro está com status UNAVAILABLE
  When um novo empréstimo é solicitado para o mesmo livro
  Then o sistema deve lançar RuleViolationException informando indisponibilidade
  And nenhum novo empréstimo deve ser criado

Scenario: Devolução libera o livro
  Given um empréstimo ACTIVE existe para um livro com status UNAVAILABLE
  When esse empréstimo é finalizado (US-203)
  Then o status do livro deve voltar para AVAILABLE
```

### Tasks
- [ ] Adicionar um método em `IBookRepository`/`BookRepository` para atualizar o status de um livro (ex: `changeStatus(String ISBN, BookStatus status)`) — ou reaproveitar `getByIsbn` + `book.changeStatus(...)` diretamente, já que `Book` é mutável
- [ ] Em `CreateLoan.execute()`, após criar o empréstimo, acionar a mudança de status do livro para `UNAVAILABLE`
- [ ] Nota técnica: como `Book.equals`/`hashCode` são baseados no ISBN (campo imutável), mutar o `status` de um `Book` já presente no `HashSet` é seguro — não corrompe a estrutura interna do `Set`
- [ ] Validar manualmente: emprestar → tentar emprestar de novo (deve bloquear) → devolver → emprestar de novo (deve permitir)

---

## US-203 — Registrar devolução de empréstimo

**Story:** Como *atendente*, quero registrar a devolução de um empréstimo, para que o livro fique disponível novamente e o histórico do cliente seja atualizado.

**Pontos:** 5 · **Status:** 🔲 To Do
**Depende de:** US-202

### Cenários (BDD)

```gherkin
Scenario: Devolver um empréstimo ativo existente
  Given um empréstimo ACTIVE existe para o cliente "C1" e o livro ISBN "X"
  When o atendente registra a devolução informando cliente "C1" e ISBN "X"
  Then o status do empréstimo deve mudar para FINISHED
  And o livro deve voltar ao status AVAILABLE

Scenario: Tentar devolver um empréstimo inexistente
  Given não existe empréstimo ACTIVE para o cliente "C2" e o livro ISBN "Y"
  When o atendente tenta registrar a devolução dessa combinação
  Then o sistema deve informar "empréstimo não encontrado", sem lançar exceção não tratada

Scenario: Tentar devolver um empréstimo já finalizado
  Given um empréstimo para cliente "C1" e ISBN "X" já está FINISHED
  When o atendente tenta devolvê-lo novamente
  Then o sistema deve informar que não há empréstimo ativo para essa combinação
```

### Tasks
- [ ] Criar usecase `FinishLoan` (`application/loan/usecase/FinishLoan.java`), seguindo o padrão dos demais usecases (construtor resolve o repositório, `execute(...)` contém a regra)
- [ ] Usecase busca o empréstimo `ACTIVE` por `customerId` + `ISBN` via `loanRepository.search(...)`; lança `NotFoundException` se não encontrar
- [ ] Altera o status para `FINISHED` e persiste via `loanRepository.update(...)`
- [ ] Aciona a liberação do livro (integração com US-202)
- [ ] Implementar `LoanCli.finishLoan(scan)`: coletar `customerId` e `ISBN`, chamar o usecase, tratar exceções
- [ ] Conectar a opção de devolução ao menu do `LoanCli.init()` (hoje só existe `[1] New`, `[2] Search`, `[3] List` — falta a opção de devolução)

---

## US-204 — Buscar empréstimos por qualquer critério isolado

**Story:** Como *atendente*, quero buscar empréstimos usando apenas um critério (cliente, livro ou status), para consultar o histórico sem precisar preencher todos os campos.

**Pontos:** 2 · **Status:** 🔲 To Do
**Independente** (pode ser feita em paralelo com US-202/203)

### Cenários (BDD)

```gherkin
Scenario: Buscar somente por cliente
  Given empréstimos existem para os clientes "C1" e "C2"
  When a busca é feita apenas com customerId = "C1"
  Then o resultado deve conter apenas empréstimos de "C1"

Scenario: Buscar somente por status
  Given existem empréstimos com status ACTIVE e status FINISHED
  When a busca é feita apenas com status = ACTIVE
  Then o resultado deve conter apenas os empréstimos ACTIVE

Scenario: Buscar sem nenhum critério
  Given qualquer estado do sistema
  When a busca é feita sem nenhum critério preenchido
  Then o sistema deve lançar IllegalArgumentException "At least one search criteria must be provided"

Scenario: Buscar com status nulo mas outro critério preenchido
  Given empréstimos existem para o cliente "C1"
  When a busca é feita com customerId = "C1" e status nulo
  Then o resultado não deve lançar NullPointerException
  And deve retornar os empréstimos de "C1"
```

### Tasks
- [ ] Corrigir `hasAnyCriteria` em `SearchLoans`: trocar os `&&` por `||` entre os três critérios
- [ ] Corrigir a chamada `loanRepository.search(...)`: hoje monta sempre `Set.of(filter.status())`, que lança NPE se `status` for nulo — ajustar para passar `null` (ou conjunto vazio) quando o status não for informado
- [ ] Implementar `LoanCli.searchLoan(scan)`: coletar os critérios de forma opcional e chamar o usecase
- [ ] Validar manualmente os 4 cenários acima

---

## US-205 — Formalizar regra do limite de empréstimos ativos

**Story:** Como *sistema*, preciso aplicar corretamente o limite de empréstimos ativos por cliente, para manter a regra de negócio clara, nomeada e testável.

**Pontos:** 1 · **Status:** 🔲 To Do
**Independente**

### Cenários (BDD)

```gherkin
Scenario: Cliente sem empréstimos ativos
  Given o cliente "C1" não possui empréstimos ACTIVE ou OVERDUE
  When um novo empréstimo é solicitado
  Then a validação deve passar

Scenario: Cliente com 1 empréstimo ativo
  Given o cliente "C1" possui 1 empréstimo ACTIVE
  When um novo empréstimo é solicitado
  Then a validação deve passar (limite de 2 ainda não atingido)

Scenario: Cliente com 2 empréstimos ativos
  Given o cliente "C1" possui 2 empréstimos ACTIVE
  When um novo empréstimo é solicitado
  Then deve lançar RuleViolationException "Customer has reached the maximum of 2 active loans"

Scenario: Cliente com empréstimo em atraso
  Given o cliente "C1" possui 1 empréstimo OVERDUE
  When um novo empréstimo é solicitado
  Then deve lançar RuleViolationException informando pendência de devolução em atraso
```

### Tasks
- [ ] Criar constante nomeada `MAX_ACTIVE_LOANS = 2` (sugestão: em `infrastructure/config/Constant.java`, que já existe)
- [ ] Atualizar `LoanEligibilityValidator` para usar a constante em vez do número mágico atual
- [ ] Corrigir a mensagem de erro para refletir o valor real do limite (hoje o texto diz "more than one", mas a regra permite 2)
- [ ] Validar manualmente os 4 cenários acima

---

## Backlog de Débito Técnico (E2 — pós-MVP, não bloqueante)

| ID | Item | Descrição | Pontos (estimativa) |
|----|------|-----------|----------------------|
| TD-01 | Contrato `equals`/`hashCode` quebrado | `Book`: `hashCode` usa `year`, `equals` usa `ISBN` (campos diferentes = contrato violado). `Customer`: `equals` usa OR entre ID e e-mail, o que quebra transitividade | 3 |
| TD-02 | `WaitingList` é código morto | Entidade existe, mas sem repository, usecase ou CLI — decidir: implementar a feature ou remover | 3 |
| TD-03 | `BookUnavailableException` não utilizada | Existe mas `RuleViolationException` já cobre esse caso — avaliar remoção ou uso correto | 1 |
| TD-04 | Ausência de Inversão de Dependência | Usecases instanciam `BookRepository.DB` / `LoanRepository.DB` direto no construtor, mesmo dependendo da interface — dificulta testes unitários isolados | 5 |
| TD-05 | Nenhuma cobertura de testes automatizados | Introduzir JUnit 5 e cobrir as usecases principais (merece sprint próprio, dedicado a aprender a ferramenta) | 8 |

---

## Convenção de commits

Segue [Conventional Commits](https://www.conventionalcommits.org/), commits de linha única (sem corpo/rodapé — fluxo via terminal), com o ID da US logo após os dois-pontos:

```
<tipo>(<escopo>): <ID> <descrição no imperativo, minúsculo, sem ponto final>
```

- Sem fluxo de Pull Request ainda (ver E6 no roadmap), o commit vai direto pra `develop` — não há auto-close de Issue. Ao concluir uma US, feche a Issue manualmente no board.
- Múltiplos commits na mesma US: todos repetem o mesmo ID (`US-XXX`) no início da descrição.

**Mapeamento de tipo por história (Sprint 2):**

| US | Tipo | Escopo sugerido |
|----|------|------------------|
| US-201 | `fix` | `book-repository` |
| US-202 | `fix` | `loan` |
| US-203 | `feat` | `loan` |
| US-204 | `fix` | `loan-repository` |
| US-205 | `fix` | `loan-validator` |

**Exemplos:**
```
fix(book-repository): US-201 implementa filtro de busca por isbn e status
fix(loan): US-202 sincroniza status do livro ao criar e finalizar emprestimo
feat(loan): US-203 implementa devolucao de emprestimo
fix(loan-repository): US-204 corrige criterio de busca opcional e npe de status nulo
fix(loan-validator): US-205 formaliza limite de emprestimos ativos
```

---

## Convenções do board

- **Pontos:** escala Fibonacci simplificada (1, 2, 3, 5, 8)
- **Status:** 🔲 To Do · 🟡 In Progress · 🔵 In Review · ✅ Done
- **Numeração de história:** `US-{sprint}{sequencial}` (ex: US-201 = Sprint 2, item 1)
- **Numeração de débito técnico:** `TD-{sequencial}`, sem vínculo fixo a sprint até ser priorizado
- **Cenários BDD:** formato Gherkin (Given/When/Then), usados como critério de aceite formal de cada história

---

*Última atualização: Épico E1 detalhado — Sprint 2*
