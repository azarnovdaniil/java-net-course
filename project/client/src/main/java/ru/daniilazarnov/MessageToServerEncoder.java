package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

public class MessageToServerEncoder extends MessageToByteEncoder<MessagePacket> {

//    private final Charset charset = Charset.forName("UTF-8");
MessagePacket msg = new MessagePacket();
        msg.setCommandToServer(commandToServer.CREATE);
        msg.setPathToFileName("dir/test1.txt");
        msg.setContentMessage("Hello! This message was saved to file.");
        ctx.writeAndFlush(msg);


    }
    @Override
    protected void encode(ChannelHandlerContext ctx, MessagePacket msg, ByteBuf out) throws Exception {

        ObjectOutputStream oos = new ObjectOutputStream(msg.);
        TestSerial ts = new TestSerial();
        oos.writeObject(ts);
        oos.flush();
        oos.close();

        }while (commandToServer.valueOf(msg.setCommandToServer())) msg.setCommandToServer();
                sout.writeObject(db[i]);
                out.writeBytes(InputStream.nullInputStream().msg.getCommandToServer().getNumberOFCommand());
                out.writeCharSequence(msg.getPathToFileName(), charset);
                out.writeCharSequence(msg.getContentMessage(), charset);
            }
        }
    }