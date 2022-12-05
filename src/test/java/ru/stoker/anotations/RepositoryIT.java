package ru.stoker.anotations;

import com.integralblue.log4jdbc.spring.Log4jdbcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.testconfig.TestDataSourceConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestDataSourceConfig.class, Log4jdbcAutoConfiguration.class})
@ActiveProfiles
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public @interface RepositoryIT {
    @AliasFor(
            annotation = ActiveProfiles.class
    )
    String[] profiles() default "iTest";
}
