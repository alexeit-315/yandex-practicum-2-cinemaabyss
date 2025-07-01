# Используем официальный образ OpenJDK 21 с Gradle для сборки
FROM gradle:8-jdk21-jammy AS builder

# Рабочая директория для сборки
WORKDIR /app

# Копируем исходные файлы (кроме указанных в .dockerignore)
COPY . .

# Собираем приложение
RUN gradle build --no-daemon --exclude-task test

# Финальный образ
FROM eclipse-temurin:21-jdk-jammy

# Рабочая директория в контейнере
WORKDIR /app

# Копируем собранный JAR из стадии builder
COPY --from=builder /app/build/libs/events-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт 8082
EXPOSE 8082

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]