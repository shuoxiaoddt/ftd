package com.xs.middle.compent.netty.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xiaos
 * @date 01/04/2020 15:09
 */
public class ChatSendToTargetHandler extends ChannelInboundHandlerAdapter {

    private String message;

    public ChatSendToTargetHandler(String message) {
        this.message = message;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("service send to "+ ctx);
        System.out.println("message : " + message);
        byte[] bytes = message.getBytes();
        ByteBuf buffer = ctx.alloc().buffer(128);
        ctx.writeAndFlush(buffer);
    }
}
