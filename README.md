# ml-market-server
REST сервер для информационного портала с веб-приложениями
## Аутентификация
Настроенны CORS, CSRF, доступ по ролям, регистрация с подтверждением почты (Spring mail), запросы кроме аутентификации только для зарегистрированных пользователей (Spring Security, JWT)
## Полнотекстовый поиск
Полнотекстовый поиск при помощи Spring Data JPA Specifications
## Документация
Документация сгенерирована при помощи Swagger
## Библиотеки
jakarta- для обеспечения работы с почтой и базой данных
jsonwebtoken - для работы с JWT
springdoc - для автоматической генерации документации
spring-boot-starter-mail - для управления почтой через spring приложение
spring-boot-starter-data-jpa - для представления моделей базы данных при помощи java классов
spring-boot-starter-security - для настройки параметров безопасности
postgresql - для работы с базой данных
hibernate-jpamodelgen - для генерации мета-классов
lombok - для уменьшения числа бойлерплейтов
