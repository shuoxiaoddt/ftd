package com.xs.middle.compent.nio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author xiaos
 * @date 27/03/2020 10:50
 */
public class IntBufferDemo {
    public static void main(String[] args) {

        IntBuffer buffer = IntBuffer.allocate(10);
        for(int i = 0 ; i <  buffer.capacity() ; i ++){
            buffer.put(i);
        }
        System.out.println("pos : "+buffer.position()+" limit : " +buffer.limit()+ " cap : "+buffer.capacity());
        buffer.flip();
        System.out.println("pos : "+buffer.position()+" limit : " +buffer.limit()+ " cap : "+buffer.capacity());
        buffer.get();
        buffer.get();
        buffer.flip();
        System.out.println("pos : "+buffer.position()+" limit : " +buffer.limit()+ " cap : "+buffer.capacity());
        buffer.get();
        buffer.get();
        System.out.println("pos : "+buffer.position()+" limit : " +buffer.limit()+ " cap : "+buffer.capacity());
        buffer.flip();
        buffer.put(1);
        buffer.put(1);
        System.out.println("pos : "+buffer.position()+" limit : " +buffer.limit()+ " cap : "+buffer.capacity());

    }
}
