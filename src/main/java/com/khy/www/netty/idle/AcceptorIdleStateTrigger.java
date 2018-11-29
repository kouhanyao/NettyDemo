package com.khy.www.netty.idle;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


/**
 * 服务端-空闲的连接和超时 处理
 */
@ChannelHandler.Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    /**
     * 当连接空闲时间太长时，将会触发一个IdleStateEvent 事件。然后，
     * 你可以通过在你的ChannelInboundHandler 中重写userEvent-
     * Triggered()方法来处理该IdleStateEvent 事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            //因为客户端是write，那么服务端自然是read，设置的状态就是IdleState.READER_IDLE
            if (state == IdleState.READER_IDLE) {
                throw new Exception("idle exception");
            }
        } else {
            //不是IdleStateEvent事件，所以将它传递给下一个Channel-InboundHandler
            super.userEventTriggered(ctx, evt);
        }
    }
}
