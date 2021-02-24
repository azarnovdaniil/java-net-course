import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import ru.sviridovaleksey.workwithfile.ShowAllDirectoryClient;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;

import java.nio.file.Files;
import java.nio.file.Path;

public class ClientTest {

    private final String defaultDirectoryForDownload = "TestFOLDER";
    private static final String newDefaultDirectoryForDownload = "TestFOLDER2";
    private final String defAddress = "Download";


    @Test
    public void testCreateDefaultDirectoryClient() {
        WorkWithFileClient workWithFileClient = new WorkWithFileClient();
        workWithFileClient.createDefaultDirectory(defaultDirectoryForDownload);
        Assert.assertTrue(Files.exists(Path.of(defaultDirectoryForDownload)));
    }

    @Test
    public void testRenameFileClient() {
        WorkWithFileClient workWithFileClient = new WorkWithFileClient();
        workWithFileClient.renameFile(defaultDirectoryForDownload, newDefaultDirectoryForDownload);
        Assert.assertTrue(Files.exists(Path.of(newDefaultDirectoryForDownload)));
    }

    @AfterClass
    public static void testDeleteFileClient() {
        WorkWithFileClient workWithFileClient = new WorkWithFileClient();
        workWithFileClient.deleteFile(newDefaultDirectoryForDownload);
        Assert.assertFalse(Files.exists(Path.of(newDefaultDirectoryForDownload)));
    }

    @Test
    public void testShowDirectory() {
        ShowAllDirectoryClient showAllDirectory = new ShowAllDirectoryClient();
        if (showAllDirectory.startShowDirectory(defAddress).equals(StringBuilder.class)) {
            Assert.fail();
        }
    }

}