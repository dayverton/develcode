## Configuração das Aplicações Java e NodeJS com Docker

As aplicações **Java** e **NodeJS** foram configuradas via **Docker** para acessar o **RabbitMQ** e **MongoDB** rodando no host local.

## Requisitos Necessários

- **Docker**, **MongoDB** e **RabbitMQ** precisam estar instalados e configurados na sua máquina local.
- O **MongoDB** deve estar rodando na porta padrão `27017`.
- O **RabbitMQ** deve estar rodando na porta padrão `5672`, com as credenciais padrão `guest/guest`.

## Construir as Imagens e Rodar os Containers

Do diretório raiz execute os seguintes comandos no terminal para construir as imagens e iniciar os containers:

```bash
cd develcode\java\checkout
docker-compose up --build

cd..
cd..

cd develcode\node\api-gateway
docker-compose up --build
```
## Requisitos Necessários

A porta `8080` foi exposta para permitir acessar a aplicação Java através do **Swagger**. Acesse a API pelo seguinte endereço:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Importante: Simulação de Pagamento

Na aplicação **api-gateway**, foi implementado um mock para simular o pagamento. A lógica da simulação é a seguinte:

-   Se o valor for **par**, o pagamento será **aprovado**.
-   Se o valor for **ímpar**, o pagamento será **rejeitado**.
