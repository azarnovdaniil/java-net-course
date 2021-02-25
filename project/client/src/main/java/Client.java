import java.io.IOException;
public class Client
{
    private static final String serverAddress = "localHost";
    private static final int serverPort = 8189;

    public static void main(String[] args) throws IOException {

        new Connection(serverAddress, serverPort).Connecting();

    }
}
