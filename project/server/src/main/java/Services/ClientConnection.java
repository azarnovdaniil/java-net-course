package server.Services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ClientConnection
{
    private final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    private static final Logger logg = Logger.getLogger(ClientConnection.class.getName());
    private SocketChannel clientSocket;




    public ClientConnection(int usePort, Handler fileHandler) throws IOException {
        logg.addHandler(fileHandler);
        serverSocketChannel.socket().bind(new InetSocketAddress(usePort));
        logg.setUseParentHandlers(false);
    }

    public void connecting()
    {

        System.out.println("Сервер запущен");
        ExecutorService poolClient = Executors.newFixedThreadPool(10);

        while (true)
        {
            waitNewClientConnection(poolClient);
        }


    }

    private void waitNewClientConnection (ExecutorService poolClient)
    {
        System.out.println("Ожидание подключения пользователя");
        logg.log(Level.INFO, "Ожидание подключения пользователя");

        try {

            clientSocket = serverSocketChannel.accept();
            logg.log(Level.INFO, "Клиент успешно подключен " + clientSocket.getRemoteAddress());
            System.out.println("Клиент успешно подключен " + clientSocket.getRemoteAddress());

            poolClient.execute(()-> {
                        checkingClientConnection(clientSocket);

                    }
            );

        } catch (IOException e)
        {
            System.out.println("Ошибка при подключения нового клиента");
            logg.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                clientSocket.finishConnect();
            } catch (IOException e)
            {
                System.out.println("Ошибка при  закрытии неудачного соединеня");
                logg.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public void checkingClientConnection(SocketChannel clientSocket) {
        new ClientHandler(this, clientSocket).handle();
    }
}
