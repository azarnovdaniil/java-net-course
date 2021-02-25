package server.operations;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.*;
import ru.daniilazarnov.common.commands.Commands;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.server.auth.AuthService;
import ru.daniilazarnov.server.auth.AuthenticationException;
import ru.daniilazarnov.server.database.sql.ConnectionService;
import ru.daniilazarnov.server.handlers.MessageHandler;
import ru.daniilazarnov.server.operations.ServerOperationsFactory;
import ru.daniilazarnov.server.operations.download.DownloadHandler;
import ru.daniilazarnov.server.operations.login.LoginHandler;
import ru.daniilazarnov.server.operations.show.ShowHandler;
import ru.daniilazarnov.server.operations.upload.UploadHandler;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerOperationsFactoryTest {

    private static EmbeddedChannel channel;
    private static final String TEST_ROOT = "C:\\Temp";
    private static final AuthService AUTH_SERVICE = new AuthService(
            new ru.daniilazarnov.server.database.sql.AuthService(
                    new ConnectionService("jdbc:mysql://localhost:3306/cs_auth",
                            "root",
                            "1")
            ));

    @BeforeAll
    static void setUp() {
        channel = new EmbeddedChannel(
                new MessageHandler(TEST_ROOT, AUTH_SERVICE));
    }

    @Test
    @Order(1)
    void createOperationLoginTest() {
        Handler handler = new ServerOperationsFactory(AUTH_SERVICE).createOperation(Commands.LOGIN.getCode(),
                channel, TEST_ROOT, ByteBufAllocator.DEFAULT.buffer(1));
        Assertions.assertTrue(handler instanceof LoginHandler);
        try {
            AUTH_SERVICE.login("test", "123", channel.id().toString());
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

    }

    @Test
    void createOperationDownloadTest() {
        Handler handler = new ServerOperationsFactory(AUTH_SERVICE).createOperation(Commands.DOWNLOAD.getCode(),
                channel, TEST_ROOT, ByteBufAllocator.DEFAULT.buffer(1));
        Assertions.assertTrue(handler instanceof DownloadHandler);
    }

    @Test
    void createOperationUploadTest() {
        Handler handler = new ServerOperationsFactory(AUTH_SERVICE).createOperation(Commands.UPLOAD.getCode(),
                channel, TEST_ROOT, ByteBufAllocator.DEFAULT.buffer(1));
        Assertions.assertTrue(handler instanceof UploadHandler);
    }

    @Test
    void createOperationShowTest() {
        Handler handler = new ServerOperationsFactory(AUTH_SERVICE).createOperation(Commands.SHOW.getCode(),
                channel, TEST_ROOT, ByteBufAllocator.DEFAULT.buffer(1));
        Assertions.assertTrue(handler instanceof ShowHandler);
    }

    @Test
    void createOperationMessageTest() {
        Handler handler = new ServerOperationsFactory(AUTH_SERVICE).createOperation(Commands.MESSAGE.getCode(),
                channel, TEST_ROOT, ByteBufAllocator.DEFAULT.buffer(1));
        Assertions.assertNull(handler);
    }

}