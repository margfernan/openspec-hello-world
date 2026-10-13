## 1. Lógica de dominio

- [x] 1.1 Crear `record GreetingResponse(String message)` en `com.example.hello_world2`
- [x] 1.2 Crear `InvalidNameException` (excepción de dominio, sin dependencias de Spring Web)
- [x] 1.3 Crear `GreetingService` con `greet(String rawName)`: recortar, aplicar el default `World` si es `null` o queda vacío, validar `MAX_NAME_LENGTH = 50` (lanza `InvalidNameException`) y devolver `Hello, <name>!`
- [x] 1.4 Escribir `GreetingServiceTest` (JUnit puro): `null`, nombre normal, recorte de espacios, vacío, solo espacios, exactamente 50, 51, 50 con espacios alrededor

## 2. Capa web

- [x] 2.1 Crear `HelloController` con `GET /hello` y `@RequestParam(required = false) String name` que delegue en `GreetingService` y devuelva `GreetingResponse` como JSON
- [x] 2.2 Agregar un `@ExceptionHandler(InvalidNameException.class)` en el controlador que traduzca a `400 Bad Request`
- [x] 2.3 Escribir `HelloControllerTest` con `@WebMvcTest` + MockMvc que cubra cada escenario de `specs/hello-greeting/spec.md`: sin parámetros, `name=Ana`, nombre con espacios, `<b>Ana</b>`, vacío, solo espacios, 50 caracteres, 51 caracteres (400), 50 con espacios alrededor. Verificar estado HTTP, `Content-Type: application/json` y cuerpo

## 3. Verificación

- [x] 3.1 Ejecutar `./mvnw test` y confirmar que pasan todos los tests (incluido `contextLoads()`)
- [x] 3.2 Levantar la app (`./mvnw spring-boot:run`) y probar a mano `GET /hello`, `GET /hello?name=Ana` y un nombre de 51 caracteres
