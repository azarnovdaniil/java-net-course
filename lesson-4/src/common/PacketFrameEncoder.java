package common;

import io.netty.channel.ChannelHandlerContext;
import server.Packet;

import java.nio.channels.Channel;

public class PacketFrameEncoder extends OneToOneEncoder
{
    @Override
    protected Object encode(ChannelHandlerContext channelhandlercontext, Channel channel, Object obj) throws Exception {
        if(!(obj instanceof Packet))
            return obj; // Если это не пакет, то просто пропускаем его дальше
        Packet p = (Packet) obj;

        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(); // Создаём динамический буфер для записи в него данных из пакета
        Packet.write(p, buffer); // Пишем пакет в буфер
        return buffer;
        // Возвращаем буфер, который и будет записан в канал
    }
}
