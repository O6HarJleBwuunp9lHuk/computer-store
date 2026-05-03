# Computer Store API

REST API для управления каталогом компьютерной техники.

**Типы товаров:**
- Настольные компьютеры (десктопы, неттопы, моноблоки)
- Ноутбуки (13, 14, 15, 17 дюймов)
- Мониторы
- Жёсткие диски

---

## Технологии

- Java 17
- Spring Boot 4.0.6
- MongoDB
- Docker / Docker Compose
- Liquibase (миграции БД)
- MapStruct (маппинг)
- Caffeine (кэширование)
- JUnit 5 + Testcontainers (тесты)
- Swagger / OpenAPI

---

## Сборка и запуск

### Требования
- JDK 17+
- Docker Desktop (для MongoDB)
- Gradle (или использовать ./gradlew)

### 1. Клонировать репозиторий
```bash
git clone https://github.com/YOUR_USERNAME/computer-store.git
cd computer-store
```
### 2. Сборка проекта
```bash
./gradlew clean build
```

### 3. Запустить через Docker Compose
```bash
docker-compose up -d
```
### 4. Проверить работу
```bash
http://localhost:8080/actuator/health
Ожидаемый ответ: {"status":"UP"}
```

###5. Swagger UI
Открыть в браузере: http://localhost:8080/swagger-ui/index.html

## Запуск локально (без Docker)
###1. Запустить MongoDB
```bash
docker run -d --name mongodb -p 27017:27017 mongo:7
```
###2. Запустить приложение
```bash
./gradlew bootRun
Приложение будет доступно на порту 8085.
```

### API Эндпоинты
Method	URL	Description
POST	/api/products	Создать товар
PUT	/api/products/{id}	Обновить товар
GET	/api/products/types/{type}	Получить по типу
GET	/api/products/{id}	Получить по ID
##Пример: создать ноутбук
bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "type": "LAPTOP",
    "serialNumber": "SN-EXAMPLE-001",
    "manufacturer": "Apple",
    "price": 129999,
    "stockQuantity": 10,
    "screenSize": 15
  }'
##Пример ответа
json
{
  "id": "65f7a1b2c3d4e5f6g7h8i9j0",
  "serialNumber": "SN-EXAMPLE-001",
  "type": "LAPTOP",
  "manufacturer": "Apple",
  "price": 129999,
  "stockQuantity": 10,
  "screenSize": 15,
  "createdAt": 1698768000000,
  "updatedAt": 1698768000000
}

### Валидация
Все ограничения настраиваются через application.yml:

yaml
app:
  validation:
    serial:
      min-length: 5
      max-length: 50
      pattern: "^[A-Z0-9-]+$"
    laptop:
      screen-sizes: [13, 14, 15, 17]
    desktop:
      form-factors: [DESKTOP, NETTOP, ALL_IN_ONE]
    price:
      min: "0.01"
      max: "9999999.99"
    stock:
      min: 0
      max: 100000
### Docker команды

# Запуск
docker-compose up -d

# Остановка
docker-compose down

# Просмотр логов
docker-compose logs -f app

# Статус контейнеров
docker-compose ps

# Полная очистка (удалить всё)
docker-compose down -v

### Структура проекта
text
computer-store/
├── src/
│   ├── main/java/org/test/h2o/
│   │   ├── config/          # Конфигурации (OpenAPI, CORS, Validation)
│   │   ├── controller/      # REST контроллеры
│   │   ├── dto/             # Record DTO
│   │   ├── enam/            # Enum (ProductType, ErrorCode, FormFactor)
│   │   ├── exception/       # Исключения + GlobalExceptionHandler
│   │   ├── mapper/          # MapStruct мапперы
│   │   ├── model/           # MongoDB Entity
│   │   ├── repository/      # Репозиторий
│   │   ├── service/         # Бизнес-логика + валидация
│   │   └── swagger/         # Процессоры для Swagger
│   └── test/                # Unit и интеграционные тесты
├── src/main/resources/
│   ├── application.yml
│   ├── application-local.yml
│   └── application-dev.yml
├── docker-compose.yml
├── Dockerfile
├── build.gradle
└── README.md
### Контакты
Автор: Bredikhin Andrey
Email: anri23092003@gmail.com
