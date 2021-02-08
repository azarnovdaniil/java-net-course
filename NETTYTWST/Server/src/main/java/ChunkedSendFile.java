import io.netty.handler.stream.ChunkedNioStream;

import java.nio.channels.ReadableByteChannel;

public class ChunkedSendFile extends ChunkedNioStream {
    public ChunkedSendFile(ReadableByteChannel in) {
        super(in);
    }

    public ChunkedSendFile(ReadableByteChannel in, int chunkSize) {
        super(in, chunkSize);
    }
}
