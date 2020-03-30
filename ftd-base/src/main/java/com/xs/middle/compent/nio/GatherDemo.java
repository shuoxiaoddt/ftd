package com.xs.middle.compent.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author xiaos
 * @date 29/03/2020 16:31
 */
public class GatherDemo {

    static private final int firstHeaderLength = 2;
    static private final int secondHeaderLength = 4;
    static private final int bodyLength = 6;


    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress(8888);
        serverSocketChannel.socket().bind(socketAddress);
        SocketChannel sc = serverSocketChannel.accept();
        int messageLength =
                firstHeaderLength + secondHeaderLength + bodyLength;

        ByteBuffer buffers[] = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate( firstHeaderLength );
        buffers[1] = ByteBuffer.allocate( secondHeaderLength );
        buffers[2] = ByteBuffer.allocate( bodyLength );

        while (true){
            int bytesRead = 0;
            while (bytesRead < messageLength) {
                long r = sc.read( buffers );
                bytesRead += r;

                System.out.println( "r "+r );
                for (int i=0; i<buffers.length; ++i) {
                    ByteBuffer bb = buffers[i];
                    System.out.println( "b "+i+" "+bb.position()+" "+bb.limit() );
                }
            }
            for (int i=0; i<buffers.length; ++i) {
                ByteBuffer bb = buffers[i];
                bb.flip();
            }
            long bytesWritten = 0;
            while (bytesWritten<messageLength) {
                long r = sc.write( buffers );
                bytesWritten += r;
            }
            // Clear buffers
            for (int i=0; i<buffers.length; ++i) {
                ByteBuffer bb = buffers[i];
                bb.clear();
            }
            System.out.println( bytesRead+" "+bytesWritten+" "+messageLength );

        }
    }
}
