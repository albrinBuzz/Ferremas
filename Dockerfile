# Usar una imagen base con JDK (Java 17 o el que estés usando)
FROM eclipse-temurin:23-jdk AS builder


# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el proyecto completo al contenedor
COPY . .

# Dar permisos de ejecución a mvnw
RUN chmod +x mvnw

# Ejecutar mvn para construir el proyecto
RUN ./mvnw clean package -DskipTests


# Ejecutar la aplicación con ./mvnw
CMD ["./mvnw", "spring-boot:run"]