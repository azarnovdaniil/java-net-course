package ru.sviridovaleksey.newclientconnection;

import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;

public class Decoder extends ObjectDecoder {
    public Decoder(ClassResolver classResolver) {
    super(classResolver); }
}
