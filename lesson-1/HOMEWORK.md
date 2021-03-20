# Домашняя работа

---

### Задание:

1. Подписаться на репозиторий https://github.com/azarnovdaniil/java-net-course
2. Поставить звездочку 
3. Сделать fork репозитория
4. Прислать pull-request в репозиторий курса с заполненным блоком ответы.
5. Прислать ссылку на pull-request через платформу.

---

### Вопросы:

1. Как организовать клиент-серверное взаимодействие?
2. Как и в каком виде передавать файлы?
3. Как пересылать большие файлы?
4. Как пересылать служебные команды?
5. Как передавать структуру каталогов/файлов?
6. Какую библиотеку использовать для сетевого взаимодействия: java.io, java.nio, Netty?

---

### Ответы:

1. Пока сталкивалаь только с java.io, соответственно, создать сервер и клиента и 
установить соединение с помощью ServerSocket и Socket (nio - SocketChannel, ServerSocketChannel;
Netty - Bootstrap и ServerBootstrap)
2. В виде потока байтов
3. Здесь пока сдаюсь.. в голову приходит только как-то разбить на части
4. Попробовать реализовать собственный протокол передачи (на основе ftp), передавать
 в начале сообщения 
5. Использовать классы Path, Paths, Files
6. Ту, с которой умеешь работать, потом разобраться в остальных и использовать Netty ))

---

### Материалы:

1. [Howe make a fork](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
2. [How make a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
3. [What is Docker?] todo: add link about docker
