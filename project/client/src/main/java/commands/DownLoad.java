package commands;

import ru.daniilazarnov.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DownLoad extends ClientHandler {


    public static void download(String file, String bossAccount) throws IOException, ClassNotFoundException {
        sendMsgFromDownload(new FileRequest(file, bossAccount));

        AbstractMessage am = readObject();

        if (am instanceof FileMessage) {
            FileMessage fm = (FileMessage) am;
            Files.write(Paths.get(WAY_CLIENT + fm.getAccount(), fm.getFileName()), fm.getData(),
                    StandardOpenOption.CREATE);
            System.out.println("получен " + file);
        } else {
            MyMessage fm = (MyMessage) am;
            System.out.println(fm.getMyMessage());
        }

    }
}
