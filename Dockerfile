# === Stage 1: Builder ===
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Копируем все pom.xml и исходники сразу
COPY pom.xml .
COPY service service
COPY storage storage
COPY notification notification
COPY liquibase liquibase
COPY feignClient feignClient

# 2. Загружаем все зависимости в кэш
RUN mvn dependency:go-offline -B

# 3. Собираем главный модуль service + все зависимости
RUN mvn -pl service -am clean package -DskipTests -B


# === Stage 2: Runtime ===
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Создаём непривилегированного пользователя
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Копируем fat jar из билд-стейджа
COPY --from=builder /app/service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
