package com.xs.middle.compent.ftd.autoconf.seletor;

import com.xs.middle.compent.ftd.autoconf.LinuxSystem;
import com.xs.middle.compent.ftd.autoconf.WindowsSystem;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author 39466
 * @date 2019/9/15 13:59
 * 大哥,写点注释怎么样?
 */
public class SystemImportSelector implements ImportSelector , EnvironmentAware {

    private Environment environment;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> annotationAttributes =
                importingClassMetadata.getAnnotationAttributes(EnableSystemAutoConfiguration.class.getName());
        String system = environment.getProperty("operate.system");
        System.out.println(system);
        return new String[]{"windows".equals(system) ? new WindowsSystem().getClass().getName()
                : new LinuxSystem().getClass().getName()};
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
