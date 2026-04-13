# Giai đoạn 1: Dùng Maven để build ra file .jar (Dùng bản Temurin 21 chuẩn)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Giai đoạn 2: Dùng Java 21 để chạy file .jar (Bản JRE siêu nhẹ)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]