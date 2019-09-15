package com.xs.middle.compent.ftd.autoconf.seletor;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 39466
 * @date 2019/9/15 18:25
 * 大哥,写点注释怎么样?
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SystemImportSelector.class)
public @interface EnableSystemAutoConfiguration {

    String system() default "windows";
}
