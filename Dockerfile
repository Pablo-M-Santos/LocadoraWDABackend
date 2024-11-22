# Etapa de build do projeto Spring Boot
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências
COPY locadoraLivro/pom.xml ./
RUN mvn dependency:go-offline

# Copiar o código-fonte e fazer o build
COPY locadoraLivro/src ./src
RUN mvn clean package -DskipTests

# Usar JDK para rodar a aplicação em produção
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar o .jar gerado no build
COPY --from=build /app/target/*.jar app.jar

# Expor a porta 8040
EXPOSE 8040

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
