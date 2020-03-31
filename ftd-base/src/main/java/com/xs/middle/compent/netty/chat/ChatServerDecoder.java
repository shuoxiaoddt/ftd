package com.xs.middle.compent.netty.chat;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xiaos
 * @date 31/03/2020 16:51
 */
public class ChatServerDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel Read !");
        ByteBuf byteBuf = (ByteBuf)msg;
        int i = byteBuf.readableBytes();
        byte b[] = new byte[i];
        byteBuf.readBytes(b);
        String content = new String(b);
        ChatMessage chatMessage = new Gson().fromJson(content,ChatMessage.class);
        System.out.println(chatMessage);
        super.channelRead(ctx, msg);
    }
}
