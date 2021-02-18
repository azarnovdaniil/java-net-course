import org.junit.Assert;
import org.junit.Test;
import ru.sviridovaleksey.workwithfiles.ShowAllDirectory;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

public class TestServer {

    private final String defaultDirectoryForDownload = "TestFOLDER";
    private final String firstDirectory = "TestFirst";
    private final String newDirectory = "TestNewFolder";
    private final String newDefaultDirectoryForDownload = "TestFOLDER2";
    private final String newFile = "TestFile.txt";
    private final String renameFile = "TestFile2.txt";
    private static final int LIMITLOGGER = 10 * 1024;
    private static final int COUNTLOGGER = 20;
    Handler fileHandler = new FileHandler("logServer_%g.txt", LIMITLOGGER, COUNTLOGGER, true);
    private final String defAddress = "Storage";
    private StringBuilder stringBuilder;

    public TestServer() throws IOException {
    }


    @Test
    public void testCreateDefaultDirectoryClient() {
        WorkWithFile workWithFile = new WorkWithFile(fileHandler);
        workWithFile.createDefaultDirectory(defaultDirectoryForDownload);
        workWithFile.createFirsDirectory(firstDirectory);
        workWithFile.createNewDirectory("test", newDirectory);
        workWithFile.createNewFile("test", newFile);
        Assert.assertTrue(Files.exists(Path.of(defaultDirectoryForDownload)));
        Assert.assertTrue(Files.exists(Path.of(firstDirectory)));
    }
    @Test
    public void testRenameFileClient() {
        WorkWithFile workWithFile = new WorkWithFile(fileHandler);
        workWithFile.renameFile("test", defaultDirectoryForDownload, newDefaultDirectoryForDownload);
        workWithFile.renameFile("test", newFile, renameFile);
        Assert.assertTrue(Files.exists(Path.of(newDefaultDirectoryForDownload)));
    }

    @Test
    public void testDeleteFileClient() {
        WorkWithFile workWithFile = new WorkWithFile(fileHandler);
        workWithFile.deleteDirectory(newDefaultDirectoryForDownload);
        workWithFile.deleteDirectory(firstDirectory);
        workWithFile.deleteDirectory(newDirectory);
        workWithFile.deleteFile(newFile);
        workWithFile.deleteFile(renameFile);
        Assert.assertFalse(Files.exists(Path.of(newDefaultDirectoryForDownload)));
        Assert.assertFalse(Files.exists(Path.of(firstDirectory)));
        Assert.assertFalse(Files.exists(Path.of(newDirectory)));
        Assert.assertFalse(Files.exists(Path.of(newFile)));
        Assert.assertFalse(Files.exists(Path.of(renameFile)));
    }
    
    @Test
    public void testShowDirectory() {
        ShowAllDirectory showAllDirectory = new ShowAllDirectory(defAddress, fileHandler);
        if (showAllDirectory.startShowDirectory(defAddress).equals(StringBuilder.class)) {
            Assert.fail();
        }
    }


}