package com.alone.coder.framework.file.config;

import com.alone.coder.framework.file.core.context.StorageSourceContext;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * agoni 博客文件自动配置
 *
 * @author AgoniMou
 * @date 2024/12/10
 */
@AutoConfiguration
public class AloneFileAutoConfiguration {

	/**
	 * 存储源上下文
	 * @return {@link StorageSourceContext }
	 */
	@Bean
	public StorageSourceContext storageSourceContext() {
		return new StorageSourceContext();
	}

}
