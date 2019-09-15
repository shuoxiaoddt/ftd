package com.xs.middle.compent.ftd.autoconf.register;

import com.xs.middle.compent.ftd.autoconf.seletor.SystemImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 39466
 * @date 2019/9/15 19:29
 * 大哥,写点注释怎么样?
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SystemImportRegister.class)
public @interface EnableSystemAutoRegisterAutoConfiguration {
}
