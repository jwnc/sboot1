
package com.wnc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 */
@SpringBootApplication
// @EnableScheduling
@EnableJpaRepositories( basePackages = {"com.wnc"} )
@EnableJpaAuditing
// @EnableTransactionManagement
public class App extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application )
    {
        return application.sources( App.class );
    }

    public static void main( String[] args )
    {
        SpringApplication.run( App.class, args );
    }
}
