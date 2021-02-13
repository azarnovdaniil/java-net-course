package ru.daniilazarnov;

//mvn -B package --file pom.xml

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;


class HandlerTest {
    @Test
    void testMethod() {
        System.out.println("Command.AUTH: " + Command.AUTH);
//        System.out.println("Command.AUTH.getCommandByte(): " + Command.AUTH.getCommandByte());
//        System.out.println("123");
    }


    @Test
    void testMethod2() {
        int i = 6;
        System.out.println(Arrays.stream(Command.values())
                .filter(command -> command.getCommandByte() == (byte) i).findFirst().orElse(Command.UNKNOWN));
    }

    @Test
    void testMethod3() {
        String s = "auth";

        Command command = Command.valueOf(s.toUpperCase());
        System.out.println(command.getCommandByte());
    }

    @Test
    void testMethod4() {
//        public static final String USER_FOLDER_PATH = "project\\client\\local_storage\\";
        String USER_FOLDER_PATH = Path.of("project", "client", "local_storage").toString();
//        public static final String SERVER_FOLDER_PATH = "project\\server\\cloud_storage\\user1\\";
        String user = "user1";
        String SERVER_FOLDER_PATH = Path.of("project", "server", "cloud_storage", user).toString();
        System.out.println(SERVER_FOLDER_PATH + File.separator);
        System.out.println(USER_FOLDER_PATH);


    }

}