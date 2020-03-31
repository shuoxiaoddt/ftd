package com.xs.middle.compent.netty.chat;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author xiaos
 * @date 31/03/2020 16:03
 */
public class CharClientServer {

    private static final String TO_IP = "127.0.0.1";
    private static final String FROM_IP = "127.0.0.1";

    private static final int SERVER_PORT = 9090;
    private static final String SERVER_IP = "127.0.0.1";

    public static final LinkedBlockingDeque<String> messgeQueue = new LinkedBlockingDeque();

    public static void main(String[] args) {
        int fromPort = Integer.valueOf(args[0]);
        int toPort = Integer.valueOf(args[1]);

        startReceListener(fromPort);
        startSendListener();
        Scanner scanner = new Scanner(System.in);
        Gson gson = new Gson();
        while (scanner.hasNextLine()){
            String content = scanner.nextLine();
            ChatMessage chatMessage = new ChatMessage(FROM_IP,fromPort,TO_IP,toPort,content);
            messgeQueue.add(gson.toJson(chatMessage));
        }
    }
    private static void startSendListener() {
        new Thread(() -> {
            EventLoopGroup eventExecutors = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventExecutors)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new ChatClientMessageSendHandler());
                                ch.pipeline().addLast(new ChatClientMessageReceHandler());
                            }
                        })
                        .option(ChannelOption.SO_KEEPALIVE,true);
                ChannelFuture f = bootstrap.connect(SERVER_IP, SERVER_PORT).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                eventExecutors.shutdownGracefully();
            }
        }).start();
    }

    private static void startReceListener(int port) {
        Thread chatClientServerThread = new Thread(() -> {
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            try {
                bootstrap
                        .group(eventLoopGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new ChatClientMessageReceHandler());
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG,128);
                ChannelFuture f = bootstrap.bind(port).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        chatClientServerThread.start();
        System.out.println("start read to rece message!");

    }
}
