# Fly Away Travel — CS2031 Week 07

Implementación de las 6 misiones del laboratorio.

## Requisitos

- Java 21+ (el README original sugiere Java 26)
- Maven
- PostgreSQL
- Docker opcional

## Ejecutar PostgreSQL

```bash
docker compose up -d postgres
```

Luego:

```bash
mvn spring-boot:run
```

La API queda en `http://localhost:8080`.

## Endpoints

- `POST /users/register`
- `POST /auth/login`
- `POST /flights/create`
- `GET /flights/search` — JWT
- `POST /flights/book` — JWT
- `GET /flight/book/{id}` — JWT

## Postman

Usa los archivos originales del laboratorio:

```text
tests/postman/Fly-Away-Travel.postman_collection.json
tests/postman/Fly-Away-Travel.postman_environment.json
```

Ejecuta toda la colección en orden.

## Confirmación

Cada reserva genera:

```text
flight_booking_email_<booking_id>.txt
```

en el directorio de ejecución de la aplicación.

## Docker

Primero construye el JAR:

```bash
mvn clean package
```

Después:

```bash
docker compose up --build
```
