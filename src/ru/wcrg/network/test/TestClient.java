package ru.wcrg.network.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * Created by Эдуард on 09.01.2018.
 */
public class TestClient {
    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 4444;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ClientDecoder(), new ClientHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();

            byte[] message = "Hello, netty".getBytes();
            ByteBuf firstMessage = Unpooled.buffer(message.length);
            firstMessage.writeBytes(message);
            f.channel().writeAndFlush(firstMessage);
            System.out.println("Сообщение типа отправлено");

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
