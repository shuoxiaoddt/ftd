package com.xs.middle.compent.ftd.asm;

import org.objectweb.asm.*;

/**
 * @author xiaos
 * @date 2019/9/6 16:49
 */
public class MyClassVisitor implements Opcodes, ClassVisitor {


    @Override
    public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {

    }

    @Override
    public void visitSource(String s, String s1) {

    }

    @Override
    public void visitOuterClass(String s, String s1, String s2) {

    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return null;
    }

    @Override
    public void visitAttribute(Attribute attribute) {

    }

    @Override
    public void visitInnerClass(String s, String s1, String s2, int i) {

    }

    @Override
    public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
        return null;
    }

    @Override
    public void visitEnd() {

    }
}
