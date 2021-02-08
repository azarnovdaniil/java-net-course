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

1. Через java.io.ServerSocket/Socket..или java.nio ServerSocketChannel/SocketChannel..или Java фреймворк Netty.. Через все попробую.
2. Сериализуем и пихаем в канал, получаем и десериализуем. Хотя можно и по байтам так кидать в канал.
3. Делим на части и передаем по очереди.
4. Ставить какой нибудь ключ перед командой.
5. xml или json можно сделать и отправить.
6. java.io, java.nio, Netty , все надо проверить.
7.

---

### Материалы:

1. [Howe make a fork](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
2. [How make a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
3. [What is Docker?]
