# messages-mvc-app
Тестовое задание на позицию Java Junior в INSIDE.

<b><i>Docker</i> image:</b> https://hub.docker.com/r/puppymail/messages-mvc-app

База данных по умолчанию стоит H2 In-Memory, поэтому между запусками база данных будет сбрасываться.
При поднятии контекста создаётся один дефолтный отправитель с именем и паролем "root". 
Поменять можно в AuthConfiguration.

Порт по умолчанию 7000.

<b>Команда для запуска проекта с помощью <i>Maven</i>:</b> `mvnw spring-boot:run`.

<b>Команда для запуска <i>Docker</i> контейнера:</b> `docker run -dp 7000:7000 puppymail/messages-mvc-app`

В папке curl находятся скрипты:
- `api-test.sh` проверяет работоспособность системы в целом: авторизация, логин, отправка и получение сообщений.
- `register.sh` позволяет добавить отправителя в базу данных, нужно указать имя и пароль.
- `login.sh` выдает токен авторизации, нужно также указать имя и пароль. Токен выводится в консоль. Каждый новый логин обновляет токен.
- `send_message.sh` позволяет отправить сообщение, нужно указать имя отправителя и текст сообщения. Если текст сообщения вида "history N", то возвращается последние N всех сообщений.

Эндпоинты:
- `/api/register` - для регистрации новых отправителей. На вход получает POST-запрос с JSON с полями: "name" и "password". Ничего не отдаёт.
- `/api/login` - для авторизации и получения JWT-токена. На вход получает POST-запрос с JSON с полями: "name" и "password". Отдаёт JSON с полем "token".
- `/api/message` - для сохранения сообщений или получения истории сообщений. На вход получает любой запрос с JSON с полями: "name" и "message". В HTTP-запросе должен быть header "Authorization" с JWT-токеном, полученным из `/api/login`, в формате: `Bearer_%token%`. Чтобы получить историю, поле "message" должно соответствовать шаблону: "history N", где N - кол-во сообщений, которые вернёт эндпоинт. Отдаёт либо ничего, либо JSON с полем "messages". Сообщения сортируются от по убыванию даты создания, т.е. самые "свежие" сообщения вверху.
