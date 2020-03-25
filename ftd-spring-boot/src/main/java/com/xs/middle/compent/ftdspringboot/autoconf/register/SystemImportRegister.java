package com.xs.middle.compent.ftdspringboot.autoconf.register;

import com.xs.middle.compent.ftdspringboot.autoconf.LinuxSystem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author 39466
 * @date 2019/9/15 19:29
 * 大哥,写点注释怎么样?
 */
public class SystemImportRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        RootBeanDefinition beanDefinition = new RootBeanDefinition(LinuxSystem.class);
        String uncapitalize = StringUtils.uncapitalize(LinuxSystem.class.getName());
        registry.registerBeanDefinition(uncapitalize,beanDefinition);
    }
}
