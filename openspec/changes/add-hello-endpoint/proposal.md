## Why

La aplicación es un esqueleto de Spring Boot sin ningún endpoint: arranca, pero no expone comportamiento observable. Un endpoint `GET /hello` le da a la app su primera funcionalidad y es un caso chico, con reglas explícitas, para practicar el flujo de spec-driven development de punta a punta.

## What Changes

- Nuevo endpoint HTTP `GET /hello` que devuelve un saludo en JSON (`{"message": "Hello, World!"}`).
- Parámetro de query opcional `name` para personalizar el saludo (`GET /hello?name=Ana` → `{"message": "Hello, Ana!"}`).
- Reglas para entradas límite: un `name` vacío o compuesto solo por espacios usa el valor por defecto `World`; los espacios al inicio y al final se recortan; un `name` de más de 50 caracteres se rechaza con `400 Bad Request`.
- Tests que cubren cada escenario de la spec.

## Capabilities

### New Capabilities
- `hello-greeting`: Endpoint HTTP de saludo, con nombre opcional, valor por defecto, normalización de la entrada y validación de longitud.

### Modified Capabilities
<!-- Ninguna: openspec/specs/ está vacío -->

## Impact

- **Código**: nuevo controlador REST en `com.example.hello_world2` y nuevos tests.
- **API**: se agrega `GET /hello`. No hay endpoints previos, así que no hay cambios incompatibles.
- **Dependencias**: ninguna nueva; `spring-boot-starter-webmvc` y `spring-boot-starter-webmvc-test` ya están en el `pom.xml`.