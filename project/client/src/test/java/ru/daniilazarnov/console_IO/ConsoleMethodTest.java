package ru.daniilazarnov.console_IO;

import org.junit.jupiter.api.Test;
import ru.daniilazarnov.enumeration.Command;

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
}