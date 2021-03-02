package commands;

import ru.daniilazarnov.ClientHandler;
import ru.daniilazarnov.FileMessage;

import java.io.IOException;
import java.nio.file.Paths;

public class UpLoad extends ClientHandler {


    public static void upLoad(String file, String bossAccount) {
        try {
            FileMessage acc = new FileMessage(bossAccount);
            FileMessage fm = new FileMessage(Paths.get(WAY_CLIENT, acc.getAccount() + file));
            out.writeObject(fm);
            out.flush();
            System.out.println("отправил " + file);
        } catch (IOException e) {
            System.err.println("Не удалось отправить " + file + " скорее всего файла нет");
        }


    }

}
