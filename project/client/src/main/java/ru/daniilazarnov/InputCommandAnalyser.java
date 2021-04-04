package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class InputCommandAnalyser {
    private static final Logger LOGGER = LogManager.getLogger(InputCommandAnalyser.class);
    static final Marker toCon = MarkerManager.getMarker("CONS");
    private final ClientModuleManager hub;
    private Consumer<String> print;
    private Consumer<ContextData> execute;
    private Consumer<String> setRepoPath;
    private Consumer<Boolean> close;
    private Consumer<Boolean> startConnection;
    private BiConsumer<String, Integer> setHost;
    public Consumer<String> yesOrNo;
    private static final String NOT_REGISTERED = "You are not authorised. Login first, please.";
    private boolean isYes;

    public InputCommandAnalyser(ClientModuleManager hub) {
        this.hub = hub;
        this.yesOrNo = s -> yesNoAnswer(s);
    }

    public void commandDecoder(String command, boolean isAuthorised) {
        LOGGER.info("New command input: " + command);
        String[] comArray = command.split(" ");
        int comNum = -1;
        try {
            comNum = CommandList.valueOf(comArray[0]).getNum();
        } catch (IllegalArgumentException e) {
            print.accept("No such command found. Print help to get command list.");
        }
        if (comNum == CommandList.help.getNum()) {
            if (comArray.length == 1) {
                help();
            } else helpCom(comArray[1]);
        } else if (comNum == CommandList.login.getNum()) {
            logIn(comArray);
        } else if (comNum == CommandList.register.getNum()) {
            register(comArray);
        } else if (comNum == CommandList.getFileList.getNum()) {
            if (!isAuthorised) {
                print.accept(NOT_REGISTERED);
                return;
            }
            getFileList();
        } else if (comNum == CommandList.upload.getNum()) {
            if (!isAuthorised) {
                print.accept(NOT_REGISTERED);
                return;
            }
            uploadFile(comArray);
        } else if (comNum == CommandList.download.getNum()) {
            if (!isAuthorised) {
                print.accept(NOT_REGISTERED);
                return;
            }
            download(comArray);
        } else if (comNum == CommandList.delete.getNum()) {
            if (!isAuthorised) {
                print.accept(NOT_REGISTERED);
                return;
            }
            delete(comArray);
        } else if (comNum == CommandList.setRepository.getNum()) {
            setRepository(comArray);
        } else if (comNum == CommandList.exit.getNum()) {
            closeApp();
        } else if (comNum == CommandList.connect.getNum()) {
            connect();
        } else if (comNum == CommandList.setHost.getNum()) {
            setHostPort(comArray);
        } else if (comNum == CommandList.rename.getNum()) {
            rename(comArray);
        }
    }

    public void initConsumers(Consumer<String> print, Consumer<ContextData> execute, Consumer<String> setRepoPath,
                              Consumer<Boolean> close, Consumer<Boolean> startConnection, BiConsumer<String, Integer> setHost) {
        this.print = print;
        this.execute = execute;
        this.setRepoPath = setRepoPath;
        this.close = close;
        this.startConnection = startConnection;
        this.setHost = setHost;
    }

    private void help() {
        List commands = Arrays.stream(CommandList.values())
                .filter(com -> !com.isSystemOnly())
                .collect(Collectors.toList());
        System.out.println(commands.toString());
        print.accept("If you need more details, print help command_you_need to know more about it.");
    }

    public void helpCom(String command) {
        try {
            print.accept(command + CommandList.valueOf(command).getDescription());
        } catch (IllegalArgumentException e) {
            print.accept("No such command found.");
        }
    }

    private void logIn(String[] comArray) {
        ContextData data = new ContextData();
        data.setCommand(CommandList.login.getNum());
        data.setLogin(comArray[1]);
        data.setPassword(comArray[2]);
        execute.accept(data);
        LOGGER.info("Trying to login");
    }

    private void register(String[] comArray) {
        ContextData data = new ContextData();
        data.setCommand(CommandList.register.getNum());
        data.setLogin(comArray[1]);
        data.setPassword(comArray[2]);
        execute.accept(data);
        LOGGER.info("Trying to register");
    }

    private void getFileList() {
        ContextData data = new ContextData();
        data.setCommand(CommandList.getFileList.getNum());
        execute.accept(data);
        LOGGER.info("Trying get file list");
    }

    private synchronized void download(String[] comArray) {
        LOGGER.info("Trying download file "+comArray[1]);
        Path path = Paths.get(hub.getPathToRepo(), comArray[1]);
        if (path.toFile().exists()) {
            LOGGER.info("File already exists");
            hub.addThread(() -> new YesNoConsole(
                    s -> yesNoAnswer(s), "The file already exists in your repo folder. Rewrite? (yes/no)").run());
            try {
                wait();
            } catch (InterruptedException e) {
                LOGGER.error(toCon,"SWW with download answer waiting.", LOGGER.throwing(e));
            }
            if (!isYes) {
                LOGGER.info("Download canceled by user");
                print.accept("Download canceled.");
                return;
            }
        }
        ContextData data = new ContextData();
        data.setCommand(CommandList.download.getNum());
        data.setFilePath(comArray[1]);
        execute.accept(data);
        LOGGER.info("Download request sent.");
    }

    private void delete(String[] comArray) {
        ContextData data = new ContextData();
        data.setCommand(CommandList.delete.getNum());
        data.setFilePath(comArray[1]);
        execute.accept(data);
        LOGGER.info("Delete request sent.");
    }

    private void setRepository(String[] comArray) {
        Path repo = Paths.get(comArray[1]);
        if (!repo.toFile().exists()) {
            repo.toFile().mkdir();
        }
        if (repo.toFile().exists()) {
            setRepoPath.accept(repo.toString());
            LOGGER.info("Local repository changed to: " + repo.toString());
            print.accept("Your new repository is: " + repo.toString());
            return;
        }
        print.accept("Invalid path!");
    }

    private void closeApp() {
        print.accept("See you later!");
        close.accept(true);
    }

    private void connect() {
        print.accept("Trying to connect...");
        startConnection.accept(true);
    }

    private void setHostPort(String[] comArray) {
        try {
            String host = comArray[1];
            int port = Integer.parseInt(comArray[2]);
            setHost.accept(host, port);
            LOGGER.info("New host and port are set: " + host + " | " + port);
            print.accept("New host and port are set");
        } catch (IllegalArgumentException e) {
            print.accept("Illegal arguments for host/port.");
        }
    }

    private void rename(String[] comArray) {
        ContextData data = new ContextData();
        data.setCommand(CommandList.rename.getNum());
        data.setFilePath(comArray[1]);
        data.setLogin(comArray[2]);
        execute.accept(data);
    }

    public synchronized void yesNoAnswer(String answer) {
        isYes = List.of("yes", "y", "YES", "Y").contains(answer);
        this.notify();
    }

    private synchronized void uploadFile(String[] comArray) {
        LOGGER.info("Trying to upload file.");
        Path fileToSend;
        if (comArray[1].startsWith("\\")) {
            fileToSend = Paths.get(hub.getPathToRepo(), comArray[1]);
        } else fileToSend = Paths.get(comArray[1]);
        if (fileToSend.toFile().exists()) {
            hub.addThread(() -> sendFileUploadInfo(fileToSend, fileToSend.toFile().length()));
            LOGGER.info("File info sent.");
            try {
                wait();
            } catch (InterruptedException e) {
                LOGGER.error(toCon, "SWW waiting respond for upload.", LOGGER.throwing(e));
            }
            if (isYes) {
                ContextData data = new ContextData();
                data.setCommand(CommandList.upload.getNum());
                data.setFilePath(fileToSend.toString());
                execute.accept(data);
                LOGGER.info("Starting file transmission.");
            } else {
                print.accept("File upload canceled.");
                LOGGER.info("File upload canceled by user.");
            }

        } else {
            print.accept("File not found. Check the path.");
            LOGGER.info("File not found.");
        }
    }

    private void sendFileUploadInfo(Path filePath, long fileLength) {
        ContextData data = new ContextData();
        data.setCommand(CommandList.fileUploadInfo.getNum());
        data.setFilePath(filePath.toFile().getName());
        data.setPassword(Objects.toString(fileLength));
        execute.accept(data);

    }

    public synchronized void contin() {
        isYes = true;
        this.notify();
    }

}
