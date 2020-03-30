package com.xs.middle.compent.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xiaos
 * @date 27/03/2020 15:39
 */
public class SelectorDemo {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress socketAddress = new InetSocketAddress(8081);
        serverSocket.bind(socketAddress);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    SocketChannel accept = server.accept();
                    accept.configureBlocking(false);
                    accept.register(selector,SelectionKey.OP_READ);
                }
                if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    int len = channel.read(byteBuffer);
                    if(len > 0){
                        byteBuffer.flip();
                        String content = new String(byteBuffer.array(),0,len);
                        SelectionKey sKey = channel.register(selector, SelectionKey.OP_WRITE);
                        sKey.attach(content);
                    }else{
                        channel.close();
                    }
                    byteBuffer.clear();
                }
                if(key.isWritable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    String content = (String) key.attachment();
                    ByteBuffer block = ByteBuffer.wrap(("输出内容：" + content).getBytes());
                    if(block != null){
                        channel.write(block);
                    }else{
                        channel.close();
                    }
                }
            }
        }


    }
}
