package commands;

import ru.daniilazarnov.AbstractMessage;
import ru.daniilazarnov.Account;
import ru.daniilazarnov.ClientHandler;
import ru.daniilazarnov.MyMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteFile extends ClientHandler {


    public static void delete(String file,  String bossAccount) {

        try {
            Account account = new Account(bossAccount);
            Path deletePath = Paths.get(WAY_CLIENT + account.getAccount() + file);
            Files.delete(deletePath);
            System.out.println("Файл " + file + " успешно удален у вас");
        } catch (IOException e) {
            System.err.println("Файл " + file + " отсутствует");
        }

    }

    public static void deleteFileFromServer(String file, String bossAccount) throws ClassNotFoundException,
            IOException {

        sendMessage("/удалить " + file + " " + bossAccount);

        AbstractMessage am = readObject();
        if (am instanceof MyMessage) {
            MyMessage fm = (MyMessage) am;
            System.out.println(fm.getMyMessage());
        }
    }
}
