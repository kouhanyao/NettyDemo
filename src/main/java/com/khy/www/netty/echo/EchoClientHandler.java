package com.khy.www.netty.echo;

import com.khy.www.netty.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

/**
 * 在echo协议下实现的处理器
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws UnsupportedEncodingException {
        ctx.writeAndFlush(NettyUtils.getSendByteBuf("客户端-->服务端 你好"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO Auto-generated method stub
        ByteBuf buf = (ByteBuf) msg;// 获取服务端传来的Msg
        String recieved = NettyUtils.getMessage(buf);
        System.out.println("------客户端收到信息------" + recieved);
        buf.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
