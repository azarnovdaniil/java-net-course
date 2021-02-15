package common;

import io.netty.channel.ChannelFutureListener;


public class Method {
    public static final String user1 = "user1";


    public static ChannelFutureListener getChannelFutureListener(String s) {
        return future -> {
            if (!future.isSuccess()) {
                System.err.println(s + "не был");
            }
            if (future.isSuccess()) {
                System.out.print(s);
            }
        };
    }
}

