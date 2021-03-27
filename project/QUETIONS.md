1. Не пойму, что будет, если гипотетически на момент чтения из канала придут не все байты, которые дальше 
   будут читаться из буфера?
2. Почему path не видит файл при указании такого пути: src/main/resources/test.txt ? 
   т. е. только относительно project: project/client/src/main/resources/testFile.txt;
3. Не совсем понятно насчёт метода attach(), если его использовать с ключом в методе handleAccept(),
   то он привяжется именно к этому ключу и в методе handleRead это уже будет абсолютно другой ключ?
4. Чем отличается первый отрывок кода от второго?
   
   1 отрывок
   clientSocketChannel = SocketChannel.open(inetSocketAddress);
   clientSocketChannel.configureBlocking(false);
   selector = Selector.open();
   clientSocketChannel.register(selector, SelectionKey.OP_READ);
   
   2 отрывок
   clientSocketChannel = SocketChannel.open();
   clientSocketChannel.bind(new InetSocketAddress("localhost", 8111));
   clientSocketChannel.connect(inetSocketAddress);
   clientSocketChannel.configureBlocking(false);
   selector = Selector.open();
   clientSocketChannel.register(selector, SelectionKey.OP_READ);
   handleReadFromServer();