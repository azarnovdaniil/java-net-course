package server;

import io.netty.channel.ChannelPipeline;

import java.nio.channels.Channels;

public class ServerPipeLineFactory implements ChannelPipelineFactory
{
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        PacketFrameDecoder decoder = new PacketFrameDecoder();
        PacketFrameEncoder encoder = new PacketFrameEncoder();
        return Channels.pipeline(decoder, encoder, new PlayerHandler(decoder, encoder));
    }
}
