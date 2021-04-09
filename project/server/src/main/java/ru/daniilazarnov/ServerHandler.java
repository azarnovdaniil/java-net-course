package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static ru.daniilazarnov.State.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(ServerHandler.class);
    private static final String TEST_FILE_NAME = "test.txt";
    private static final String SERVER_STORAGE = "project" + File.separator
            + "server" + File.separator + "storage";

    private State state = IDLE;
    private String filename = "";
    private ServerSettings serverSettings = new ServerSettings();
    {
        serverSettings.setCreateNewIfExist(true);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        LOGGER.info("Client connected " + ctx.channel().remoteAddress().toString());
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String strMsg = msg.toString().trim();
        ctx.writeAndFlush(state.name());
        switch (state) {
            case IDLE:
                Command cmd = Command.byCmd(strMsg);
                switch (cmd) {
                    case TEST:
                        setState(ctx, WAITING_DATA);
                        break;
                    case UPLOAD:
                        setState(ctx, WAITING_FILENAME);
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
                break;
            case WAITING_DATA:
                String dataFilePath = SERVER_STORAGE + File.separator + TEST_FILE_NAME;
                FileUtils.createFile(dataFilePath);
                FileUtils.addTextToFile(dataFilePath, strMsg);
                setState(ctx, IDLE);
                break;
            case WAITING_FILENAME:
                filename = strMsg;
                String[] tokens = filename.split("\\.(?=[^\\.]+$)");
                String basename = tokens[0];
                String extension = "";
                if (tokens.length > 1) {
                    extension = tokens[1];
                }
                if (FileUtils.isFileExist(SERVER_STORAGE + File.separator + filename)) {
                    if (serverSettings.isCreateNewIfExist()) {
                        List<String> currentFiles = FileUtils.getFilesInDirectory(SERVER_STORAGE);
                        LOGGER.info("current files = " + currentFiles);
                        List<String> filesWithThisFilename = currentFiles.stream()
                                .filter(name -> name.startsWith(basename))
                                .collect(Collectors.toList());
                        LOGGER.info("files with this basename = " + filesWithThisFilename);

                        if (filesWithThisFilename.size() > 0) {
                            if (filesWithThisFilename.size() == 1) {
                                filename = basename + "(1)." + extension;
                            } else {
                                //TODO rewrite with regexp
                                int lastFileIndex = filesWithThisFilename.stream()
                                        .map(s -> s.split("\\.(?=[^\\.]+$)")[0])
                                        .filter(s -> s.endsWith(")"))
                                        .map(s -> s.replace(basename + "(", ""))
                                        .map(s -> s.replace(")", ""))
                                        .mapToInt(Integer::valueOf)
                                        .max()
                                        .getAsInt();
                                LOGGER.info("Got last file index = " + lastFileIndex);
                                filename = basename + "(" + (lastFileIndex + 1) + ")." + extension;
                            }
                        }
                        LOGGER.debug("Got new filename: " + filename);
                    }
                }
                setState(ctx, WAITING_FILECONTENT);
                break;
            case WAITING_FILECONTENT:
                String filecontent = strMsg;
                String newFilePath = SERVER_STORAGE + File.separator + filename;
                FileUtils.createFile(newFilePath);
                FileUtils.addTextToFile(newFilePath, filecontent);
                LOGGER.info("File " + filename + " stored on server.");
                filename = "";
                setState(ctx, IDLE);
                break;
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

    private void setState(ChannelHandlerContext ctx, State state) {
        LOGGER.info("Updating state: " + this.state + " -> " + state);
        this.state = state;
        ctx.writeAndFlush(state.name());
    }
}
