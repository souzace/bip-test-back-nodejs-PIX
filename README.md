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

* `/db`: Scripts SQL para criação do banco (`schema.sql`) e massa de dados inicial (`seed.sql`).
* `/ejb-module`: Módulo de serviço EJB contendo a regra de negócio com falha de concorrência.
* `/backend-module`: API Spring Boot que consome o EJB e expõe os recursos.
* `/frontend`: Aplicação Angular para interação com o usuário.
* `/docs`: Documentação complementar e critérios técnicos.

---

## ✅ Tarefas do Candidato

1.  **Setup de Dados:** Executar os scripts da pasta `/db` no seu ambiente de banco de dados.
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
* **Expectativa:** Implementar validações de negócio, garantir a transacional
