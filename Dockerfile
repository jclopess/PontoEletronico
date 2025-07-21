# Estágio 1: Build - Usa uma imagem com Maven e JDK para compilar o projeto
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Run - Usa uma imagem leve apenas com o Java para rodar a aplicação
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
# Copia o .jar gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta que a aplicação vai usar dentro do container
EXPOSE 8081
# Comando para iniciar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]