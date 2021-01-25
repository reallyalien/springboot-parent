package com.ot.springtest.config;

import com.ot.springtest.dto.Pig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {

    /**
     * 将此方法的返回值放入ioc容器
     * @param importingClassMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{Pig.class.getName()};
    }
}
