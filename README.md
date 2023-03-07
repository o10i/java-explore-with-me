# _REST API BACKEND SERVICE "Explore With Me"_
#### Сервис позволяющий делиться информацией об интересных событиях и помогать найти компанию для участия в них

### _Используемые технологии_
- Java 11
- SpringBoot Framework
- PostgreSQL
- JPA, Hibernate
- Docker
- Swagger

## _Структура приложения_

### _Main Service API_
#### Главный сервис, в котором реализованы основные функции приложения
- /users
- /events
- /requests
- /compilations
- /categories
- /comments

### _Statistics service API_
#### Многомодульный сервис статистики:
- клиент
- общие dto
- сервер

Клиент позволяет сохранять информацию о запросах из главного сервиса в сервис статистики.

### _Main database_
#### База данных хранящая в себе пользователей, события, подборки, запросы и категории
<img src="https://github.com/o10i/java-explore-with-me/blob/main/img/ewm-db-ER.png?raw=true" width="40%" height="40%">

### _Statistic database_
#### База данных статистики, хранящая в себе количество просмотров событий
<img src="https://github.com/o10i/java-explore-with-me/blob/main/img/stats-db-ER.png?raw=true" width="20%" height="20%">

## _Swagger API Specification_
- Main service - https://github.com/o10i/java-explore-with-me/blob/main/ewm-main-service-spec.json
- Statistic service - https://github.com/o10i/java-explore-with-me/blob/main/ewm-stats-service-spec.json

## _Docker start-up guide_
1. mvn clean package
2. mvn install
3. docker-compose build
4. docker-compose up -d
5. Main service: http://localhost:8080
6. Statistic service: http://localhost:9090