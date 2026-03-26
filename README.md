# Байтик — Backend (API Server)

Серверная часть платформы для изучения программирования.

## Стек

- Kotlin 2.1.0 / JVM 17
- Spring Boot 3.4.4
- Spring Security + JWT (jjwt 0.12.6)
- Spring Data JPA + PostgreSQL 16
- Gradle 8.12, Kotlin DSL
- Python 3 (для выполнения пользовательского кода)

## Запуск

Смотри инструкцию в корневом README проекта.

## Переменные окружения

| Переменная | Значение по умолчанию |
|---|---|
| SPRING_DATASOURCE_URL | jdbc:postgresql://localhost:5432/bytik |
| SPRING_DATASOURCE_USERNAME | bytik |
| SPRING_DATASOURCE_PASSWORD | bytik_secret |
| JWT_SECRET | super-secret-jwt-key-for-bytik-2026-hackathon |

## Тестовые аккаунты (создаются автоматически)

| Логин | Пароль | Роль |
|---|---|---|
| student | student123 | Ученик (с прогрессом) |
| parent | parent123 | Родитель |
