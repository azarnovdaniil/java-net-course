package ru.daniilazarnov;

//mvn -B package --file pom.xml
import org.junit.jupiter.api.Test;

import java.util.Arrays;


class HandlerTest {
    @Test
    void testMethod(){
        System.out.println("Command.AUTH: " + Command.AUTH);
//        System.out.println("Command.AUTH.getCommandByte(): " + Command.AUTH.getCommandByte());
//        System.out.println("123");
    }


    @Test
    void testMethod2(){
        int i = 6;
        System.out.println(Arrays.stream(Command.values())
                .filter(command -> command.getCommandByte() == (byte) i).findFirst().orElse(Command.UNKNOWN));
    }

    @Test
    void testMethod3(){
        String s = "auth";

        Command command = Command.valueOf(s.toUpperCase());
        System.out.println(command.getCommandByte());
    }
}