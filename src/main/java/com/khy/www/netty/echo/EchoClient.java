package com.khy.www.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            //指定EventLoopGroup 以处理客户端事件；需要适用于NIO 的实现
            b.group(group)
                    //适用于NIO 传输的Channel 类型
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //在创建Channel 时，向ChannelPipeline中添加一个Echo-ClientHandler 实例
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoClientHandler());
                        }
                    });

            //连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            if (f.isSuccess()) {
                System.out.println("------connect server success------");
            } else {
                System.out.println("------connect server fail------");
            }
            //阻塞，直到Channel 关闭
            f.channel().closeFuture().sync();
        } finally {
            //关闭线程池并且释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        String host = "127.0.0.1";
        new EchoClient(host, port).start();
    }
}
