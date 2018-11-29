package com.khy.www.netty.echo;

import com.khy.www.netty.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;

/**
 * 处理服务端 channel.
 */
//标记一个channelHandle可以被多个channel安全的共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

   /* @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送信息
        ctx.writeAndFlush(NettyUtils.getSendByteBuf("服务器-->客户端 你好"));
    }*/

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf in = (ByteBuf) msg;// 获取服务端传来的Msg
        System.out.println("server received:" + in.toString(CharsetUtil.UTF_8));
        //将接收到的消息写给发送者，二部冲刷出站消息
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       /* ctx.write(NettyUtils.getSendByteBuf("服务器-->客户端(2) 你好"));
        ctx.write(NettyUtils.getSendByteBuf("服务器-->客户端(3) 你好"));
        ctx.write(NettyUtils.getSendByteBuf("服务器-->客户端(4) 你好"));
        ctx.write(NettyUtils.getSendByteBuf("服务器-->客户端(5) 你好"));
        ctx.writeAndFlush(NettyUtils.getSendByteBuf("服务器-->客户端(6) 你好"));*/
        //将未决信息冲刷到远程节点，并且关闭该channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 打印异常栈追踪
        cause.printStackTrace();
        //关闭该channel
        ctx.close();
    }
}
