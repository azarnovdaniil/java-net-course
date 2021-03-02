package commands;

import ru.daniilazarnov.AbstractMessage;
import ru.daniilazarnov.ClientHandler;
import ru.daniilazarnov.MyMessage;

import java.io.File;
import java.io.IOException;

public class RenameFiles extends ClientHandler {



    public static void renameFile(String[] messageCommand, String bossAccount) {
        String oldNameFile = messageCommand[2];
        String newNameFile = messageCommand[3];

        File file = new File(WAY_CLIENT + bossAccount, oldNameFile);
        File newFile = new File(WAY_CLIENT + bossAccount, newNameFile);
        if (file.renameTo(newFile)) {
            System.out.println("Файл " + file + " успешно переименован в " + newFile);
        } else {
            System.out.println("Файл " + oldNameFile + " НЕ переименован в " + newNameFile);
        }

    }




    public static void renameFileFromServer(String oldFile, String newFile, String bossAccount) {

        try {
            ClientHandler.sendMessage("/переименовать" + " " + bossAccount + " " + oldFile + " " + newFile);
            AbstractMessage am = readObject();
            MyMessage fm = (MyMessage) am;

            System.out.println(fm.getMyMessage());

        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Не удалось переименовать скорее всего файла нет");
        }
    }

}
