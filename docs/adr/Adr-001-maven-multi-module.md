# ADR-001 — Modularização Maven (domain / application / infrastructure)

## Status
**Aceito — registrado retroativamente.**
A decisão foi implementada diretamente na branch `develop`, sem passar por uma User Story
formal. Este ADR documenta a decisão a posteriori (ver TD04 no `BACKLOG.md`), como caso
excepcional reconhecido — não como novo padrão de fluxo de trabalho.

**Data da decisão (implementação):** ver histórico de commits em `develop`, período do Épico E4.
**Data de registro deste ADR:** consolidação documental pós-E3/durante E4.

## Contexto

Até o Épico E3, o projeto **Library Express** era um único módulo Maven (`packaging: jar`),
com as camadas `domain`, `application` e `infrastructure` organizadas apenas como pacotes
Java dentro de `org.libraryexpress`, todos compartilhando um único `src/main` e `src/test`.

Essa organização era suficiente enquanto não havia suíte de testes automatizados relevante.
Com a entrada do Épico E4 (Fundação de Testes Automatizados JUnit 5), surgiu a necessidade
prática de isolar testes por camada:

- Testes de **domínio** (`BookTest`, `CustomerTest`, `LoanTest`) precisam validar regras de
  negócio e contratos (`equals`/`hashCode`) sem qualquer acesso a implementações de
  infraestrutura.
- Testes de **usecase** (`application`) precisam de test doubles (fakes) de repositório, mas
  não devem ter acesso a classes de CLI ou configuração de infraestrutura.
- Um único `src/test` compartilhado por todas as camadas não impõe essa fronteira — nada
  impede, por exemplo, que um teste de domínio importe uma classe de infraestrutura por
  engano, e isso só seria percebido por revisão manual, não pelo build.

Além disso, o projeto já usava uma organização em pacotes que espelhava Clean Architecture
(`domain`, `application`, `infrastructure`), mas essa separação era apenas **lógica**
(convenção de pacote), não **física** (unidade de compilação). Não havia enforcement real
de que `domain` não pudesse depender de `infrastructure`.

## Decisão

Migrar de um módulo Maven único para um **projeto multi-módulo**, com:

- Um `pom.xml` raiz agregador (`packaging: pom`), sem código-fonte próprio, responsável por
  centralizar `dependencyManagement` (versões de JUnit, MapStruct, Jackson) e
  `pluginManagement` (compiler, surefire, exec).
- Três módulos filhos, cada um com `pom.xml` próprio e `src/main`/`src/test` independentes:
    - **`domain`** — entidades, enums, helpers, interfaces de repositório/validador. Sem
      dependência de `application` ou `infrastructure`.
    - **`application`** — casos de uso, DTOs, mappers, validadores de aplicação. Depende
      apenas de `domain`.
    - **`infrastructure`** — entrypoint CLI, configuração, implementações concretas de
      repositório (in-memory). Depende de `domain` e `application`.
- Direção de dependência fixada como `infrastructure → application → domain`, agora
  **enforçada pelo próprio Maven**: uma tentativa de `domain` depender de `application` falha
  a build, não apenas a revisão de código.

## Alternativas consideradas

| Alternativa | Por que foi descartada |
|---|---|
| Manter módulo único, usar apenas convenção de pacote + revisão manual disciplinada | Não escala com o crescimento do projeto; a fronteira entre camadas depende de disciplina humana, não de enforcement de build. Falha silenciosamente. |
| Módulo único, mas com `src/test` segmentado por sourceSet customizado (ex: Gradle-like) | Maven não tem suporte nativo maduro a múltiplos sourceSets por módulo sem plugins adicionais (ex: `build-helper-maven-plugin`), o que adicionaria complexidade equivalente à multi-módulo, sem o benefício do enforcement de dependência entre camadas. |
| Multi-módulo, mas com granularidade maior (ex: um módulo por feature — `book`, `customer`, `loan`) | Prematuro para o estágio atual do projeto (MVP solo, Fase 2). Aumentaria custo de manutenção do build sem ganho proporcional — violaria o princípio de "low-cost future-aware" adotado no projeto. |

## Consequências

**Positivas**
- Testes de domínio fisicamente isolados de infraestrutura — impossível vazar dependência
  por acidente.
- Direção de dependência entre camadas (`domain ← application ← infrastructure`) validada
  pelo compilador Maven, não apenas por convenção.
- Cada módulo pode rodar sua suíte de testes isoladamente (`mvn test -pl domain`), útil para
  ciclos de feedback rápidos durante TDD.
- Estrutura mais próxima do que se espera em times profissionais que praticam Clean/Hexagonal
  Architecture com enforcement real — relevante para o objetivo de mercado internacional.

**Negativas / trade-offs aceitos**
- Overhead de build: `mvn install` precisa resolver o reactor multi-módulo, mais lento que um
  módulo único para um projeto deste porte.
- Empacotamento final (`.jar` executável) fica mais complexo — o módulo `infrastructure`
  sozinho não inclui as dependências de `domain`/`application` no artefato, exigindo plugin
  de shade/assembly (ver TD05, vinculado ao Épico E8).
- Três arquivos `pom.xml` para manter versão/dependências sincronizadas em vez de um único,
  ainda que mitigado pelo `dependencyManagement` centralizado no pai.

## Notas
Este ADR foi criado como parte do item de débito técnico TD04, reconhecido explicitamente
como caso excepcional (implementação sem User Story prévia). Não estabelece precedente para
que mudanças estruturais futuras sigam sem planejamento em `BACKLOG.md`.