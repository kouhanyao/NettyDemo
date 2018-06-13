package com.khy.www.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 处理服务端 channel.
 */
public class EchoServer {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        //创建并分配一个NioEventLoopGroup 实例以进行事件的处理，如接受新连接以及读/写数据；
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //引导和绑定服务器
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    //指定所使用的nio传输channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    //当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
                    // 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                    .option(ChannelOption.TCP_NODELAY, true)
                    //心跳保活机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //添加一个EchoServerHandler 到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            //异步绑定服务器；调用sync()方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            //获取channel的closeFuture，并且阻塞当前线程直到他完成
            if (f.isSuccess()) {
                logger.info("启动Netty Server成功，端口号：" + this.port);
            } else {
                logger.info("启动Netty Server失败，端口号：" + this.port);
            }
            // 等待服务器  channel关闭 。
            f.channel().closeFuture().sync();
        } finally {
            //关闭EventLoopGroup，释放所有资源
            workerGroup.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new EchoServer(port).run();
    }
}
