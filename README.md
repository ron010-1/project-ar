# Payment API

API REST para cadastro de pedidos e integração com o Mercado Pago para geração e consulta de pagamentos.

## Arquitetura

```
controller/   endpoints REST (Pedido, Pagamento)
service/      regras de negocio
repository/   acesso a dados (Spring Data JPA)
entity/       entidades JPA (Pedido, Pagamento, StatusPagamento)
dto/          objetos de entrada/saida da API
gateway/      cliente HTTP para a API REST do Mercado Pago
exception/    excecoes de dominio e tratamento global de erros
config/       configuracao de propriedades e OpenAPI
```

Banco de dados: H2 em memória (via Spring Data JPA), sem necessidade de instalação.

## Fluxo de integração

1. `POST /api/pedidos` cadastra um pedido (descrição + valor).
2. `POST /api/pagamentos` gera uma preferência de pagamento no Mercado Pago (`/checkout/preferences`) usando o pedido como `external_reference` e retorna a URL de checkout (`checkoutUrl`).
3. O pagamento é feito na página do Mercado Pago (sandbox).
4. `GET /api/pagamentos/{id}/status` consulta a API do Mercado Pago (`/v1/payments/search`) filtrando pelo `external_reference` do pedido, atualiza e retorna o status atual (`PENDENTE`, `APROVADO`, `RECUSADO`, `CANCELADO`).
5. `GET /api/pagamentos` lista o histórico de pagamentos já registrados.

## Como executar

Pré-requisitos: JDK 21+ e Maven 3.9+ instalados.

1. Crie uma conta de desenvolvedor no Mercado Pago e obtenha um Access Token de teste (sandbox) em https://www.mercadopago.com.br/developers.
2. Exporte a variável de ambiente:
   ```
   set MERCADOPAGO_ACCESS_TOKEN=SEU_TOKEN_DE_TESTE
   ```
3. Suba a aplicação:
   ```
   mvn spring-boot:run
   ```
4. A API sobe em `http://localhost:8080`.

## Documentação da API

Swagger UI: `http://localhost:8080/swagger-ui.html`
OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | /api/pedidos | Cadastrar pedido |
| GET | /api/pedidos | Listar pedidos |
| GET | /api/pedidos/{id} | Buscar pedido por id |
| POST | /api/pagamentos | Gerar pagamento (body: `{ "pedidoId": 1 }`) |
| GET | /api/pagamentos/{id}/status | Consultar status atual no gateway |
| GET | /api/pagamentos | Histórico de pagamentos |

## Slide de apresentação

`docs/apresentacao.html` — abra direto no navegador (setas do teclado ou botões no canto inferior direito para navegar entre os slides).

## Testes manuais

A coleção do Postman está em `postman/project-ar.postman_collection.json`.

## Console H2

`http://localhost:8080/h2-console` — JDBC URL: `jdbc:h2:mem:paymentdb`, usuário `sa`, senha em branco.
