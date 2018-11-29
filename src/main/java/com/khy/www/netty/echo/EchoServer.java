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
        /*与父Channel 相关联的EventLoopGroup 将分配一个EventLoop负责为传入连接请求创建
        Channel 。一旦连接被接受，第二个EventLoopGroup 就会给它的Channel
        分配一个EventLoop。见《netty实战》3.3*/
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
                    //当设置为true的时候，TCP会实现监控连接是否有效，当连接处于空闲状态的时候，超过了2个小时，本地的TCP实现会发送一个数据包给远程的 socket，
                    // 如果远程没有发回响应，TCP会持续尝试11分钟，直到响应为止，如果在12分钟的时候还没响应，TCP尝试关闭socket连接。
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //添加一个EchoServerHandler 到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            //绑定端口，同步等待成功
            //服务端启动辅助类配置完成之后，调用它的bind方法绑定监听端口
            //随后，调用它的同步阻塞方法sync等待绑定操作完成。
            //完成之后Netty会返回一个ChannelFuture，它的功能类似于JDK的java.util.concurrent.Future，主要用于异步操作的通知回调。
            ChannelFuture f = b.bind().sync();
            //获取channel的closeFuture，并且阻塞当前线程直到他完成
            if (f.isSuccess()) {
                logger.info("启动Netty Server成功，端口号：" + this.port);
            } else {
                logger.info("启动Netty Server失败，端口号：" + this.port);
            }
            //等待服务端监听端口关闭
            //使用f.channel().closeFuture().sync()方法进行阻塞,等待服务端链路关闭之后main函数才退出。
            f.channel().closeFuture().sync();
        } finally {
            //关闭EventLoopGroup，释放所有资源
            //优雅退出，释放线程池资源
            //调用NIO线程组的shutdownGracefully进行优雅退出，它会释放跟shutdownGracefully相关联的资源。
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
