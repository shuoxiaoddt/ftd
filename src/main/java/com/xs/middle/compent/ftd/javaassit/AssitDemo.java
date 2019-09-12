package com.xs.middle.compent.ftd.javaassit;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author xiaos
 * @date 2019/9/6 17:14
 */
public class AssitDemo {

    public static void main(String[] args) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass cc = classPool.get("com.xs.middle.compent.ftd.javaassit.Hello");
        CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore(" System.out.print(\"Hello.say():\"); ");
        Class c = cc.toClass();
        Hello hello = (Hello)c.newInstance();
        hello.say();
    }
}
