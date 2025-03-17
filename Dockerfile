FROM openjdk:17-slim as build
WORKDIR /workspace/app

# Verificar primero si existen los archivos de Maven
COPY pom.xml .
COPY src src

# Si no tienes mvnw, usar Maven directamente:
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline -B
RUN mvn package -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Imagen final
FROM openjdk:17-slim
VOLUME /tmp

# Exponer puerto
EXPOSE 8080

# Variables para jvmArguments
ARG DEPENDENCY=/workspace/app/target/dependency

# Estructura de la aplicación
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Punto de entrada con opciones mínimas
ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dmanagement.metrics.enabled=false", \
            "-cp", "app:app/lib/*", "com.xebia.observability.ObservabilityApiApplication"]