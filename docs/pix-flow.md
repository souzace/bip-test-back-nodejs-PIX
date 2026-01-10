1. Cliente envia solicitação PIX
    - O cliente faz uma requisição POST para /api/v1/pix/payments, enviando os dados do pagamento.
2. API valida e persiste
    - A API valida os dados recebidos e persiste o pagamento no banco de dados.
3. Processamento de transferência (EJB)
    - EJB valida requisição e saldo disponível.
    - Debita remetente e credita destinatário com controle de concorrência.
4. Consulta paginada de pagamentos
    - O cliente faz uma requisição GET para /api/v1/pix/payments, podendo informar filtros (status, senderPixKey, receiverPixKey) e parâmetros de paginação (page, size).
    - A API valida os parâmetros e executa a consulta paginada no banco de dados.
    - A resposta retorna uma lista paginada de pagamentos, com os campos content, totalElements, totalPages, size e number.
5. Evento publicado na fila
6. Processor consome evento
7. Simula SPI
8. Atualiza status
