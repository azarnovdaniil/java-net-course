package ru.sviridovaleksey.newClientConnection;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import ru.sviridovaleksey.Command;

import java.io.Serializable;


public class Decoder extends ObjectDecoder{


    public Decoder(ClassResolver classResolver) {
        super(classResolver);
    }
}