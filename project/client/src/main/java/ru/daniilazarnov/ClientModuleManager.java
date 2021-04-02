package ru.daniilazarnov;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ClientModuleManager {

    public InputCommandAnalyser inAnalyser;
    public Console console;

    private final Path configPath;
    private boolean isAuthorised;

    private final ExecutorService threadPool;

    private ClientConfigProcessor configProc;
    private RepoClient client;
    private ClientPathHolder pathHolder;
    private ClientCommandReader incomingReader;
    private ContextData conData;

    private Consumer<ContextData> outCommand;
    private static Consumer<String> printer;
    private Consumer<String> inCommandSort;
    private Consumer<Boolean> close;
    private Consumer<Runnable> addThread;
    private Consumer<Boolean> goOn;

    ClientModuleManager(Path configPath) {
        this.configPath = configPath;
        this.isAuthorised = false;
        this.threadPool = Executors.newCachedThreadPool();
        this.inAnalyser = new InputCommandAnalyser(this);
        initConsumers();

    }

    public void start() {
        configProc = new ClientConfigProcessor(this.configPath);
        configProc.readConfig();
        conData = new ContextData();
        this.pathHolder = new ClientPathHolder(configProc.getRepoPath(), s -> console.print(s));
        this.incomingReader = new ClientCommandReader(this, conData, pathHolder, goOn);
        client = new RepoClient(configProc.getHost(), configProc.getPort(), conData, pathHolder, incomingReader, addThread);
        console = new Console(inCommandSort);
        threadPool.submit(console);
        threadPool.submit(client);

    }

    public String getPathToRepo() {
        return configProc.getRepoPath();
    }

    public void addThread(Runnable runnable) {
        this.threadPool.submit(runnable);
    }

    public void setAuthorised(boolean res) {
        isAuthorised = res;
    }

    private void initConsumers() {
        this.inCommandSort = s -> threadPool.submit(new Thread(() -> inAnalyser.commandDecoder(s, isAuthorised)));
        this.addThread = threadPool::submit;
        printer = s -> console.print(s);
        this.outCommand = contextData -> client.execute(contextData);
        Consumer setRepoPath = (Consumer<String>) s -> {
            pathHolder.setAuthority(s);
            configProc.setRepoPath(s);
            configProc.writeConfig();
        };
        this.close = aBoolean -> {
            client.close();
            System.exit(0);
        };
        Consumer startConnection = (Consumer<Boolean>) aBoolean -> {
            client.close();
            client = new RepoClient(configProc.getHost(), configProc.getPort(), conData, pathHolder, incomingReader, addThread);
            threadPool.submit(client);
        };

        BiConsumer setHost = (BiConsumer<String, Integer>) (s, integer) -> {
            configProc.setHost(s);
            configProc.setPort(integer);
            configProc.writeConfig();
        };

        this.goOn = aBoolean -> client.goOn();

        inAnalyser.initConsumers(printer, outCommand, setRepoPath, close, startConnection, setHost);
    }


}


