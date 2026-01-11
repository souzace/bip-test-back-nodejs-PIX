# 🏗️ Desafio Fullstack Integrado

Este desafio técnico visa avaliar suas habilidades em arquitetura de sistemas, resolução de problemas de concorrência em sistemas legados (EJB) e integração de tecnologias modernas (Spring Boot e Angular).

---

## 🚨 Instruções Obrigatórias (LEIA ANTES DE COMEÇAR)

Para que seu desafio seja avaliado, você **não deve realizar um Fork direto** deste repositório. Siga rigorosamente os passos abaixo:

1.  **Criação do Repositório:** Clique no botão **"Use this template"** (Usar este modelo) para criar um novo repositório público em sua conta pessoal do GitHub.
    * Caso o botão não esteja disponível, faça o **Clone** e suba o código em um **novo repositório público** no seu GitHub pessoal.
2.  **Privacidade:** O repositório deve ser **Público** para que nossa equipe consiga acessá-lo.
3.  **Avaliação:** A avaliação será realizada **exclusivamente no link do repositório criado por você.** Certifique-se de que todos os seus commits estejam presentes no seu diretório principal (branch main ou master).
**[!IMPORTANT]** Não envie Pull Requests para este repositório original. O seu trabalho deve residir apenas no repositório que você gerou a partir do template.
---

## 🎯 Objetivo

O objetivo é entregar uma solução funcional ponta a ponta que corrija uma falha crítica de integridade de dados e integre as camadas de persistência, lógica de negócio, API e interface de usuário.

### 📦 Estrutura do Projeto

* `/scripts`: Scripts SQL para criação do banco (`db-init.sql`) e massa de dados inicial (`seed.sql`).
* `/ejb-module`: Módulo de serviço EJB contendo a regra de negócio com falha de concorrência.
* `/services/pix-api-java`: API Spring Boot que consome o EJB e expõe os recursos.
* `/infra`: Configuração Docker Compose para execução do ambiente.
* `/docs`: Documentação complementar e critérios técnicos.

---

## ✅ Tarefas do Candidato

1.  **Setup de Dados:** Executar os scripts da pasta `/scripts` no seu ambiente de banco de dados.
2.  **Correção do Bug Crítico (EJB):** Identificar e corrigir a falha no `BeneficioEjbService`.
3.  **Backend (Spring Boot):** * Implementar o CRUD completo de benefícios.
    * Implementar a integração com o módulo EJB para operações de transferência.
4.  **Frontend (Angular):** Desenvolver uma interface funcional para gestão dos dados e execução das transferências.
5.  **Testes:** Implementar testes (unitários ou de integração) focados na lógica corrigida no EJB.
6.  **Documentação:** Atualizar o README do seu repositório com o passo a passo para rodar a aplicação.

---

## 🐞 Detalhes do Bug no EJB

O serviço de transferência no módulo EJB (`BeneficioEjbService`) possui falhas graves:
* **Problema:** O método não valida saldo disponível e não utiliza mecanismos de controle de concorrência. Isso permite que múltiplas requisições simultâneas gerem saldos negativos ou inconsistentes.
* **Expectativa:** Implementar validações de negócio, garantir a transacionalidade e prevenir race conditions.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- Docker e Docker Compose

### Opção 1: Executar com Docker Compose (Recomendado)

```bash
# 1. Navegar para a pasta infra
cd infra

# 2. Iniciar todos os serviços (banco de dados será criado automaticamente)
docker-compose up -d --build

# 3. Aguardar inicialização do banco (scripts executam automaticamente)
# Os scripts em /scripts serão executados na primeira inicialização

# A API estará disponível em http://localhost:8081
```

**Containers que serão iniciados:**
- `postgres` - Banco de dados PostgreSQL (porta 5432)
- `mongo` - MongoDB
- `kafka` - Apache Kafka
- `rabbitmq` - RabbitMQ
- `pix-api-java` - API REST Spring Boot (porta 8081)
- `pix-api-java-test` - Testes automatizados da API
- `pix-api-node` - API Node.js
- `wildfly` - Servidor WildFly para EJB (portas 8080, 9990)
- `ejb-test` - Testes automatizados do EJB

**Nota:** Os scripts SQL (`db-init.sql` e `seed.sql`) são executados automaticamente na primeira vez que o container PostgreSQL é criado. Se o volume já existe, os scripts não serão executados novamente.

### Opção 2: Executar Manualmente (Apenas Banco de Dados)

```bash
# 1. Iniciar apenas PostgreSQL
cd infra
docker-compose up -d postgres

# 2. Compilar módulo EJB
cd ../ejb-module
mvn clean install

# 3. Compilar e executar API
cd ../services/pix-api-java
mvn clean install
mvn spring-boot:run

# A API estará disponível em http://localhost:8080
```

### Verificar se está funcionando

```bash
# Listar benefícios
curl http://localhost:8081/api/v1/benefits

# Criar benefício
curl -X POST http://localhost:8081/api/v1/benefits \
  -H "Content-Type: application/json" \
  -d '{"userId":"test","balance":1000.00,"pixKey":"11988776655"}'
```

---

## 📊 Verificar Resultados dos Testes

### Ver Logs dos Testes (Executados automaticamente no docker-compose up)

```bash
# Logs dos testes do EJB (valida correção do bug de concorrência)
docker logs infra-ejb-test-1

# Logs dos testes da API (valida CRUD e endpoints)
docker logs infra-pix-api-java-test-1
```

### Executar Testes Localmente (Alternativa)

```bash
# Testes do EJB
cd ejb-module
mvn test

# Testes da API
cd services/pix-api-java
mvn test
```

**Resultado esperado:**
- ✅ EJB: 32 testes passando (validação do bug de concorrência corrigido)
- ✅ API: 16 testes passando (CRUD, endpoints, integrações)
- ✅ **Total: 48 testes** no projeto

### Ver Logs dos Outros Containers

```bash
# Logs da API Java
docker logs infra-pix-api-java-1 -f

# Logs do PostgreSQL
docker logs infra-postgres-1 -f

# Ver todos os logs em tempo real
cd infra
docker-compose logs -f
```

---

## 🐛 Bug Corrigido - Concorrência no EJB

### Problema Identificado

O serviço `BenefitEjbService` permitia que múltiplas threads processassem pagamentos simultaneamente, resultando em:
- Saldos negativos
- Operações duplicadas aprovadas
- Inconsistência de dados

### Solução Implementada

Implementado controle de concorrência com `synchronized` garantindo:
- Verificação de saldo e débito/crédito ocorrem atomicamente
- Validações robustas (valor positivo, chaves diferentes, saldo suficiente)
- Impossível aprovar mais pagamentos do que o saldo permite

### Validação

Testes de concorrência com 20 threads simultâneas:
- **Antes:** 20 pagamentos aprovados (BUG)
- **Depois:** 5 pagamentos aprovados, 15 rejeitados (CORRETO)

**Status:** ✅ 32/32 testes passando

---

## 📚 Documentação Adicional

- `docs/architecture.md` - Arquitetura do sistema
- `docs/decisions.md` - Decisões técnicas
- `docs/pix-flow.md` - Fluxo de pagamentos PIX
- `docs/api-contracts/pix-api.yaml` - Contrato OpenAPI
