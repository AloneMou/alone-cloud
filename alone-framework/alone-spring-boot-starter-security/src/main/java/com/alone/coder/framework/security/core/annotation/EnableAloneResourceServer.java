package com.alone.coder.framework.security.core.annotation;

import com.alone.coder.framework.security.config.AloneResourceServerAutoConfiguration;
import com.alone.coder.framework.security.config.AloneResourceServerConfiguration;
import com.alone.coder.framework.security.core.feign.AloneFeignClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 资源服务注解
 */
@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({ AloneResourceServerAutoConfiguration.class, AloneResourceServerConfiguration.class,
		AloneFeignClientConfiguration.class })
public @interface EnableAloneResourceServer {

}
