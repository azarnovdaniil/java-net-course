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

1. Нам нужно 2 действующих программы(машины), клиент-сервер, инициирует всю движуху клиент.
   Сервером будет служить абстрактная машина в сети, которая будет способна получить к примеру http-запрос, обработать его и вернуть ответ.
   Клиентом будем считать все, что угодно, способное сформировать и отправить http-запрос, и так же получить ответ в любом его виде будь то файлы, каталоги, сообщения.

2. К примеру воспользоватся BufferedInputStream, и установить размер кэша 8192 ну или 16k.
   
3. Используем DeflaterOutputStream в отправителе и InflaterInputStream в получателе.
   
4. К примеру: можно создать 2 потока (2 класса имплементированных от Runnable).
   Один слушает входной поток Сокета.
   Другой слушает выходной поток сокета.

    Вот пример класса который слушает входной поток.

    public class ListingInputStream implements Runnable {
            private Thread t;
            private DataInputStream in;

            ListingInputStream(DataInputStream in) {
                super();
                this.in = in;
                t = new Thread(Thread.currentThread().getThreadGroup(), this, "ListingInputStream");
                t.start();
            }
        
        public void run() {
            try {
             while (!t.interrupted())
                  System.out.println(in.readUTF());
            } catch (Exception e) {
            }
        }
    }
        В конструктор, где-я создаю этот класс, задаю входной поток сокета, которий нужно слушать.
        Вот вызов:
        sin = new DataInputStream(socket.getInputStream());
        new ListingInputStream(sin);

5. К примеру: если объект File представляет каталог, то его метод isDirectory() возвращает true. И поэтому мы можем получить его содержимое - вложенные подкаталоги и файлы с помощью методов list() и listFiles(). 
   Получим все подкаталоги и файлы в определенном каталоге:
   
        // определяем объект для каталога
        File dir = new File("C://SomeDir");
        // если объект представляет каталог
        if(dir.isDirectory())
        {
            // получаем все вложенные объекты в каталоге
            for(File item : dir.listFiles()){
              
                 if(item.isDirectory()){
                      
                     System.out.println(item.getName() + "  \t folder");
                 }
                 else{
                      
                     System.out.println(item.getName() + "\t file");
                 }
             }
        }

6. java.io, java.nio, предпочтительнее Netty.

---

### Материалы:

1. [Howe make a fork](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo)
2. [How make a pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
3. [What is Docker?]
