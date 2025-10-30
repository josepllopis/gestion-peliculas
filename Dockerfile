# ----------- ETAPA 1: Construcción -----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el pom y descargamos dependencias primero (optimiza la caché)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el resto del código y construimos el jar
COPY src ./src
RUN mvn clean package -DskipTests

# ----------- ETAPA 2: Ejecución -----------
FROM maven:3.9.6-eclipse-temurin-21
WORKDIR /app

# Copiamos solo el .jar desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que usa Spring Boot
EXPOSE 8096

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]