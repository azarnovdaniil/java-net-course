package ru.daniilazarnov.console_IO;

import org.junit.jupiter.api.Test;
import ru.daniilazarnov.FileSender;
import ru.daniilazarnov.ReceivingFiles;
import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.enumeration.State;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleMethodTest {
    ConsoleMethod cm = new ConsoleMethod();

    @Test
    void getCommand() {
        assertEquals(Command.DISCONNECT, cm.getCommand("disconnect"));
        assertEquals(Command.DELETE, cm.getCommand("delete"));
        assertNotEquals(Command.UPLOAD, cm.getCommand("delete"));
        assertEquals(Command.STATUS, cm.getCommand(" status"));
        assertEquals(Command.STATUS, cm.getCommand(" status "));
        assertEquals(Command.STATUS, cm.getCommand("STATUS "));

    }

//        @Test
//    boolean consoleIsNotBusyTest() {
//        return
//                !FileSender.isLoadingStatus()  // удалить
//                        && ReceivingFiles.getCurrentState() == State.IDLE
//                        && !OutputConsole.isConsoleBusy();
//    }

}