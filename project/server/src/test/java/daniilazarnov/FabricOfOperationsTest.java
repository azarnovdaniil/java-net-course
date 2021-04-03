package daniilazarnov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.daniilazarnov.Commands;
import ru.daniilazarnov.serverOperations.*;

public class FabricOfOperationsTest {

    @Test
    public void AuthOp() {
        ServerOperation serverOperation = FabricOfOperations.getOperation(Commands.user, null);
        Assertions.assertEquals(AuthenticationUser.class, serverOperation.getClass());
    }

    @Test
    public void ChangDirOp() {
        ServerOperation serverOperation = FabricOfOperations.getOperation(Commands.cd, null);
        Assertions.assertEquals(ChangeClientDirectory.class, serverOperation.getClass());
    }

    @Test
    public void DisconnectOp() {
        ServerOperation serverOperation = FabricOfOperations.getOperation(Commands.disconnect, null);
        Assertions.assertEquals(DisconnectClient.class, serverOperation.getClass());
    }

    @Test
    public void MakeDirOp() {
        ServerOperation serverOperation = FabricOfOperations.getOperation(Commands.mkd, null);
        Assertions.assertEquals(MakeDirectory.class, serverOperation.getClass());
    }

    @Test
    public void PresentDir() {
        ServerOperation serverOperation = FabricOfOperations.getOperation(Commands.pwd, null);
        Assertions.assertEquals(PresentWorkDirectory.class, serverOperation.getClass());
    }

    @Test
    public void SaveFileOp() {
        ServerOperation serverOperation = FabricOfOperations.getOperation(Commands.stor, null);
        Assertions.assertEquals(SaveFile.class, serverOperation.getClass());
    }

    @Test
    public void UploadOp() {
        ServerOperation serverOperation = FabricOfOperations.getOperation(Commands.retr, null);
        Assertions.assertEquals(UploadFileToClient.class, serverOperation.getClass());
    }
}
