package server;


import io.netty.bootstrap.ServerBootstrap;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Server
{
    ExecutorService bossExec = new OrderedMemoryAwareThreadPoolExecutor(1, 400000000, 2000000000, 60, TimeUnit.SECONDS);
    ExecutorService ioExec = new OrderedMemoryAwareThreadPoolExecutor(4 , 400000000, 2000000000, 60, TimeUnit.SECONDS);
    ServerBootstrap networkServer = new ServerBootstrap(new NioServerSocketChannelFactory(bossExec, ioExec,  4 ));
networkServer.setOption("backlog", 500);
networkServer.setOption("connectTimeoutMillis", 10000);
networkServer.setPipelineFactory(new ServerPipelineFactory());
    Channel channel = networkServer.bind(new InetSocketAddress(address, port));





    public static void main(String[] args)
    {

    }



}
