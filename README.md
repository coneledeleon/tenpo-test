# Prueba Técnica Tenpo 2026

## 1. Descripción

API REST desarrollada en Spring Boot que calcula un porcentaje adicional sobre la suma de dos números. Utiliza Resilience4j para manejar rate limiting y reintentos automáticos, y persiste el historial de solicitudes en PostgreSQL.

Challenge técnico para optar al cargo de Desarrollador Backend en Tenpo.

## 2. Requisitos Previos

- **Docker y Docker Compose** ([Como instalar](https://docs.docker.com/compose/install/))

## 3. Ejecución Local

Para levantar el proyecto, solo necesitas asignar permisos de ejecución y correr el script `run-compose.sh`

```bash
wget https://raw.githubusercontent.com/coneledeleon/tenpo-test/refs/heads/develop/run-compose.sh && chmod +x run-compose.sh && ./run-compose.sh
```

Esto levantará:
- API: `http://localhost:5001`
- PostgreSQL: `localhost:5432`

## 4. Endpoints

### GET /api/v1/percents

Calcula el porcentaje adicional sobre la suma de dos números.

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `num1` | Query (String) | Primer número (debe ser positivo) |
| `num2` | Query (String) | Segundo número (debe ser positivo) |

**Respuesta:**
```json
{
  "num1": "100",
  "num2": "200",
  "appliedPercent": "0.15",
  "result": "345.00",
  "timestamp": "2026-05-08T14:30:00"
}
```
---

### GET /api/v1/percents/history

Obtiene el historial de solicitudes con paginación.

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `page` | Query (Integer) | Número de página (inicia en 1, opcional) |
| `size` | Query (Integer) | Registros por página (opcional, default: 10) |

**Respuesta:**
```json
{
  "history": [
    {
      "timestamp": "2026-05-08T14:30:00",
      "endpoint": "/api/v1/percents",
      "params": { "num1": "100", "num2": "200" },
      "statusCode": 200,
      "response": { ... },
      "duration": 125
    }
  ],
  "pagination": {
    "currentPage": 1,
    "pageSize": 10,
    "totalPages": 5,
    "totalElements": 50
  }
}
```

---

Al levantar la aplicación, la documentación interactiva está disponible en:

- **Swagger UI**: [http://localhost:5001/swagger-ui.html](http://localhost:5001/swagger-ui.html)
- **OpenAPI (JSON)**: [http://localhost:5001/v3/api-docs](http://localhost:5001/v3/api-docs)

## 5. Cómo Probar

Al ser endpoints GET sin autenticación, pueden probarse directamente desde el navegador, o mediante CURL:

### Endpoint de cálculo

```bash
curl -s "http://localhost:5001/api/v1/percents?num1=100&num2=200" | jq
```

### Endpoint de historial

```bash
curl -s "http://localhost:5001/api/v1/percents/history?page=1&size=10" | jq
```

> **Nota**: El flag `jq` permite formatear la respuesta JSON de forma legible. Si no dispones de `jq`, puedes omitirlo y la respuesta se mostrará en una sola línea.

También puedes acceder a la documentación interactiva en Swagger UI para probar los endpoints desde la interfaz.

## 6. Configuración

La aplicación se configura mediante variables de entorno. Estas pueden definirse en un archivo `.env` o pasarse directamente al contenedor.

| Variable | Descripción | Default |
|----------|-------------|---------|
| `DB_HOST` | Host de PostgreSQL | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base de datos | `tenpo` |
| `DB_SCHEMA` | Esquema de la base de datos | `tenpo` |
| `DB_USER` | Usuario de PostgreSQL | `fabio` |
| `DB_PASS` | Contraseña de PostgreSQL | `******` |

El archivo de configuración principal se encuentra en: `src/main/resources/application.yaml`

## 7. Características

- **Rate Limiting**: Límite de 3 solicitudes por minuto por cliente (configurable via Resilience4j)
- **Reintentos Automáticos**: 3 intentos con espera de 2 segundos ante fallos del servicio externo
- **Historial de Solicitudes**: Persiste todas las peticiones en PostgreSQL con paginación
- **Logging de Requests/Responses**: Mediante Spring AOP, registra parámetros, respuesta y duración de cada solicitud
- **Documentación Interactiva**: Swagger UI y OpenAPI 3 disponibles
- **Manejo de Errores**: GlobalExceptionHandler con códigos de error estandarizados

## 8. Stack Tecnológico

- **Spring Boot 4.0.6** - Framework principal
- **Java 21** - Lenguaje
- **PostgreSQL 16** - Base de datos
- **Spring Data JPA / Hibernate** - ORM
- **Spring AOP** - Programación orientada a aspectos
- **Resilience4j** - Rate limiting y reintentos
- **Springdoc OpenAPI** - Documentación Swagger
- **Lombok** - Reducción de boilerplate
- **Docker / Docker Compose** - Contenedores
- **Maven** - Gestión de dependencias

## 9. Autor

**Fabio Geissbuhler Alarcón**
- GitHub: [https://github.com/coneledeleon](https://github.com/coneledeleon)
- LinkedIn: [https://www.linkedin.com/in/fabiogeissbuhler](https://www.linkedin.com/in/fabiogeissbuhler)
- Email: gssbhler.code@gmail.com