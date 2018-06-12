package com.khy.www.netty.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;

public class NettyUtils {

    /*
     * 从ByteBuf中获取信息 使用UTF-8编码返回
     */
    public static String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 将Sting转化为UTF-8编码的字节
     */
    public static ByteBuf getSendByteBuf(String message) throws UnsupportedEncodingException {
        byte[] req = message.getBytes("UTF-8");
        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(req);
        return pingMessage;
    }
}
