package com.khy.www.netty.echo;

import com.khy.www.netty.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

/**
 * 处理服务端 channel.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送信息
        ctx.writeAndFlush(NettyUtils.getSendByteBuf("服务器-->客户端 你好"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        System.out.println(ctx.channel().remoteAddress() + "->Server :" + msg.toString());
        // TODO Auto-generated method stub
        ByteBuf buf = (ByteBuf) msg;// 获取服务端传来的Msg
        String recieved = NettyUtils.getMessage(buf);
        System.out.println("------服务端收到信息------" + recieved);
        buf.release();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.write(NettyUtils.getSendByteBuf("服务器-->客户端(2) 你好"));
        ctx.write(NettyUtils.getSendByteBuf("服务器-->客户端(3) 你好"));
        ctx.write(NettyUtils.getSendByteBuf("服务器-->客户端(4) 你好"));
        ctx.write(NettyUtils.getSendByteBuf("服务器-->客户端(5) 你好"));
        ctx.writeAndFlush(NettyUtils.getSendByteBuf("服务器-->客户端(6) 你好"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
