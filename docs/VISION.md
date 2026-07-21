# Library Express — Visão de Produto (Longo Prazo)

> Este documento é **aspiracional**, não executável. Ele não gera épicos, sprints ou histórias diretamente — serve como bússola para decisões de arquitetura de baixo custo no presente e como fonte futura de refinamento quando a hora chegar.
>
> Para o que está de fato em execução, ver `BACKLOG.md`.
>
> **Nota sobre idioma:** este documento está em português, assim como o `BACKLOG.md`. O `../../../Downloads/files/README.md` do repositório está em inglês — divisão intencional entre documentação pública e material de planejamento.

---

## Núcleo do negócio (não muda)

O empréstimo de livro físico continua sendo o core do Library Express. Tudo abaixo é **expansão secundária**, visando atender um público maior — não substitui o núcleo.

---

## Expansões futuras

### 1. Plataforma self-service
Cliente se cadastra sozinho e agenda a retirada de um livro físico on-line — essencialmente o mesmo fluxo de empréstimo atual, mas iniciado remotamente em vez de presencialmente por um atendente.

**Implica:** `Customer` precisa evoluir para suportar autenticação/identidade própria (hoje é cadastrado por um atendente, sem login).

### 2. Marketplace de acervos de terceiros
Cliente consulta outros acervos, que vendem livros físicos ou digitais.

**Implica:** o conceito de "livro" deixa de pertencer só à biblioteca — passa a existir a noção de **múltiplas origens de catálogo** (biblioteca própria vs. acervo de terceiros) e de **formato** (físico vs. digital).

### 3. Pagamentos e segurança
Entra como consequência direta das vendas do marketplace.

**Atenção especial:** esse é o domínio de maior risco/sensibilidade da visão inteira — envolve tratamento de transação, falha, idempotência e segurança de dados (mesmo em ambiente de estudo, vale simular com o cuidado real: nunca persistir dado sensível de pagamento em texto puro, por exemplo).
**Sequenciamento sugerido:** só deveria ser abordado depois que a Fase 3 do roadmap de engenharia (`BACKLOG.md`) já tiver dado alguma bagagem de segurança e observabilidade. Fazer pagamentos sem essa base é pular etapa.

### 4. Clube do livro
Assinatura recorrente com desconto na compra.

**Implica:** conceito de associação/assinatura do cliente, separado do cadastro simples.

### 5. Audiobook e documentários em vídeo
Mais distante no tempo. Envolve distribuição de mídia digital (armazenamento, possivelmente streaming).

---

## Evolução arquitetural de longo prazo — microsserviços e EDA

A intenção é, no futuro, fragmentar o sistema numa arquitetura de **microsserviços**, usando o **padrão Strangler Fig** (estrangulamento) como estratégia de migração — extrair serviços do monólito gradualmente, por trás de uma fachada, em vez de reescrita completa.

Junto disso, entra inevitavelmente **EDA (Event-Driven Architecture)**: com múltiplos serviços fragmentados, comunicação direta (chamada síncrona) cria acoplamento entre eles; eventos resolvem isso, permitindo que serviços reajam a mudanças de estado uns dos outros sem se conhecerem diretamente.

**Observação de baixo custo já aproveitada:** a separação em camadas que o projeto já adota (`domain` / `application` / `infrastructure`, usecases isoladas de forma de transporte) é exatamente a costura que o Strangler Fig precisa para extrair serviços aos poucos sem reescrever tudo. Essa decisão foi tomada por outros motivos, mas paga esse dividendo de graça no futuro — não foi necessário nenhum esforço extra hoje para se alinhar com essa visão.

**Sequenciamento sugerido:** microsserviços e EDA são, honestamente, a decisão mais cara de toda essa visão de longo prazo. Fazem sentido só depois que a Fase 3 do roadmap de engenharia (segurança, observabilidade) já estiver madura — depurar um sistema distribuído sem observabilidade é impraticável. Também pressupõe que o marketplace/pagamentos já tenham gerado múltiplos domínios reais o suficiente pra justificar a fragmentação (fragmentar um sistema pequeno demais cedo é complexidade sem benefício).

---

## Domínios que essa visão introduz

Hoje o domínio é essencialmente: **Cliente empresta Livro**. Essa visão introduz, em bounded contexts que no mundo real seriam times/serviços separados:

- **Catálogo/Marketplace** — múltiplas origens de livro, físico e digital
- **Pagamentos** — o mais sensível, tratado à parte
- **Assinatura/Clube** — recorrência e desconto
- **Mídia** — audiobook e vídeo, armazenamento e streaming
- **Identidade** — autenticação self-service do cliente

---

## Princípio de arquitetura acordado

**Não fazemos YAGNI estrito nem over-engineering antecipado.** A regra combinada é:

> Sempre que uma decisão do roadmap atual (`BACKLOG.md`) tiver **custo baixo** para ficar mais compatível com essa visão futura, tomamos essa decisão agora. Quando o custo não for baixo, construímos só o que a fase atual precisa e refatoramos quando a hora chegar.

Isso não gera trabalho antecipado — só influencia escolhas de nomenclatura, limites de responsabilidade e pontos de extensão quando isso não custar esforço extra hoje.

---

*Este documento deve ser revisitado a cada nova fase do roadmap de engenharia, para reavaliar se alguma decisão de baixo custo se tornou relevante.*
