# clinica-veterinaria-api
Api da clínica veterinária.

# Requisitos

Java 17
Maven

# Variáveis de ambiente

SERVER_PORT=1515
DB_HOST=127.0.0.1
DB_PORT=5432
DB_USER=pguser
DB_PASSWORD=12345

# Executar

```
./mvnw spring-boot:run
```

```
SERVER_PORT=1515 DB_HOST=127.0.0.1 DB_PORT=5432 DB_USER=pguser DB_PASSWORD=12345 ./mvnw spring-boot:run
```

# Docker

Compilar e montar pacote .jar
```
./mvnw clean package
```

Construir imagem docker
```
sudo docker build -t proway-upskilling/clinica-veterinaria-api:0.1 .
```

Executar:
```
sudo docker run -p 1515:1515 -e SERVER_PORT=1515 proway-upskilling/clinica-veterinaria-api:0.1
```
