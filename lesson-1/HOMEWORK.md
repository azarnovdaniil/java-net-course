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

1. 
 Класс ServerSocket server; класс Socket socket;
 PORT;
 DataInputStream in - 
 DataOutputStream out;

2. Если это пакет java.io., то возможно используя классы FileOutputStream/FileInputStream

3.Честно, пока не знаю. Видимо частями.
 Надеюсь этот вопрос мы будем разбирать отдельно.

4. 
Проще используя стринговые команды типа:
String str = in.readUTF();
if (str.equals("/regok")) {

Хотя интересно было бы попробовать сериализацию.

5. Используя класс File как вариант. 

6. Пока начну с java.io


---

### Материалы:

1. [Howe make a fork](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
2. [How make a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
3. [What is Docker?]
