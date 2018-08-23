
package com.wnc.sboot1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Hello world!
 */
@SpringBootApplication
// @EnableScheduling
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
