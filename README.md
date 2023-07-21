# Loja E-commerce ShirtVirtual

Este projeto é uma aplicação de loja de Ecommerce desenvolvida em Java 11, utilizando o banco de dados PostgreSQL e documentada com Swagger. Além disso, a API pode ser testada e explorada através do Postman.

## Requisitos

Certifique-se de ter as seguintes ferramentas instaladas em seu ambiente de desenvolvimento:
 - [ ] Java 11 ou superior
 - [ ]  PostgreSQL 9.5.2
 - [ ] Postman (ou similar) para realizar teste de requisições da API

## Configuração do Banco de Dados

Certifique-se de ter as seguintes ferramentas instaladas em seu ambiente de desenvolvimento:
 1. Instale o PostgreSQL em sua máquina e certifique-se de que o serviço esteja em execução. 
 2. Crie um banco de dados chamado "BDESSH01" usando o cliente PostgreSQL de sua preferência.
 3. Certifique-se de que todas as dependências do projeto tenham sido baixadas corretamente.
 4. As configurações de conexão do banco de dados podem ser ajustadas no arquivo application-dev.properties na pasta src/main/resources.

```springboot
spring.datasource.url = jdbc:postgresql://localhost:5432/DBDESSH01?autoreconnect=true
spring.datasource.username = SeuUsuario
spring.datasource.password = SuaSenha
spring.jpa.hibernate.ddl-auto = none
spring.sql.init.platform = postgres
spring.datasource.driver-class-name = org.postgresql.Driver
spring.jpa.show-sql = true
```

## Executando a Aplicação
 1. Clone este repositório para o seu ambiente local. [REPOSITORIO](https://github.com/tiagofrancarita/backend-loja-virtual-shirt) 
 2. Abra o projeto em uma IDE java de sua preferência.
 3. Certifique-se de que todas as dependências do projeto tenham sido baixadas corretamente.
 4. Execute a aplicação através do método `main` na classe principal `Application.java`.




## Documentação da API
A API da loja de Ecommerce está documentada usando o Swagger, o que facilita a visualização e teste dos endpoints disponíveis. 
Para acessar a documentação da API, inicie a aplicação e abra o seguinte link em seu navegador web:

```bash
  http://localhost:8080/swagger-ui.html
```


## Testando a API
Você pode testar a API utilizando o Postman (ou uma ferramenta similar) para enviar requisições HTTP aos endpoints da aplicação.
 1. Abra o Postman em seu computador.
 2. Importe a coleção de requisições disponível em `postman/loja_ecommerce.postman_collection.json`.
 3. Com a aplicação em execução, envie as requisições para testar os diferentes endpoints da API.


## Contribuindo

Contribuições são bem-vindas! Se você encontrar algum problema ou tiver sugestões para melhorias, sinta-se à vontade para abrir uma issue ou enviar um pull request.


## Licença
Este projeto está licenciado sob a licença MIT. Sinta-se livre para usar, modificar e distribuir o código conforme as diretrizes desta licença. [MIT](https://choosealicense.com/licenses/mit/)


## Etiquetas

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
![Logo](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/th5xamgrr6se0x5ro4g6.png)


## Variáveis de Ambiente

Para rodar esse projeto, você vai precisar adicionar as seguintes variáveis de ambiente no seu .env

`API_KEY`

`ANOTHER_API_KEY`
