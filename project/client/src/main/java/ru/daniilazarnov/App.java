
package ru.daniilazarnov;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        CLI cli = new CLI();
        Config config = Config.readConfig(Config.DEFAULT_CONFIG);
        cli.run(config.getHost(), config.getPort(), config.getClientRepo());
    }
}
