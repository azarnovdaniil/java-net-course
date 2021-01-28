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

1. клиент(ы) и сервер будут прописаны на одном хосте, но как разные приложения. 
Можно запустить сервер и несколько клиентов. Соеданенеие посредством Socket/ServerSocket 
2. потоками байтов. Определяем - сообщаем размер, имя файла, и передаём его посредством классов BufferedInputStream, FileOutputStream, FileInputStream, BufferedOutputStream
3. думаю частями, наверное с помощью селекторов
4. думаю посредством DataOutputStream, DataInputStream
5. командами, если организовывать. Или с помощью jaca.nio выяснять, передавать, как команды, восстанавливать 
6. похоже Netty
7.

---

### Материалы:

1. [Howe make a fork](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
2. [How make a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
3. [What is Docker?]
