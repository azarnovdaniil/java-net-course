package server;

public class Packet1Login
{
    // Пакет, которым клиент передаёт серверу свой логин
    public class Packet1Login extends Packet {
        public String login;

        public void get(ChannelBuffer buffer) {
            int length = buffer.readShort();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < length ++i)
            builder.append(buffer.readChar());
            login = builder.toString();
        }

        public void send(ChannelBuffer buffer) {
            // Тело отправки пустое, т.к. сервер не посылает этот пакет
        }
    }

    // Пакет, которым сервер выкидывает клиента с указаной причиной, или клиент отключается от сервера
    public class Packet255KickDisconnect extends Packet {
        public String reason;

        public void get(ChannelBuffer buffer) {
            int length = buffer.readShort();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < length ++i)
            builder.append(buffer.readChar());
            reason = builder.toString();
        }

        public void send(ChannelBuffer buffer) {
            buffer.writeShort(reason.length());
            for(int i = 0; i < reason.length(); ++i) {
                buffer.writeChar(reason.getCharAt(i));
            }
        }
    }
}
