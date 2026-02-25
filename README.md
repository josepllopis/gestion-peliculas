# 🎬 Gestión de Películas

Aplicación web para gestionar y registrar las películas que vas viendo. Cada usuario tiene su propia lista personalizada, puede puntuar sus películas y competir en un ranking local.

---

## 📋 Descripción

**Gestión de Películas** es una API REST desarrollada con Spring Boot que permite a múltiples usuarios llevar un registro de las películas que han visto. La aplicación incluye autenticación con Spring Security, gestión completa de películas (CRUD) y un ranking de usuarios según el número de películas vistas.

La aplicación está desplegada con Docker y corre de forma continua en un servidor local, permitiendo que otros usuarios de la red se conecten a través de la IP del host.

---

## ✨ Funcionalidades

- **Autenticación de usuarios** — 4 usuarios preconfigurados con login mediante Spring Security (Consultar DataInitializer).
- **Ordenación dinámica** — Ordena tu lista de películas por cualquier parámetro (nombre, puntuación, fecha, duración, director...) en orden ascendente o descendente.
- **Exportación a PDF** — Descarga tu historial de películas en formato PDF como tabla lista para compartir o guardar.
- **Gestión de películas (CRUD)** — Añadir, ver, editar y eliminar películas de tu lista personal.
- **Registro detallado** — Cada película incluye los siguientes campos:
  - Nombre
  - País de origen
  - Cine o plataforma donde se vio
  - Fecha de visualización
  - Duración
  - Puntuación
  - Director
- **Documentación interactiva con Swagger** — Todos los endpoints documentados y probables directamente desde el navegador en `/swagger-ui.html`.
- **Ranking local** — Pantalla con el ranking de usuarios según el número de películas vistas.
- **Lista por usuario** — Haciendo clic en cualquier usuario del ranking se puede ver su lista completa de películas.
- **Acceso en red local** — Otros usuarios pueden conectarse a través de la IP del servidor.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.5.6 | Framework de la aplicación |
| Spring Security + BCrypt | — | Autenticación con contraseñas hasheadas |
| Spring Data JPA | — | Capa de acceso a datos |
| Hibernate | 6 | ORM / mapeo objeto-relacional |
| MySQL | 8.0 | Base de datos relacional |
| Docker | — | Contenedorización |
| Docker Compose | — | Orquestación de contenedores |
| Swagger / OpenAPI | 3 | Documentación interactiva de la API |
| Maven | 3.9 | Gestión de dependencias y build |
| GitHub Actions | — | CI/CD — build y push automático a Docker Hub |

---

## 🏗️ Arquitectura del proyecto

El proyecto sigue una arquitectura en capas bien definida:

- **Controller** — Exposición de endpoints REST y gestión de peticiones HTTP.
- **Service** — Lógica de negocio.
- **Repository** — Acceso a base de datos mediante Spring Data JPA.
- **DTO + Mapper** — Separación entre la capa de persistencia y la API, evitando exponer las entidades directamente.
- **Config** — Configuración de Spring Security y otros beans.
- **Utils** — Clases de utilidad transversales.
- **@ControllerAdvice** — Manejo global de excepciones con respuestas de error consistentes.

---

## 🚀 Despliegue con Docker

### Requisitos previos

- [Docker](https://www.docker.com/) instalado
- [Docker Compose](https://docs.docker.com/compose/) instalado

### Pasos

**1. Descarga el `docker-compose.yml`** del repositorio.

**2. Levanta los contenedores:**

```bash
docker compose up -d
```

Esto descargará automáticamente la imagen de Docker Hub y la imagen oficial de MySQL, y los levantará correctamente conectados.

**3. Accede a la aplicación** en tu navegador:

```
http://localhost:8096
```

O desde otro dispositivo en la misma red:

```
http://<IP-del-servidor>:8096
```

### Parar la aplicación

```bash
docker compose down
```

---

## ⚙️ Configuración (`docker-compose.yml`)

```yaml
version: "3.8"

services:
  gestion_peliculas:
    image: mysql:8.0
    container_name: gestion_peliculas
    ports:
      - "3307:3306"
    environment:
      MYSQL_ROOT_PASSWORD: tu_contraseña
      MYSQL_DATABASE: gestionpeliculas
    restart: always
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 10

  gestion_peliculas_app:
    image: josepllopis33/gestion-peliculas-image:latest
    container_name: gestion_peliculas_app
    ports:
      - "8096:8096"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://gestion_peliculas:3306/gestionpeliculas?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: tu_contraseña
    restart: always
    depends_on:
      gestion_peliculas:
        condition: service_healthy
```

---

## 🔄 CI/CD

El proyecto utiliza **GitHub Actions** para automatizar el build y despliegue. Cada push a la rama `main`:

1. Compila el proyecto con Maven.
2. Construye la imagen Docker.
3. Hace push automáticamente a [Docker Hub](https://hub.docker.com/r/josepllopis33/gestion-peliculas-image).

---

## 🐳 Imagen en Docker Hub

```bash
docker pull josepllopis33/gestion-peliculas-image:latest
```

🔗 [josepllopis33/gestion-peliculas-image](https://hub.docker.com/r/josepllopis33/gestion-peliculas-image)

---

## 👤 Autor

**Josep Llopis**  
🐙 [GitHub](https://github.com/josepllopis33)





