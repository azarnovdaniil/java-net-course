package ru.uio.io;

import ru.uio.io.handlers.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.uio.io.ClientHandlerEnum.*;

public class Client {
    private DataInputStream in;
    private DataOutputStream out;
    private AtomicBoolean isClose;
    private Map<ClientHandlerEnum, BaseHandler> handlerMap = new HashMap<>();
    private Scanner scanner ;

    public Client() throws IOException {
        Socket socket = new Socket("localhost", 8888);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.isClose = new AtomicBoolean();
        this.scanner = new Scanner(System.in);
        init();
    }

    private void init() throws IOException {
        Path dir = Paths.get("download");
        if(!Files.isDirectory(dir))
            Files.createDirectory(dir);

        handlerMap.put(AUTH, new AuthHandler(in, out));
        handlerMap.put(UPLOAD, new UploadHandler(in, out));
        handlerMap.put(LIST, new ListHandler(in, out));
        handlerMap.put(DOWNLOAD, new DownloadHandler(in, out));
        handlerMap.put(HELP, new HelpHandler(in, out));
        handlerMap.put(REG, new RegHandler(in, out));
        handlerMap.put(DEL, new DeleteHandler(in, out));

        isClose.set(false);
    }

    public void start(){
        execute(handlerMap.get(HELP));
        while (true) {
            try {
                if (isClose.get()){
                    System.out.println("Connect closed");
                    scanner.close();
                    break;
                }
                System.out.println("Enter command:");
                String command = scanner.nextLine();
                if (command.startsWith("-help")){
                    execute(handlerMap.get(HELP));
                    continue;
                }

                BaseHandler baseHandler;
                if (command.startsWith("-auth")){
                    baseHandler = handlerMap.get(AUTH);
                    baseHandler.setCommand(command);
                    execute(baseHandler);
                    continue;
                }
                if (command.startsWith("-reg")){
                    baseHandler = handlerMap.get(REG);
                    baseHandler.setCommand(command);
                    execute(baseHandler);
                    continue;
                }
                if (command.startsWith("-del")){
                    String[] str = command.split(" ");
                    if(str.length <= 1){
                        System.out.println("Please write number file. Like: -del 1");
                        continue;
                    }
                    if("-del".equals(str[0])){
                        baseHandler = handlerMap.get(DEL);
                        baseHandler.setCommand(str[1]);
                        execute(baseHandler);
                    }
                    continue;
                }
                if (command.startsWith("-u") || command.startsWith("-upload")){
                    System.out.println("upload");
                    execute(handlerMap.get(UPLOAD));
                    continue;
                }
                if (command.startsWith("-l") || command.startsWith("-list")){
                    execute(handlerMap.get(LIST));
                    continue;
                }
                if (command.startsWith("-d") || command.startsWith("-download")){
                    String[] str = command.split(" ");
                    if(str.length <= 1){
                        System.out.println("Please write number file. Like: -d 1");
                        continue;
                    }
                    baseHandler = handlerMap.get(DOWNLOAD);
                    baseHandler.setCommand(str[1]);
                    execute(baseHandler);
                    continue;
                }
                if (command.startsWith("-exit")){
                    out.write(Common.createDataPacket("120".getBytes("UTF8"), "exit".getBytes("UTF8")));
                    out.flush();
                    isClose.set(true);
                }

            } catch (IOException e) {
                throw new RuntimeException("SWW6", e);
            }
        }
    }

    private void execute(BaseHandler baseHandler){
        baseHandler.workWithMessage();
    }
}
