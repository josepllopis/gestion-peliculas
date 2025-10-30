# Gestión de Películas

API REST para la gestión de películas desarrollada con **Spring Boot**, **MySQL** y **Docker**.

---

##  Características

- CRUD completo de películas
- Base de datos MySQL con Docker
- Documentación de API con Swagger
- Configuración mediante `docker-compose`

---

##  Requisitos

- Java 17+
- Maven
- Docker & Docker Compose

---

##  Ejecutar con Docker

```bash
docker-compose build

docker-compose up
```

## Ejecutar localmente

1. Crear base de datos MySQL:
2. Configurar `application.properties` con tus credenciales.
3. Ejecutar la aplicación: mvn spring-boot:run

- API disponible en: http://localhost:8096
- MySQL disponible en: localhost:3307

---

## Documentación

Swagger UI: http://localhost:8096/swagger-ui/index.html

---

## Endpoints

| Método | Endpoint                  | Descripción                                                                    |
|--------|---------------------------|--------------------------------------------------------------------------------|
| GET    | /api/peliculas            | Obtener todas las películas                                                   |
| GET    | /api/peliculas/{id}       | Obtener una película por ID                                                   |
| POST   | /api/peliculas            | Crear nueva película                                                         |
| PUT    | /api/peliculas/{id}       | Actualizar película                                                          |
| DELETE | /api/peliculas/{id}       | Eliminar película                                                            |
| GET    | /api/films/sorted         | Lista de películas ordenadas por las variables `sortBy` y `direction`        |
| GET    | /api/films/pdf            | Descargar PDF con tabla de todas las películas, ordenadas por `sortBy` y `direction` |


---

## Autor

Josep Llopis – [GitHub](https://github.com/josepllopis)

---

## Licencia

MIT





