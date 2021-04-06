package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.utils.FileUtils;

import java.io.File;

import static ru.daniilazarnov.State.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(ServerHandler.class);
    private static final String TEST_FILE_NAME = "test.txt";
    private static final String SERVER_STORAGE = "project" + File.separator
            + "server" + File.separator + "storage";

    private State state = IDLE;
    private String filename = "";

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        LOGGER.info("Client connected " + ctx.channel().remoteAddress().toString());
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String strMsg = msg.toString().trim();
        if (state.equals(WAITING_FILECONTENT)) {
            String filecontent = strMsg;
            String filePath = SERVER_STORAGE + File.separator + filename;
            FileUtils.createFile(filePath);
            FileUtils.addTextToFile(filePath, filecontent);
            LOGGER.info("File " + filename + " stored on server.");
            filename = "";
            setState(IDLE);
        }
        if (state.equals(WAITING_FILENAME)) {
            filename = strMsg;
            setState(WAITING_FILECONTENT);
        }
        if (state.equals(WAITING_DATA)) {
            String filePath = SERVER_STORAGE + File.separator + TEST_FILE_NAME;
            FileUtils.createFile(filePath);
            FileUtils.addTextToFile(filePath, strMsg);
            setState(IDLE);
        }
        if (state.equals(IDLE)) {
            Command cmd = Command.byCmd(strMsg);
            switch (cmd) {
                case TEST:
                    setState(WAITING_DATA);
                    break;
                case UPLOAD:
                    setState(WAITING_FILENAME);
                    break;
                case EXIT:
                    ctx.close();
                    break;
                case UNKNOWN:
                    LOGGER.warn("Unknown command received" + strMsg);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LOGGER.info("Read Complete...");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Client disconnected " + ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Error..." + cause.toString(), cause);
        ctx.close();
    }

    private void setState(State state) {
        LOGGER.info("Updating state: " + this.state + " -> " + state);
        this.state = state;
    }
}
