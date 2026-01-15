package com.jangmo.web.repository.base;

import com.jangmo.web.config.JpaConfiguration;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfiguration.class)
public abstract class BaseRepositoryTest {

}
