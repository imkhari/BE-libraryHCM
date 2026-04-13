# Giai đoạn 1: Build bằng Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Chỉ copy file config trước để tận dụng cache của Docker (giúp build sau này cực nhanh)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy bằng JRE nhẹ (chỉ 150MB thay vì 500MB)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Cấu hình để Java hiểu tiếng Việt trong log
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

EXPOSE 8080
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]