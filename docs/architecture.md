# Arquitetura

- Microserviços
    * pix-api expõe endpoints REST para criação e consulta de pagamentos.
- Comunicação assíncrona
- Event-driven
- Separação de responsabilidades
    * pix-api: valida, persiste e consulta pagamentos, aplicando filtros e paginação para eficiência e melhor experiência do usuário.
    * ejb-module: processa transferências PIX com validação de saldo e controle de concorrência.
    * Banco de dados: armazena os pagamentos com índices nos campos filtráveis (status, senderPixKey, receiverPixKey) para garantir performance nas consultas paginadas.
## Componentes
- pix-api (entrada)
    * expõe endpoints GET /pix-payment/payment com suporte a filtros (status, senderPixKey, receiverPixKey) e paginação (page, size).
- ejb-module (Jakarta EE)
    * BenefitEjbService para processamento de transferências.
    * controle de saldo e concorrência.
- pix-processor (processamento)
- Kafka/RabbitMQ
- PostgreSQL
    * persistência dos pagamentos, com índices para consultas rápidas.
- MongoDB
