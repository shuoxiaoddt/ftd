package com.xs.middle.compent.netty.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;


/**
 * @author xiaos
 * @date 31/03/2020 16:37
 */
public class ChatClientMessageSendHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        while (true){
            ByteBuf byteBuf = ctx.alloc().buffer(1024);
            String s = CharClientServer.messgeQueue.takeFirst();
            byteBuf.writeBytes(s.getBytes());
            ChannelFuture channelFuture = ctx.writeAndFlush(byteBuf);
            channelFuture.addListener(x -> {
                byteBuf.resetReaderIndex();
                byteBuf.resetWriterIndex();
            });
        }
    }
}
