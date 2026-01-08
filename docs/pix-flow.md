1. Cliente envia solicitação PIX
    - O cliente faz uma requisição POST para /api/v1/pix/payments, enviando os dados do pagamento.
2. API valida e persiste
    - A API valida os dados recebidos e persiste o pagamento no banco de dados.
3. Consulta paginada de pagamentos
    - O cliente faz uma requisição GET para /api/v1/pix/payments, podendo informar filtros (status, senderPixKey, receiverPixKey) e parâmetros de paginação (page, size).
    - A API valida os parâmetros e executa a consulta paginada no banco de dados.
    - A resposta retorna uma lista paginada de pagamentos, com os campos content, totalElements, totalPages, size e number.
3. Evento publicado na fila
4. Processor consome evento
5. Simula SPI
6. Atualiza status
