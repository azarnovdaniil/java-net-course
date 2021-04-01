1. Модуль Server включает пакет serverOperations и класс ServerConnection.
   
        Класс ServerConnection отвечат за запуск сервера. Метод start() открывает сокет для подключения клиентов,
        подготавливает WelcomeMessage и запускает метод waitConnections(), в котором открывается селектор и захватываются
        все подключающиеся клиенты и дальнейшие сообщения от них.
        
        Пакет ServerConnection содержит в себе классы каждой из операций, имплементирующие интерфейс ServerOperation, 
        которые выполняет сервер в зависимости от полученной команды клиента. Также в этот пакет входит класс
        FabricOfOperations, содержащий фабричный метод getOperation(), который в зависимости от переданной команды
        клиента, создаёт объект класса соответствующей операции на сервере.


2. Модуль Client включает в себя пакет commands и класс ClientConnection.
   
        Класс ClientConnection отвечает за приём и интерпретацию команд из консоли, а также за запуск в отдельном
        потоке селектора, который перехватывает входящие сообщения от сервера.
        
        Пакет commands содержит в себе классы каждой из команд, имплементирующие интерфейс ICommand, которые 
        подготавливают соответствующим образом и отправляют сообщение на сервер. Также в этот пакет входит класс
        FabricOfCommands, содержащий фабричный метод getCommand(), который в зависимости от переданых ему аргументов, 
        создаёт объект класса соответствующей команды.


3. Модуль Common включает в себя класссы Protocol и UserInfo, а также перечисление Commands.

        Класс Protocol отвечает за отправку и приём файлов и сообщений как сервером, так и клиентом.
        Класс UserInfo содежрит в себе всю необходимую информацию о клиенте.
        Перечисление Commands включает в себя все команды, необходимые для взаимодействия клиента с сервером, а также
        их байтовое представление.

Реализованые команды:
   -авторизация по имени;
   -отправка файла;
   -получение файла;
   -создание новой папки;
   -смена текущей папки;
   -информация о текущем каталоге;
   -disconnect;