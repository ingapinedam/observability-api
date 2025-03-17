# Usa una imagen base con JDK 17 (Java 17)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR desde el directorio 'target' de tu máquina local al contenedor
COPY target/Observability-API-0.0.1-SNAPSHOT.jar /app/Observability-API-0.0.1-SNAPSHOT.jar

# Expón el puerto en el que la aplicación Spring Boot se ejecutará
EXPOSE 8080

# Ejecuta el archivo JAR de la aplicación
CMD ["java", "-jar", "Observability-API-0.0.1-SNAPSHOT.jar"]
