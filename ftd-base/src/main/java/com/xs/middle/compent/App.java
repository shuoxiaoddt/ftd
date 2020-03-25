package com.xs.middle.compent;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.xs.middle.compent");
        System.out.println( "Hello World!" );
    }
}
