package com.jangmo.web.config;

import com.jangmo.web.model.BaseUuidEntity;
import com.jangmo.web.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.TimeZone;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackageClasses = BaseUuidEntity.class)
@EnableJpaRepositories(basePackages = "com.jangmo.web.repository")
@EnableTransactionManagement(order = AspectOrder.TRANSACTION)
public class JpaConfiguration {
    static  {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

}
