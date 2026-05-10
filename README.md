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

## 9. Decisiones de Diseño

1. Se construye un único controller, no por la baja cantidad de endpoints, sino porque todos los endpoints están asociados a una mismo concepto: la consulta de porcentajes.
2. Aún cuando se construye un único endpoint, se privilegia el control global de excepciones mediante ControllerAdvice y no con ExceptionHandler, porque el costo de implementación es basicamente el mismo, y ControllerAdvice permite añadir nuevos Controllers al proyecto, si necesidad de re implementar control de errores.
3. Todos los endpoints construidos operan con método GET. Aún cuando la "consulta de porcentaje" da origen a un registro en BD, esto es una consuecuencia de la consulta, el método dispuesto no tiene como finalidad la creación de registros de un recurso.
4. La implementación de RateLimit y Reintentos se hizo con Resilience4j, porque tiene el manejo resuelto de muy buena manera. Para ambos casos, los parámetros de configuración no fueron configurados para ser obtenidos desde variables de entorno, porque no lo consideré necesario.
5. La implementación de registro histórico de solicitudes se hace usando SpringAOP y no un filtro, por la necesidad de que la persistencia sea asincrona, y los componentes en el FilterChain no permiten asincronía de forma orgánica. De todas maneras la información de la solicitud se obtiene desde el RequestContextHolder.
6. La implementación de obtención de historial de solicitudes tiene parámetros de paginación no requeridos como parte del request, pero en caso de no venir, se asume paginación por defecto, para evitar que se pueda sobrecargar el endpoint con consultas no paginadas.
7. Si bien el ejercicio pedía explicitamente que el registro de solicitudes y sus respuestas fuera para "todas las llamadas realizadas a los endpoints de la API", decidí no implementar registro de las solicitudes a la obtención de historial de solicitudes, por un tema de diseño: Si encadeno N consultas de historial, se iba a empezar a generar data de respuesta recursiva que en poco tiempo iba a exceder el largo permitido para el campo. Esto obligaría a dejar el campo sin un largo límite, y esto es una mala práctica desde la perspectiva del diseño de modelos de datos.
8. Si bien el ejercicio no lo pedía, el registro histórico de consultas incluye el status y tiempo de respuesta, para tener más información sobre el resultado.
9. Sobre la implementación de docker compose, y por la forma de estructurar el proyecto, se decidió delegar a Spring la creación del modelo, esto para simplificar el ejercicio.
10. Sobre la base de datos, se decidió no añadir índices o constraints adicionales, porque la operación de la API no lo requiere: no hay consultas parametrizables, por la naturaleza del registro no se identifican llaves de unicidad claras, así que el esquema no se complejizó innecesariamente.
11. Los test unitarios se agregan solo sobre componentes @Service, porque la idea de los test unitarios es validar funcionalidades que reflejen lógica de negocio, de forma aislada. Para todas estas piezas se establecen casos de éxito y de fallo, para validar que las decisiones se estén controlando de forma esperada.
12. Se escoge el puerto 5001 como puerto de exposición de la API, para evitar colisiones con puertos regularmente utilizados por otros servicios conocidos, al levantar la app.
13. El servicio externo de consulta de porcentaje se implementó como un mock funcional solo en profile "dev". La aplicación se levanta en este profile y todo funciona. Para cambiar el profile es necesaria una nueva implementación de esta interfaz, que apunte a un servicio externo real.
14. Sobre el endpoint de consulta de porcentaje, inicialmente decidí que los parámetros num1 y num2 se pasaran como @PathVariables, me parece más claro, y le da caracter de "no opcional" a los valores, creo que era una mejor opción, pero como la solicitud pedía "registro de parámetros", asumí que se esperaba que estos valores fueran @RequestParam, y lo abordé de esa forma. Preferí ajustarme "a la definición".

## 10. Autor

**Fabio Geissbuhler Alarcón**
- GitHub: [https://github.com/coneledeleon](https://github.com/coneledeleon)
- LinkedIn: [https://www.linkedin.com/in/fabiogeissbuhler](https://www.linkedin.com/in/fabiogeissbuhler)
- Email: gssbhler.code@gmail.com