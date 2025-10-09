# 🏥 Clínica Veterinária API

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=flat&logo=docker)](https://www.docker.com/)
[![Azure](https://img.shields.io/badge/Azure-Container%20Apps-0078D4?style=flat&logo=microsoftazure)](https://azure.microsoft.com/)

Sistema de microserviços para gerenciamento de clínica veterinária, desenvolvido com Spring Boot e hospedado no Azure Container Apps.

## 🌐 Acesso ao Sistema

A aplicação está disponível em:
```
https://clinica-veterinaria-api.salmonground-c4514ff0.brazilsouth.azurecontainerapps.io
```

## 📋 Pré-requisitos

- **Java 17**
- **Docker** (opcional, para containerização)
- **PostgreSQL** (para banco de dados)

## ⚙️ Configuração

### Variáveis de Ambiente para clinica-veterinaria-api

| Variável                  | Valor Padrão           | Descrição                    |
|---------------------------|------------------------|------------------------------|
| `SERVER_PORT`             | `1515`                 | Porta do servidor            |
| `RABBITMQ_USER`           |                        | Usuario do RabbitMQ          |
| `RABBITMQ_PASSWORD`       |                        | Senha                        |
| `RABBITMQ_QUEUE`          | default.email          | Fila do RabbitMQ             |
| `RABBITMQ_QUEUE_RESPONSE` | default.email.response | Fila de resposta do RabbitMQ |

### Variáveis de Ambiente para servico-email

| Variável  | Valor Padrão | Descrição                  |
|--------------|-------------|----------------------------|
| `RABBITMQ_USER` |             | Usuario do RabbitMQ        |
| `RABBITMQ_PASSWORD` |             | Senha                      |
| `RABBITMQ_QUEUE`          | default.email          | Fila do RabbitMQ             |
| `RABBITMQ_QUEUE_RESPONSE` | default.email.response | Fila de resposta do RabbitMQ |
| `SMTP_HOST`  |     sandbox.smtp.mailtrap.io        | Host do servidor de e-mail |
| `SMTP_PORT`  |      2525       | Porta                      |
| `SMTP_USERNAME` |             | Usuario                    |
| `SMTP_PASSWORD` |             | Senha                      |
| `DB_HOST`                 | `127.0.0.1`            | Host do banco de dados       |
| `DB_PORT`                 | `5432`                 | Porta do PostgreSQL          |
| `DB_USER`                 | `pguser`               | Usuário do banco             |
| `DB_PASSWORD`             | `12345`                | Senha do banco               |


## 🚀 Executando o Projeto

### Execução Local

1. Navegue até a pasta do serviço desejado:
```bash
cd [nome-do-servico]
```

2. Execute o mvnw (Maven Wrapper):
```bash
./mvnw spring-boot:run
```

3. Ou execute a aplicação com as variáveis de ambiente personalizadas seguindo o exemplo abaixo:
```bash
SERVER_PORT=1515 DB_HOST=127.0.0.1 DB_PORT=5432 DB_USER=pguser DB_PASSWORD=12345 ./mvnw spring-boot:run
```

## 🐳 Docker

### Construindo a Imagem

1. Compile e empacote o projeto:
```bash
./mvnw clean package
```

2. Construa a imagem Docker:
```bash
docker build -t proway-upskilling/clinica-veterinaria-api:0.1 .
```

### Executando o Container

Execute o container com as variáveis de ambiente necessárias:
```bash
docker run -p 1515:1515 \
  -e SERVER_PORT=1515 \
  -e DB_PORT=5432 \
  -e DB_USER=pguser \
  -e DB_PASSWORD=12345 \
  proway-upskilling/clinica-veterinaria-api:0.1
```

## 🏗️ Arquitetura

Este projeto segue uma arquitetura de microserviços, permitindo escalabilidade e manutenção independente de cada módulo.
