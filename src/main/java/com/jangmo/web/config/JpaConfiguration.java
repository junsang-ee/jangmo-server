package com.jangmo.web.config;

import com.jangmo.web.config.dsl.QueryDslJpaRepositoryFactoryBean;
import com.jangmo.web.model.BaseUuidEntity;
import com.jangmo.web.repository.UserRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackageClasses = BaseUuidEntity.class)
@EnableJpaRepositories(
        basePackageClasses = UserRepository.class,
        repositoryFactoryBeanClass = QueryDslJpaRepositoryFactoryBean.class
)
@EnableTransactionManagement(order = AspectOrder.TRANSACTION)
public class JpaConfiguration {
    static  {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
