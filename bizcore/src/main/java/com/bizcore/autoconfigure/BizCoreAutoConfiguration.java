package com.bizcore.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Auto-configuration for BizCore.
 * This class is automatically loaded when bizcore is included as a dependency.
 */
@AutoConfiguration
@ComponentScan(basePackages = "com.bizcore")
@EntityScan(basePackages = "com.bizcore.entity")
@EnableJpaRepositories(basePackages = "com.bizcore.repository")
public class BizCoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BizCoreProperties bizCoreProperties() {
        return new BizCoreProperties();
    }
}
