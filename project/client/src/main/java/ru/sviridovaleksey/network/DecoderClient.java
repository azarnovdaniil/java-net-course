package ru.sviridovaleksey.network;

import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;

public class DecoderClient extends ObjectDecoder {


    public DecoderClient(ClassResolver classResolver) {
        super(classResolver);
    }
}
