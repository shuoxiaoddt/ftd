package com.xs.middle.compent;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaos
 * @date 2020/1/10 11:43
 */
public class AgentDemo {
    private static String className = "hello.GreetingController";
    private static String methodName = "getDomain";

    public static void agentmain(String args, Instrumentation instrumentation) {

        try {
            List<Class> needRetransFormClasses = new LinkedList<>();
            Class[] loadedClass = instrumentation.getAllLoadedClasses();
            for (int i = 0; i < loadedClass.length; i++) {
                if (loadedClass[i].getName().equals(className)) {
                    needRetransFormClasses.add(loadedClass[i]);
                }
            }
            instrumentation.addTransformer(new TestTransformer(className, methodName));
            instrumentation.retransformClasses(needRetransFormClasses.toArray(new Class[0]));
        } catch (Exception e) {

        }
    }

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new TestTransformer(className, methodName));
    }
}
