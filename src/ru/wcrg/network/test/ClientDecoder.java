package ru.wcrg.network.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Эдуард on 09.01.2018.
 */
public class ClientDecoder extends ByteToMessageDecoder { // (1)
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
        System.out.println("try decode");
        if (in.readableBytes() < 2) {
            return;
        }
        out.add(in.readBytes(2));
    }
}
