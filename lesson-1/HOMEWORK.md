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

-Как организовать клиент-серверное взаимодействие?
java.io ServerSocket/Socket; https://javarush.ru/groups/posts/654-klassih-socket-i-serversocket-ili-allo-server-tih-menja-slihshishjh
java.nio ServerSocketChannel/SocketChannel;
Java фреймворк Netty. https://xakep.ru/2015/07/10/netty-review/

-Как и в каком виде передавать файлы?

в виде потока байтов. C помощью Чтение и запись файлов. FileInputStream и FileOutputStream https://metanit.com/java/tutorial/6.3.php
с помощью протокола передачи
-Как пересылать большие файлы?
возможно с помощью Передачи сериализованных объектов

-Как пересылать служебные команды?
передаётся побайтовое сообщение

-Что хранить в базе данных?

инфо о пользователях, пароли , майлы, файлы и т.д(мета информация)

-Как передавать структуру каталогов/файлов?
не могу найти инфо по данному вопросу(

-Какую библиотеку использовать для сетевого взаимодействия: java.io, java.nio, Netty?
Нетти


---

### Материалы:

1. [Howe make a fork](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
2. [How make a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
3. [What is Docker?] todo: add link about docker
