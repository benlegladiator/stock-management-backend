# Build with Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/stock-management-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=prod"]
