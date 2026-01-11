# Arquitetura

- Separação de responsabilidades
    * pix-api-java: valida, persiste e consulta pagamentos, aplicando filtros e paginação para eficiência e melhor experiência do usuário.
    * ejb-module: processa transferências PIX com validação de saldo e controle de concorrência.
    * Banco de dados PostgreSQL: armazena os pagamentos com índices nos campos filtráveis (status, senderPixKey, receiverPixKey) para garantir performance nas consultas paginadas.

## Componentes

- pix-api-java (Spring Boot)
    * expõe endpoints REST em /api/v1/benefits e /api/v1/pix/payments
    * suporta filtros (status, senderPixKey, receiverPixKey) e paginação (page, size)
- ejb-module (Jakarta EE)
    * BenefitEjbService para processamento de transferências
    * controle de saldo e concorrência com synchronized
- PostgreSQL
    * persistência dos pagamentos e benefícios
    * índices para consultas rápidas
