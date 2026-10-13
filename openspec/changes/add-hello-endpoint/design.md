## Context

`hello-world2` es un proyecto Spring Boot 4.0.8 (Java 17) recién generado con Spring Initializr. Solo tiene la clase `HelloWorld2Application` y el test `contextLoads()`. Ya trae `spring-boot-starter-webmvc` (que incluye Jackson para JSON) y `spring-boot-starter-webmvc-test` (MockMvc). Este es el primer endpoint y el primer cambio que pasa por OpenSpec.

## Goals / Non-Goals

**Goals:**
- Implementar `GET /hello` según la spec `hello-greeting`.
- Dejar la lógica del saludo en un lugar que se pueda testear sin levantar todo el contexto.
- Cubrir cada escenario de la spec con al menos un test.

**Non-Goals:**
- Internacionalización (el saludo es siempre `Hello, ...!`).
- Otros métodos HTTP (`POST`, etc.) o variantes de ruta como `/hello/{name}`.
- Autenticación, rate limiting o persistencia.
- Definir un formato de error propio para el `400`; alcanza con el que da Spring por defecto.

## Decisions

### 1. JSON (`{"message": ...}`) en vez de texto plano
Un `record GreetingResponse(String message)` serializado por Jackson.
- **Por qué**: deja la puerta abierta para agregar campos sin romper a los clientes, y Jackson se encarga del escape de caracteres especiales (`<`, `"`, etc.), así que el escenario de caracteres especiales sale gratis.
- **Alternativa**: `text/plain` con un `String`. Es más simple, pero cambiar el formato después sería un cambio incompatible.

### 2. Query param (`?name=`) en vez de path variable (`/hello/{name}`)
- **Por qué**: el nombre es opcional y los query params representan bien lo opcional. Con path variable harían falta dos mappings (`/hello` y `/hello/{name}`).
- **Alternativa**: path variable. Se descarta por la duplicación de rutas.

### 3. Normalización y validación en un servicio, no en el controlador
- `GreetingService.greet(String rawName)`: recorta el valor, aplica el default `World` si queda vacío o es `null`, valida la longitud (≤ 50) y arma el mensaje.
- `HelloController` recibe `@RequestParam(required = false) String name`, delega en el servicio y devuelve el record.
- **Por qué**: la regla "recortar y después validar" es lógica de dominio. En un servicio se testea con tests unitarios puros, y el controlador queda como adaptador HTTP.
- **Alternativa**: Bean Validation (`@Size(max = 50)`) en el parámetro. Se descarta porque valida antes del recorte: un nombre de 50 caracteres rodeado de espacios sería rechazado, y eso contradice la spec.

### 4. El `400` se devuelve desde un `@ExceptionHandler` local
Si el nombre es demasiado largo, el servicio lanza una excepción de dominio (`InvalidNameException`). Un `@ExceptionHandler` con `@ResponseStatus(BAD_REQUEST)` en el controlador la traduce y devuelve un `ProblemDetail` (RFC 9457).
- **Por qué**: así el servicio no depende de HTTP y el mapeo a estado HTTP queda en la capa web.
- **Alternativa**: lanzar `ResponseStatusException` directo desde el servicio. Es más corto, pero acopla el dominio a Spring Web.
- **Nota (implementación)**: la primera versión lanzaba `ResponseStatusException` *desde* el handler. Se descartó porque Spring MVC no vuelve a resolver las excepciones que lanza un `@ExceptionHandler`, y el resultado sería un `500`.

### 5. Estrategia de tests
- **Unitarios** (`GreetingServiceTest`, JUnit puro): default, recorte, vacío, solo espacios, límite de 50, 51 caracteres.
- **Slice web** (`HelloControllerTest`, `@WebMvcTest` + MockMvc): verifica estado HTTP, `Content-Type` y cuerpo JSON de cada escenario de la spec, incluido el de caracteres especiales.
- El test `contextLoads()` existente se mantiene.

## Risks / Trade-offs

- [El límite de 50 es arbitrario] → Queda en una constante con nombre (`MAX_NAME_LENGTH`), así es fácil de cambiar. Si alguna vez se necesita configurarlo, se puede pasar a `application.properties`.
- [Cuerpo del `400` sin formato definido] → Es aceptable para este alcance. Si más adelante hace falta un formato estable, se puede activar `ProblemDetail` (`spring.mvc.problemdetails.enabled=true`) en otro cambio.
- [Se cuentan `char` de Java, no caracteres Unicode reales] → Un emoji o un carácter fuera del BMP cuenta como 2. Se acepta para este ejercicio y no se lleva a la spec.