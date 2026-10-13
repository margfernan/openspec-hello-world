## ADDED Requirements

### Requirement: Saludo por defecto
El sistema SHALL exponer `GET /hello` y, si no recibe el parámetro `name`, SHALL responder `200 OK` con `Content-Type: application/json` y el cuerpo `{"message": "Hello, World!"}`.

#### Scenario: Petición sin parámetros
- **WHEN** un cliente envía `GET /hello` sin parámetros
- **THEN** el sistema responde `200 OK` con `Content-Type: application/json`
- **AND** el cuerpo es `{"message": "Hello, World!"}`

### Requirement: Saludo personalizado
Cuando recibe un parámetro de query `name` válido, el sistema SHALL responder `200 OK` con el cuerpo `{"message": "Hello, <name>!"}`, donde `<name>` es el valor recibido sin espacios al inicio ni al final.

#### Scenario: Nombre provisto
- **WHEN** un cliente envía `GET /hello?name=Ana`
- **THEN** el sistema responde `200 OK`
- **AND** el cuerpo es `{"message": "Hello, Ana!"}`

#### Scenario: Nombre con espacios alrededor
- **WHEN** un cliente envía `GET /hello?name=%20%20Ana%20%20`
- **THEN** el sistema responde `200 OK`
- **AND** el cuerpo es `{"message": "Hello, Ana!"}`

#### Scenario: Nombre con caracteres especiales
- **WHEN** un cliente envía `GET /hello` con `name` igual a `<b>Ana</b>`
- **THEN** el sistema responde `200 OK` con `Content-Type: application/json`
- **AND** el campo `message` contiene el nombre tal cual, como string JSON correctamente codificado: `Hello, <b>Ana</b>!`

### Requirement: Nombre vacío usa el valor por defecto
Si el parámetro `name` está vacío o solo contiene espacios, el sistema SHALL tratarlo como ausente y responder con el saludo por defecto.

#### Scenario: Nombre vacío
- **WHEN** un cliente envía `GET /hello?name=`
- **THEN** el sistema responde `200 OK`
- **AND** el cuerpo es `{"message": "Hello, World!"}`

#### Scenario: Nombre solo con espacios
- **WHEN** un cliente envía `GET /hello?name=%20%20%20`
- **THEN** el sistema responde `200 OK`
- **AND** el cuerpo es `{"message": "Hello, World!"}`

### Requirement: Longitud máxima del nombre
El sistema SHALL aceptar nombres de hasta 50 caracteres (medidos después de recortar los espacios) y SHALL rechazar los más largos con `400 Bad Request`.

#### Scenario: Nombre en el límite
- **WHEN** un cliente envía `GET /hello` con un `name` de exactamente 50 caracteres
- **THEN** el sistema responde `200 OK` con el saludo personalizado

#### Scenario: Nombre demasiado largo
- **WHEN** un cliente envía `GET /hello` con un `name` de 51 caracteres
- **THEN** el sistema responde `400 Bad Request`

#### Scenario: Espacios no cuentan para el límite
- **WHEN** un cliente envía `GET /hello` con un `name` de 50 caracteres rodeado de espacios
- **THEN** el sistema responde `200 OK` con el saludo personalizado usando el nombre recortado