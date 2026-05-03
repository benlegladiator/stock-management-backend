# Étape 1 : Build avec Maven et JDK 17
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Étape 2 : Runtime avec OpenJDK 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/stock-management-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=prod"]
