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

1. с помощью пакета java.io, java.nio или фреймворк Netty (не смотрел еще)
2. FileInputStream и FileOutputStream (Netty, не смотрел еще)
3. Через буфер. размер буфера передавать в самом начале (возможно в Netty как то еще)
4. Если io: DataOutputStream,DataInputStream (или Netty что-то реализует, не смотрел еще)
5. Структуру каталогов, файлов хранить в файле, определенного формата. Передавать его при подключении..ну там еще если получится
   допилить авторизацию и передавать только строку из файла, например
6. Netty

---

### Материалы:

1. [Howe make a fork](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
2. [How make a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
3. [What is Docker?] todo: add link about doc