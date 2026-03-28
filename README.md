# Байтик — Платформа для изучения программирования

Интерактивное веб-приложение для школьников 7–15 лет. Геймифицированное обучение основам программирования через уроки, квизы, практические задания с написанием кода и мини-проекты.

---

## Инструкция по развёртыванию

### Требования

- **Docker** версии 20.10+
- **Docker Compose** версии 2.0+ (входит в Docker Desktop)
- Свободные порты: **3000** (фронтенд), **8080** (бэкенд), **5432** (PostgreSQL)
- ОС: Linux / macOS / Windows (с Docker Desktop)

### Шаг 1. Клонировать репозитории

```bash
mkdir bytik && cd bytik

git clone https://github.com/BasterMan96/apprentice-developer-server.git backend
git clone https://github.com/BasterMan96/Apprentice-developer-client.git frontend
```

### Шаг 2. Создать файл docker-compose.yml

Создать файл `docker-compose.yml` в корневой папке `bytik/` со следующим содержимым:

```yaml
services:
  postgres:
    image: postgres:16-alpine
    container_name: bytik-postgres
    environment:
      POSTGRES_DB: bytik
      POSTGRES_USER: bytik
      POSTGRES_PASSWORD: bytik_secret
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U bytik"]
      interval: 5s
      timeout: 3s
      retries: 5

  backend:
    build: ./backend
    container_name: bytik-backend
    restart: on-failure:3
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/bytik
      SPRING_DATASOURCE_USERNAME: bytik
      SPRING_DATASOURCE_PASSWORD: bytik_secret
      JWT_SECRET: super-secret-jwt-key-for-bytik-2026
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy

  frontend:
    build: ./frontend
    container_name: bytik-frontend
    restart: unless-stopped
    ports:
      - "3000:80"
    depends_on:
      - backend

volumes:
  postgres_data:
```

### Шаг 3. Собрать и запустить

```bash
docker compose up --build -d
```

Первая сборка занимает 3–5 минут (скачивание зависимостей Gradle и npm).

### Шаг 4. Проверить что всё запустилось

```bash
docker compose ps
```

Должны быть 3 сервиса в статусе `Up`:

| Сервис | Контейнер | Порт | Статус |
|--------|-----------|------|--------|
| postgres | bytik-postgres | 5432 | Up (healthy) |
| backend | bytik-backend | 8080 | Up |
| frontend | bytik-frontend | 3000 | Up |

### Шаг 5. Открыть приложение

- **Приложение:** http://localhost:3000
- **API:** http://localhost:8080/api

При первом запуске бэкенд автоматически:
1. Создаёт все таблицы в БД (Hibernate ddl-auto: update)
2. Заполняет тестовые данные: 3 курса, ~42 урока, 14 достижений, тестовые аккаунты

---

## Тестовые аккаунты

Создаются автоматически при первом запуске.

| Логин | Пароль | Роль | Описание |
|-------|--------|------|----------|
| student | student123 | Ученик | Прогресс: 5 уроков пройдено, 2 достижения, уровень 2 |
| parent | parent123 | Родитель | Привязан к аккаунту student |

Также можно зарегистрировать нового пользователя через форму регистрации.

---

## Остановка и перезапуск

```bash
# Остановить все сервисы
docker compose down

# Остановить и удалить данные БД (полный сброс)
docker compose down -v

# Перезапустить с пересборкой
docker compose up --build -d
```

---

## Технологический стек

### Backend
- **Kotlin** 2.1.0 / JVM 17
- **Spring Boot** 3.4.4
- **Spring Security** + JWT (jjwt 0.12.6)
- **Spring Data JPA** + Hibernate
- **PostgreSQL** 16
- **Gradle** 8.12, Kotlin DSL
- **Python 3** — для выполнения пользовательского кода (установлен в Docker-контейнере бэкенда)

### Frontend
- **React** 19 + **TypeScript**
- **Vite** 6 (сборка)
- **Tailwind CSS** 3 (mobile-first дизайн)
- **Zustand** 5 (state management)
- **React Router** 7 (маршрутизация)
- **CodeMirror** 6 (редактор кода)
- **Framer Motion** (анимации)

### Инфраструктура
- **Docker** + **Docker Compose**
- **Nginx** — раздача фронтенда + проксирование `/api/` на бэкенд
- **PostgreSQL** 16 (Alpine)

---

## Архитектура

```
┌─────────────────┐
│   Пользователь  │
│   (браузер)     │
└────────┬────────┘
         │ :3000
┌────────▼────────┐
│     Nginx       │
│  (frontend)     │
│  - статика SPA  │
│  - proxy /api/  │
└────────┬────────┘
         │ :8080
┌────────▼────────┐
│   Spring Boot   │
│   (backend)     │
│  - REST API     │
│  - JWT Auth     │
│  - Python exec  │
└────────┬────────┘
         │ :5432
┌────────▼────────┐
│   PostgreSQL    │
│   (database)    │
└─────────────────┘
```

---

## Функционал

### Базовый (обязательный)
- Регистрация и авторизация (JWT)
- Каталог курсов с фильтрацией по сложности и поиском
- Уроки: теория (Markdown), квизы (5 вопросов), практика (код-редактор), мини-проекты
- Выполнение Python-кода прямо в браузере
- Система уровней, XP, валюта «Байты», достижения (14 штук)
- Профиль с статистикой и прогресс-баром уровня
- Портфолио выполненных проектов
- Сертификаты за прохождение курсов
- «Моё обучение» — прогресс по записанным курсам

### Дополнительный
- Родительский контроль — привязка аккаунта ребёнка, просмотр прогресса и статистики

---

## Переменные окружения

| Переменная | Значение по умолчанию | Описание |
|---|---|---|
| SPRING_DATASOURCE_URL | jdbc:postgresql://localhost:5432/bytik | URL подключения к БД |
| SPRING_DATASOURCE_USERNAME | bytik | Пользователь БД |
| SPRING_DATASOURCE_PASSWORD | bytik_secret | Пароль БД |
| JWT_SECRET | super-secret-jwt-key-for-bytik-2026-hackathon | Секрет для JWT токенов |

---

## Возможные проблемы

### Порт занят
```
Error: address already in use
```
Решение: остановить процесс на нужном порту или изменить порты в docker-compose.yml.

### Backend не подключается к PostgreSQL
Если бэкенд упал с ошибкой `Connection refused` — перезапустите:
```bash
docker compose restart backend
```
Бэкенд настроен на `restart: on-failure:3`, обычно переподключается автоматически.

### Первая сборка долгая
Первый `docker compose up --build` скачивает все зависимости (~3-5 минут). Последующие сборки используют кэш Docker и проходят быстрее.
