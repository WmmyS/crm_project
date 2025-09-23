# Multi-stage build para compilar com Maven no container
FROM maven:3-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar arquivos de configuração do Maven primeiro (para cache de dependências)
COPY pom.xml .

# Baixar dependências (será cached se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar a aplicação
RUN mvn clean package -DskipTests

# Stage final - apenas runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar apenas o JAR compilado do stage anterior
COPY --from=build /app/target/crm-application-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]