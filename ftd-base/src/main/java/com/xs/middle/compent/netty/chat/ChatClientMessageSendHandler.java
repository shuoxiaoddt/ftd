package com.xs.middle.compent.netty.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


/**
 * @author xiaos
 * @date 31/03/2020 16:37
 */
public class ChatClientMessageSendHandler extends ChannelInboundHandlerAdapter {

    private volatile Boolean start = Boolean.FALSE;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active start : " + start+" obj: "+ this);
        if(!start){
            synchronized (this){
                if(!start){
                    start = Boolean.TRUE;
                    Observable.just(ctx).observeOn(Schedulers.newThread()).subscribe(item -> {
                        while (true){
                            ByteBuf byteBuf = item.alloc().buffer(1024);
                            String s = CharClientServer.messgeQueue.takeFirst();
                            byteBuf.writeBytes(s.getBytes());
                            ChannelFuture channelFuture = item.writeAndFlush(byteBuf);
                            channelFuture.addListener(x -> {
                                byteBuf.resetReaderIndex();
                                byteBuf.resetWriterIndex();
                            });
                        }
                    });
                }
            }
        }
    }

}
