Decisões arquiteturais.

- EJB Stateless para lógica de negócio de transferências com controle de concorrência usando synchronized.
- Filtros e paginação implementados nos endpoints de consulta (/api/v1/pix/payments) para garantir performance e flexibilidade na busca de pagamentos.
- Page/Pageable do Spring Data para integração nativa com o banco e respostas padronizadas.
- A separação entre CRUD de benefícios (/api/v1/benefits) e processamento de transferências facilita manutenção e evolução futura do sistema.
- Scripts SQL em /scripts executados automaticamente na primeira inicialização do container PostgreSQL via docker-compose.
- Testes automatizados (48 testes: 32 EJB + 16 API) executados em containers dedicados para garantir qualidade.