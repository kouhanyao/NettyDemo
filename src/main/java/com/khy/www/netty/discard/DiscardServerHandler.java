package com.khy.www.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 该类实现处理器（handle）
 * 目的：处理服务端channel
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ByteBuf 是一个引用计数对象,此处必须显示的调用release进行释放
        ByteBuf in = (ByteBuf) msg;
        System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
        in.release();
    }

    /**
     * 该事件处理方法是当出现 Throwable 对象才会被调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
