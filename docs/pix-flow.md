1. Cliente envia solicitação PIX
    - O cliente faz uma requisição POST para /api/v1/pix/payments, enviando os dados do pagamento (valor, chave PIX origem, chave PIX destino, descrição).

2. API valida e persiste
    - A API Spring Boot valida os dados recebidos e persiste o pagamento no banco de dados PostgreSQL.

3. Processamento de transferência (EJB)
    - EJB valida requisição completa (valor positivo, chaves diferentes, existência de contas).
    - Verifica saldo disponível do remetente dentro de bloco sincronizado.
    - Debita remetente e credita destinatário atomicamente com controle de concorrência.
    - Retorna sucesso ou erro (saldo insuficiente, validações).

4. Consulta paginada de pagamentos
    - O cliente faz uma requisição GET para /api/v1/pix/payments.
    - Pode informar filtros opcionais: status, senderPixKey, receiverPixKey.
    - Pode informar parâmetros de paginação: page (padrão 0), size (padrão 10).
    - A API valida os parâmetros e executa a consulta paginada no banco usando índices otimizados.
    - A resposta retorna uma lista paginada com campos: content, totalElements, totalPages, size, number.

5. Gestão de benefícios (CRUD)
    - O cliente acessa /api/v1/benefits para criar, listar, atualizar ou deletar benefícios.
    - Operações diretas no banco via Spring Data JPA sem passar pelo EJB.
