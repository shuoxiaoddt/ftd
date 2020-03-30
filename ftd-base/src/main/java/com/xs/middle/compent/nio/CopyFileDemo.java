package com.xs.middle.compent.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xiaos
 * @date 29/03/2020 11:14
 */
public class CopyFileDemo {

    private static final String PATH = "D:\\shiplabel.txt";
    private static final String TARGET_PATH = "D:\\shiplabel-bak.txt";
    public static void main(String[] args) throws IOException {
        FileInputStream ins = new FileInputStream(PATH);
        FileOutputStream ous = new FileOutputStream(TARGET_PATH);

        FileChannel insChannel = ins.getChannel();
        FileChannel ousChannel = ous.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true){
            buffer.clear();
            int read = insChannel.read(buffer);
            if(read == -1){
                break;
            }
            buffer.flip();
            ousChannel.write(buffer);
        }
    }
}
