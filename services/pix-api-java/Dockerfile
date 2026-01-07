# Etapa 1: build com Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copie apenas o pom.xml e baixe as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Agora copie o restante do código
COPY . .

# Execute os testes
RUN mvn test

RUN cat /app/target/surefire-reports/*.txt || true

# Compile o projeto (sem rodar testes novamente)
RUN mvn clean package -DskipTests

# Etapa 2: imagem final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]