package com.xs.middle.compent.unittest;

import java.lang.annotation.*;
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UnitTestAnnotation {

    String resource() default "";

    String description() default "";
}
