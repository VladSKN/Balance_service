# Исследование многопоточного сервиса

## Цель
Создание многопоточного сервиса и оценка его производительности

## Функционал сервиса
Сервис должен хранить, обновлять и получать значение банковского счёта по указанному идентификатору используя следующий интерфейс:

    /**
     * Интерфейс сервиса для работы с банковским счётом содержит два метода
     **/
    interface BalanceService {
        /**
        *  Получение баланса
        *
        *  @param id идентификатор банковского счёта
        *  @return сумма денег на банковском счёте
        */
        Optional<Long> getBalance(Long id);

        /**
        *  Изменение баланса на определённое значение
        *
        *  @param id идентификатор банковского счёта
        *  @param value сумма денег, которую нужно добавить к банковскому счёту
        */
        void changeBalance(Long id, Long amount);
    }

## Технологии использованные в проекте
- Spring boot
- Maven сборщик проектов
- Docker
- Liquibase миграции баз данных
- PostgreSQL база данных
- Logback логгирование, файл с настройками `src/main/resources/logback.xml`
- Swagger
- JUnit 5, Mockito тестирование

### Сборка и запуск сервиса
- Собрать приложение, выполнив `mvn clean package`
- Перейти корневую папку проекта и собрать docker образ выполнив команду в корневой папке проекта<br>
  `docker build -f DockerfileRESTAPP -t restapp .`<br>
### Сборка и запуск базы данных Postgres
- Перейти корневую папку проекта и собрать docker образ выполнив команду в корневой папке проекта<br>
  `docker build -f DockerfilePostgres -t postgres .`<br>

### Запуск в docker-compose
Для запуска всех приложений в docker-compose выполнить команду:<br>
`docker-compose up` при добавлении ключа `-d` приложение запустится в фоновом режиме<br>
(Файл с описанием docker-compose находится в корневой папке проекта `docker-compose.yml`)