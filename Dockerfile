# Dockerfile para Spring Boot (Backend)
FROM eclipse-temurin:17-jdk-alpine AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Descargar dependencias (capa cacheada)
RUN ./mvnw dependency:go-offline

# Copiar código fuente
COPY src ./src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8090

# Variables de entorno (sobrescritas por docker-compose)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/sentimentdb
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=rootpassword
ENV DS_API_URL=http://ds-api:8000/sentiment

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]