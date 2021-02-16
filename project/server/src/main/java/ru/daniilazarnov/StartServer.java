package ru.daniilazarnov;

import org.apache.log4j.Logger;
import ru.daniilazarnov.handler.InboundHandler;

public class StartServer {
    private static final int SERVER_PORT = 8189;
    private static final Logger log = Logger.getLogger(InboundHandler.class);

    public static void main(String[] args) {

        int port;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = SERVER_PORT;
        }

        try {
            new Server(port).run();
        } catch (Exception e) {
            log.error(e);
        }
    }
}
