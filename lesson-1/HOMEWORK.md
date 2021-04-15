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

1. Подключить со стороны сервера (ServerSocket, ServerSocketChannel, NioServerSocketChannel) в зависимости от пакета. Со стороны клиента (Socket, SocketChannel,NioSocketChannel)
2. Побайтово, записывая в (Socket, SocketChannel,NioSocketChannel)
3. Побайтово с использованием буфера
4. Реализовать  протокола для передачи файлов, задав назначение каждого байта (группы байт) из последовательности
5. В виде объекта класса File (сериалищовав его) , либо перегонять строку с путем каталога в байты, а потом на требуемой стороне ее собирать
6. Предпочтительно использовать Netty.


---

### Материалы:

1. [Howe make a fork](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
2. [How make a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
3. [What is Docker?] todo: add link about docker
