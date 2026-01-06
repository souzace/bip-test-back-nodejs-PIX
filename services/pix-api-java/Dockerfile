# Etapa 1: build com Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copie apenas o pom.xml e baixe as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Agora copie o restante do código
COPY . .

# Compile o projeto
RUN mvn clean package -DskipTests

# Etapa 2: imagem final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]