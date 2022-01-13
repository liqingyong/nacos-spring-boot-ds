package com.eternitystar.nacos.enable;

import com.eternitystar.nacos.configurer.NacosConfigurer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(NacosConfigurer.class)
@Documented
public @interface EnableFpfNacos {

}
