package com.xs.middle.compent.netty.chat;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author xiaos
 * @date 31/03/2020 16:38
 */
public class ChatServerMessageSendHandler extends ChannelDuplexHandler {

    public final EventLoopGroup workGroup = new NioEventLoopGroup(5);

    public final LinkedBlockingDeque<ChatMessage> chatMessages = new LinkedBlockingDeque<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("sever channel read : " + this);
        ChatMessage chatMessage = new Gson().fromJson(msg.toString(),ChatMessage.class);
        System.out.println(chatMessage);
        ByteBuf buffer = ctx.alloc().buffer(10);
        buffer.writeBytes("receiverd".getBytes());
        ctx.writeAndFlush(buffer);
        Observable.just(chatMessage).observeOn(Schedulers.newThread()).subscribe(x -> {
            sendToTarger(chatMessage);
        });
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("bind");
        super.bind(ctx, localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("connect");
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("read");
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("write");
        super.write(ctx, msg, promise);
    }

    public void sendToTarger(ChatMessage chatMessage) throws InterruptedException {
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChatSendToTargetHandler(chatMessage.getContent()));
                        }
                    });

            ChannelFuture f = bootstrap.connect(chatMessage.getToIp(), chatMessage.getToPort()).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
