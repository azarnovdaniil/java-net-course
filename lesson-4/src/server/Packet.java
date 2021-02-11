package server;

import java.io.IOException;

public abstract class Packet
{
    public static Packet read(ChannelBuffer buffer) throws IOException {
    int id = buffer.readUnsignedShort(); // Получаем ID пришедшего пакета, чтобы определить, каким классом его читать
    Packet packet = getPacket(id); // Получаем инстанс пакета с этим ID
    if(packet == null)
        throw new IOException("Bad packet ID: " + id); // Если произошла ошибка и такого пакета не может быть, генерируем исключение
    packet.get(buffer); // Читаем в пакет данные из буфера
    return packet;
}

    public static Packet write(Packet packet, ChannelBuffer buffer) {
        buffer.writeChar(packet.getId()); // Отправляем ID пакета
        packet.send(buffer); // Отправляем данные пакета
    }


    // Функции, которые должен реализовать каждый класс пакета
    public abstract void get(ChannelBuffer buffer);
    public abstract void send(ChannelBuffer buffer);
}

